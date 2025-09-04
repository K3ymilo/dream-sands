package cc.dreamcode.sands.config;

import cc.dreamcode.platform.bukkit.component.configuration.Configuration;
import cc.dreamcode.platform.persistence.StorageConfig;
import cc.dreamcode.sands.features.killstreak.KillStreakItemReward;
import cc.dreamcode.sands.features.killstreak.KillStreakRewardWrapper;
import cc.dreamcode.sands.features.killstreak.KillStreakServiceReward;
import cc.dreamcode.utilities.bukkit.builder.ItemBuilder;
import com.cryptomorin.xseries.XMaterial;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.CustomKey;
import eu.okaeri.configs.annotation.Header;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.util.*;

import static java.util.Objects.requireNonNull;

@Configuration(child = "config.yml")
@Header("## Dream-Sands (Main-Config) ##")
public class PluginConfig extends OkaeriConfig {

    @Comment
    @Comment("Debug pokazuje dodatkowe informacje do konsoli. Lepiej wylaczyc. :P")
    @CustomKey("debug")
    public boolean debug = true;

    @Comment
    @Comment("Ponizej znajduja sie dane do logowania bazy danych:")
    @CustomKey("storage-config")
    public StorageConfig storageConfig = new StorageConfig("dreamtemplate");

    @Comment({"UUID dla konsoli, nie powinno byc zmieniane."})
    @CustomKey("console-sender-uuid")
    public UUID consoleSenderUuid = UUID.randomUUID();

    @Comment
    @Comment("Nagrody za killstreaki")
    public List<KillStreakRewardWrapper> killStreakRewards = Arrays.asList(
            KillStreakRewardWrapper.builder()
                    .killStreak(10)
                    .reward(KillStreakItemReward.builder()
                            .itemStack(
                                    ItemBuilder.of(requireNonNull(XMaterial.DIAMOND_PICKAXE.parseMaterial()))
                                            .setName("&6&lNagroda za 10 killstreak")
                                            .setLore(
                                                    "",
                                                    "&7Twoja nagroda za 10 killstreak!"
                                            )
                                            .toItemStack()
                            )
                            .build()
                    )
                    .build(),
            KillStreakRewardWrapper.builder()
                    .killStreak(20)
                    .reward(KillStreakServiceReward.builder()
                            .commands(Collections.singletonList("ffarepairall {player}"))
                            .build()
                    )
                    .build()
    );

    @Comment("Konfiguracja przedmiotow startowych.")
    @CustomKey("starting-items")
    @Getter @Setter
    public StartingItems startingItems = new StartingItems();

    @Getter
    @Setter
    public static class StartingItems extends OkaeriConfig {
        @Comment("Przedmioty, ktore gracz dostaje na poczatek (inventory).")
        public List<ItemStack> regularJoinItems = Collections.singletonList(
                new ItemStack(org.bukkit.Material.IRON_SWORD)
        );

        @Comment("Przedmioty, ktore gracz dostaje na poczatek (zalozone na sobie).")
        public Map<String, ItemStack> regularJoinArmor = Collections.singletonMap(
                "CHESTPLATE", new ItemStack(org.bukkit.Material.DIAMOND_CHESTPLATE)
        );
    }

    @Comment
    @Comment("Kordy spawnu:")
    @Comment("Uprawnienia do ustawiania lokalizacji: (sandsplugin.setspawn)")
    public Location spawnLocation = new Location(Bukkit.getWorld("world"), 0.0, 0.0, 0.0);

    @Comment
    @Comment("Czas teleportacji w sekundach na spawna.")
    @Comment("Uprawnienia do omijania cooldownu: (sandsplugin.spawn.bypass)")
    public Duration teleportTime = Duration.ofSeconds(5);

    @Comment
    @Comment("Czy title po zabiciu gracza ma być włączony?")
    public boolean killTitleMessage = true;
    @Comment("Czy broadcast po zabiciu gracza ma być włączony?")
    public boolean killChatMessage = true;

    @Comment
    @Comment("Czy message po wejściu na czacie ma być wyświetlony?")
    public boolean motdChatJoin = true;
    @Comment("Czy message po wejściu na title ma być wyświetlony?")
    public boolean motdTitleJoin = true;
}
