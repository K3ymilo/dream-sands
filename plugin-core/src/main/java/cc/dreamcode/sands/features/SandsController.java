package cc.dreamcode.sands.features;

import cc.dreamcode.sands.features.items.StartingItemService;
import cc.dreamcode.sands.features.killstreak.RewardService;
import cc.dreamcode.sands.profile.Profile;
import cc.dreamcode.sands.profile.ProfileService;
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
    private final RewardService rewardService;
    private final StartingItemService startingItemService;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        event.setJoinMessage(null);

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
        Player killer = victim.getKiller();

        this.profileService.modifyProfile(victim.getUniqueId(), profile -> {
            profile.getProfileStatistics().resetKillstreak();
        });

        if (killer != null) {
            this.profileService.modifyProfile(killer.getUniqueId(), killerProfile -> {
                killerProfile.getProfileStatistics().addKill();
                this.rewardService.giveReward(killerProfile, killer);
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
