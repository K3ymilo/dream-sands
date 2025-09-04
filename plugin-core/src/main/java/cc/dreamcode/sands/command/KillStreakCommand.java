package cc.dreamcode.sands.command;

import cc.dreamcode.command.CommandBase;
import cc.dreamcode.command.annotation.Command;
import cc.dreamcode.command.annotation.Executor;
import cc.dreamcode.sands.config.MessageConfig;
import cc.dreamcode.sands.profile.ProfileService;
import cc.dreamcode.utilities.builder.MapBuilder;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Command(name = "killstreak", aliases = {"sands:killstreak", "ks", "sands:ks"})
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class KillStreakCommand implements CommandBase {

    private final ProfileService profileService;
    private final MessageConfig messageConfig;

    @Executor
    void killstreak(Player player) {
        int killstreak = this.profileService.getKillstreak(player.getUniqueId());
        this.messageConfig.killStreakMessage
                .with(new MapBuilder<String, Object>()
                        .put("killstreak", killstreak)
                        .build())
                .send(player);
    }
}