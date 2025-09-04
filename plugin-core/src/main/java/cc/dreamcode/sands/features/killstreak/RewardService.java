package cc.dreamcode.sands.features.killstreak;

import cc.dreamcode.sands.config.PluginConfig;
import cc.dreamcode.sands.profile.Profile;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class RewardService {

    private final PluginConfig pluginConfig;

    public void giveReward(Profile killerProfile, Player killer) {
        int killstreak = killerProfile.getProfileStatistics().getKillstreak();

        pluginConfig.killStreakRewards.stream()
                .filter(reward -> reward.getKillStreak() == killstreak)
                .findFirst()
                .ifPresent(rewardWrapper -> rewardWrapper.getReward().give(killer));
    }
}