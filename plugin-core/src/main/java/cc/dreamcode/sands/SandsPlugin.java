package cc.dreamcode.sands;

import cc.dreamcode.command.bukkit.BukkitCommandProvider;
import cc.dreamcode.menu.bukkit.BukkitMenuProvider;
import cc.dreamcode.menu.serializer.MenuBuilderSerializer;
import cc.dreamcode.notice.serializer.BukkitNoticeSerializer;
import cc.dreamcode.platform.DreamVersion;
import cc.dreamcode.platform.bukkit.DreamBukkitConfig;
import cc.dreamcode.platform.bukkit.DreamBukkitPlatform;
import cc.dreamcode.platform.bukkit.component.ConfigurationResolver;
import cc.dreamcode.platform.bukkit.serializer.ItemMetaSerializer;
import cc.dreamcode.platform.component.ComponentService;
import cc.dreamcode.platform.other.component.DreamCommandExtension;
import cc.dreamcode.platform.persistence.DreamPersistence;
import cc.dreamcode.platform.persistence.component.DocumentPersistenceResolver;
import cc.dreamcode.platform.persistence.component.DocumentRepositoryResolver;
import cc.dreamcode.sands.command.KillStreakCommand;
import cc.dreamcode.sands.command.SandsCommand;
import cc.dreamcode.sands.command.SetSpawnCommand;
import cc.dreamcode.sands.command.SpawnCommand;
import cc.dreamcode.sands.command.handler.InvalidInputHandlerImpl;
import cc.dreamcode.sands.command.handler.InvalidPermissionHandlerImpl;
import cc.dreamcode.sands.command.handler.InvalidSenderHandlerImpl;
import cc.dreamcode.sands.command.handler.InvalidUsageHandlerImpl;
import cc.dreamcode.sands.command.result.BukkitNoticeResolver;
import cc.dreamcode.sands.config.MessageConfig;
import cc.dreamcode.sands.config.PluginConfig;
import cc.dreamcode.sands.features.SandsController;
import cc.dreamcode.sands.features.items.StartingItemService;
import cc.dreamcode.sands.features.killstreak.RewardService;
import cc.dreamcode.sands.features.spawn.SpawnManager;
import cc.dreamcode.sands.profile.Profile;
import cc.dreamcode.sands.profile.ProfileCache;
import cc.dreamcode.sands.profile.ProfileRepository;
import cc.dreamcode.sands.profile.ProfileService;
import cc.dreamcode.utilities.adventure.AdventureProcessor;
import cc.dreamcode.utilities.adventure.AdventureUtil;
import cc.dreamcode.utilities.bukkit.StringColorUtil;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.configs.serdes.commons.serializer.InstantSerializer;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import eu.okaeri.configs.yaml.bukkit.serdes.itemstack.ItemStackFailsafe;
import eu.okaeri.configs.yaml.bukkit.serdes.serializer.ItemStackSerializer;
import eu.okaeri.persistence.document.DocumentPersistence;
import eu.okaeri.tasker.bukkit.BukkitTasker;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.Instant;

public final class SandsPlugin extends DreamBukkitPlatform implements DreamBukkitConfig, DreamPersistence {

    @Getter private static SandsPlugin instance;

    @Override
    public void load(@NonNull ComponentService componentService) {
        instance = this;

        AdventureUtil.setRgbSupport(true);
        StringColorUtil.setColorProcessor(new AdventureProcessor());
    }

    @Override
    public void enable(@NonNull ComponentService componentService) {
        componentService.setDebug(false);

        this.registerInjectable(BukkitTasker.newPool(this));
        this.registerInjectable(BukkitMenuProvider.create(this));

        this.registerInjectable(BukkitCommandProvider.create(this));
        componentService.registerExtension(DreamCommandExtension.class);


        componentService.registerResolver(ConfigurationResolver.class);
        componentService.registerComponent(MessageConfig.class);

        componentService.registerComponent(BukkitNoticeResolver.class);
        componentService.registerComponent(InvalidInputHandlerImpl.class);
        componentService.registerComponent(InvalidPermissionHandlerImpl.class);
        componentService.registerComponent(InvalidSenderHandlerImpl.class);
        componentService.registerComponent(InvalidUsageHandlerImpl.class);

        componentService.registerComponent(PluginConfig.class, pluginConfig -> {
            // register persistence + repositories
            this.registerInjectable(pluginConfig.storageConfig);

            componentService.registerResolver(DocumentPersistenceResolver.class);
            componentService.registerComponent(DocumentPersistence.class);
            componentService.registerResolver(DocumentRepositoryResolver.class);

            // enable additional logs and debug messages
            componentService.setDebug(pluginConfig.debug);
        });

        componentService.registerComponent(ProfileRepository.class);
        componentService.registerComponent(ProfileCache.class);
        componentService.registerComponent(ProfileService.class);
        componentService.registerComponent(SandsCommand.class);
        componentService.registerComponent(RewardService.class);
        componentService.registerComponent(StartingItemService.class);
        componentService.registerComponent(SandsController.class);
        componentService.registerComponent(KillStreakCommand.class);
        componentService.registerComponent(SpawnManager.class);
        componentService.registerComponent(SetSpawnCommand.class);
        componentService.registerComponent(SpawnCommand.class);
    }

    @Override
    public void disable() {
        // features need to be call when server is stopping
    }

    @Override
    public @NonNull DreamVersion getDreamVersion() {
        return DreamVersion.create("Dream-Sands", "1.0-InDEV", "Keymilo");
    }

    @Override
    public @NonNull OkaeriSerdesPack getConfigSerdesPack() {
        return registry -> {
            registry.register(new BukkitNoticeSerializer());
            registry.register(new MenuBuilderSerializer());


            registry.registerExclusive(ItemStack.class, new ItemStackSerializer(ItemStackFailsafe.BUKKIT));
            registry.registerExclusive(ItemMeta.class, new ItemMetaSerializer());
            registry.registerExclusive(Instant.class, new InstantSerializer(false));
        };
    }

    @Override
    public @NonNull OkaeriSerdesPack getPersistenceSerdesPack() {
        return registry -> {
            registry.register(new SerdesBukkit());

            registry.registerExclusive(ItemStack.class, new ItemStackSerializer(ItemStackFailsafe.BUKKIT));
            registry.registerExclusive(ItemMeta.class, new ItemMetaSerializer());
            registry.registerExclusive(Instant.class, new InstantSerializer(false));
        };
    }

}
