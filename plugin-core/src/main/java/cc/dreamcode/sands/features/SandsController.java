package cc.dreamcode.sands.features;

import cc.dreamcode.sands.config.MessageConfig;
import cc.dreamcode.sands.config.PluginConfig;
import cc.dreamcode.sands.features.items.StartingItemService;
import cc.dreamcode.sands.profile.Profile;
import cc.dreamcode.sands.profile.ProfileService;
import cc.dreamcode.utilities.builder.MapBuilder;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.tasker.core.Tasker;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;


@RequiredArgsConstructor(onConstructor_ = @Inject)
public class SandsController implements Listener {

    private final ProfileService profileService;
    private final Tasker tasker;
    private final StartingItemService startingItemService;
    private final PluginConfig pluginConfig;
    private final MessageConfig messageConfig;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        event.setJoinMessage(null);
        if (this.pluginConfig.motdChatJoin) {
            this.messageConfig.motdJoinChat.send(player);
        }

        if (this.pluginConfig.motdTitleJoin) {
            this.messageConfig.motdJoinTitle.send(player);
        }

        if (player.isDead())
            player.spigot().respawn();

        Profile profile = this.profileService.getProfileOrCreate(player);

        this.startingItemService.giveStartingItems(player);

        if (profile.isDead()) {
            player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
            player.setFoodLevel(20);
            player.setHealth(20.0D);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

        event.setDeathMessage(null);

        Player victim = event.getEntity();
        Player attacker = victim.getKiller();

        this.profileService.modifyProfile(victim.getUniqueId(), profile -> {
            profile.getProfileStatistics().resetKillstreak();
        });

        if (attacker != null) {
            this.profileService.modifyProfile(attacker.getUniqueId(), killerProfile -> {
                killerProfile.getProfileStatistics().addKill();

                if (this.pluginConfig.killChatMessage) {
                    this.messageConfig.killChatMessage.with(new MapBuilder<String, Object>()
                                    .put("victim", victim.getName())
                                    .put("attacker", attacker.getName())
                                    .build())
                            .sendAll();
                }

                if (this.pluginConfig.killTitleMessage) {
                    this.messageConfig.playerKillTitleKiller.send(attacker);
                    this.messageConfig.playerKillTitleKilled.send(victim);
                }
            });
        }

        this.tasker.newSharedChain(victim.getUniqueId().toString())
                .sync(() -> {
                    victim.spigot().respawn();
                })
                .execute();
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {

        Player player = event.getPlayer();

        this.startingItemService.giveStartingItems(player);
    }
}
