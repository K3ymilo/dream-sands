package cc.dreamcode.sands.profile;

import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.*;

public class ProfileCache {


    private final Map<UUID, Profile> profileMap = new HashMap<>();

    private final Map<UUID, String> onlineMap = new HashMap<>();

    public int getProfilesAmount() {
        return this.profileMap.size();
    }

    public int getOnlineAmount() {
        return this.onlineMap.size();
    }

    public List<Profile> getProfiles() {
        return new ArrayList<>(this.profileMap.values());
    }

    public Map<UUID, String> getOnline() {
        return Collections.unmodifiableMap(this.onlineMap);
    }

    public List<UUID> getOnlineUuids() {
        return new ArrayList<>(this.onlineMap.keySet());
    }

    public List<String> getOnlineNames() {
        return new ArrayList<>(this.onlineMap.values());
    }

    public Optional<Profile> getProfile(@NonNull UUID uuid) {
        return Optional.ofNullable(this.profileMap.get(uuid));
    }

    public Optional<Profile> getProfile(@NonNull String name) {
        return this.profileMap.values()
                .stream()
                .filter(profile -> (profile.getName() != null && profile.getName().equalsIgnoreCase(name)))

                .findAny();
    }

    public void registerProfile(@NonNull Profile profile) {
        this.profileMap.put(profile.getUniqueId(), profile);
        syncOnlineStatus(profile);
    }

    public void syncOnlineStatus(@NonNull Profile profile) {
        if (profile.isOnline()) {
            if (this.onlineMap.containsKey(profile.getUniqueId()))
                return;
            this.onlineMap.put(profile.getUniqueId(), profile.getName());
        } else {
            if (!this.onlineMap.containsKey(profile.getUniqueId()))
                return;
            this.onlineMap.remove(profile.getUniqueId());
        }
    }

    public void unregisterProfile(@NonNull UUID profileUuid) {
        this.profileMap.remove(profileUuid);
        this.onlineMap.remove(profileUuid);
    }

    public void addKillToProfile(@NonNull UUID uuid) {
        this.getProfile(uuid).ifPresent(profile -> profile.getProfileStatistics().addKill());
    }

    public void resetProfileKillstreak(@NonNull UUID uuid) {
        this.getProfile(uuid).ifPresent(profile -> profile.getProfileStatistics().resetKillstreak());
    }

    public int getProfileKillstreak(@NonNull UUID uuid) {
        return this.getProfile(uuid)
                .map(profile -> profile.getProfileStatistics().getKillstreak())
                .orElse(0);
    }
}
