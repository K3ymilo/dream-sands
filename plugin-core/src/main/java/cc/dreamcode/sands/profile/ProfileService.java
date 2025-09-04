package cc.dreamcode.sands.profile;

import cc.dreamcode.platform.DreamLogger;
import cc.dreamcode.sands.SandsPlugin;
import cc.dreamcode.sands.config.PluginConfig;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.injector.annotation.PostConstruct;
import eu.okaeri.tasker.bukkit.BukkitTasker;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ProfileService {

    private final DreamLogger dreamLogger;
    private final SandsPlugin sandsPlugin;
    private final ProfileCache profileCache;
    private final ProfileRepository profileRepository;
    private final BukkitTasker tasker;
    private final PluginConfig pluginConfig;

    @PostConstruct
    private void initialize() {
        Objects.requireNonNull(this.profileCache);
        this.profileRepository.findAll().forEach(this.profileCache::registerProfile);
        if (this.sandsPlugin.getComponentService().isDebug())
            this.dreamLogger.info((new DreamLogger.Builder())
                    .type("Loaded profiles")
                    .name(String.valueOf(getProfilesAmount()))
                    .build());
    }

    public int getProfilesAmount() {
        return this.profileCache.getProfilesAmount();
    }

    public int getOnlineProfilesAmount() {
        return this.profileCache.getOnlineAmount();
    }

    public boolean isOnline(@NonNull UUID playerUuid) {
        return getOnlineUuids().contains(playerUuid);
    }

    public List<Profile> getProfiles() {
        return this.profileCache.getProfiles();
    }

    public Optional<Profile> getProfile(@NonNull UUID uuid) {
        return this.profileCache.getProfile(uuid);
    }

    public Optional<Profile> getProfile(@NonNull String name) {
        return this.profileCache.getProfile(name);
    }

    public Optional<Profile> getProfile(@NonNull HumanEntity player) {
        return this.profileCache.getProfile(player.getUniqueId());
    }

    public Map<UUID, String> getOnline() {
        return this.profileCache.getOnline();
    }

    public List<UUID> getOnlineUuids() {
        return this.profileCache.getOnlineUuids();
    }

    public List<String> getOnlineNames() {
        return this.profileCache.getOnlineNames();
    }

    public Profile getProfileOrCreate(@NonNull HumanEntity player) {
        return this.profileCache.getProfile(player.getUniqueId())
                .orElseGet(() -> awaitProfile(player));
    }

    public List<Profile> getProfilesByUuid(@NonNull List<UUID> profileUuids) {
        return profileUuids
                .stream()
                .map(this::getProfile)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    // Nowa, poprawiona metoda do modyfikacji i zapisu
    public void modifyProfile(@NonNull UUID playerUuid, @NonNull Consumer<Profile> consumer) {
        this.tasker.newSharedChain("dbops;" + playerUuid)
                .async(() -> {
                    Profile profile = this.profileRepository.findOrCreateByPath(playerUuid);
                    consumer.accept(profile);
                    this.profileCache.registerProfile(profile); // Zsynchronizuj z cachem
                    profile.save();
                })
                .execute();
    }

    // Zmieniona metoda, używa nowej, poprawionej wersji
    public void modifyProfile(@NonNull HumanEntity player, @NonNull Consumer<Profile> consumer) {
        this.modifyProfile(player.getUniqueId(), consumer);
    }

    // Zmieniona metoda, używa nowej, poprawionej wersji
    public void modifyProfile(@NonNull Profile profile, @NonNull Consumer<Profile> consumer) {
        this.modifyProfile(profile.getUniqueId(), consumer);
    }

    public void uptimeProfileData(@NonNull HumanEntity player) {
        this.tasker.newSharedChain("dbops;" + player.getUniqueId())
                .async(() -> {
                    Profile profile = this.profileRepository.findOrCreateByPath(player.getUniqueId());
                    profile.setName(player.getName());
                    profile.setOnline(true);
                    this.profileCache.registerProfile(profile);
                    profile.save();
                })
                .execute();
    }

    private Profile awaitProfile(@NonNull HumanEntity player) {
        Profile profile = this.profileRepository.findOrCreateByPath(player.getUniqueId());
        profile.setName(player.getName());
        profile.setOnline(true);
        this.profileCache.registerProfile(profile);
        return profile;
    }

    public void importProfile(@NonNull UUID profileUuid) {
        this.tasker.newSharedChain("dbops;" + profileUuid)
                .async(() -> {
                    Optional<Profile> optionalProfile = this.profileRepository.findByPath(profileUuid);
                    if (!optionalProfile.isPresent()) {
                        this.dreamLogger.warning("Profile with uuid " + profileUuid + " not found, unregistering.");
                        this.profileCache.unregisterProfile(profileUuid);
                        return;
                    }
                    Profile profile = optionalProfile.get();
                    Optional<Profile> optionalCached = getProfile(profileUuid);
                    if (optionalCached.isPresent()) {
                        Profile cachedProfile = optionalCached.get();
                        cachedProfile.load(profile);
                        this.profileCache.syncOnlineStatus(cachedProfile);
                        return;
                    }
                    this.profileCache.registerProfile(profile);
                }).execute();
    }

    public UUID getCommandSenderUuid(@NonNull CommandSender sender) {
        if (sender instanceof Entity) {
            Entity entity = (Entity) sender;
            return entity.getUniqueId();
        }
        if (sender instanceof org.bukkit.command.ConsoleCommandSender)
            return getConsoleUuid();
        throw new RuntimeException("unknown sender type: " + sender.getName());
    }

    public UUID getConsoleUuid() {
        return this.pluginConfig.consoleSenderUuid;
    }

    public void addKill(@NonNull UUID playerUuid) {
        this.modifyProfile(playerUuid, profile -> profile.getProfileStatistics().addKill());
    }

    public void resetKillstreak(@NonNull UUID playerUuid) {
        this.modifyProfile(playerUuid, profile -> profile.getProfileStatistics().resetKillstreak());
    }

    public int getKillstreak(@NonNull UUID playerUuid) {
        return this.getProfile(playerUuid)
                .map(profile -> profile.getProfileStatistics().getKillstreak())
                .orElse(0);
    }
}