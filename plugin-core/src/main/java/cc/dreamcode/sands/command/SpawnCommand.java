package cc.dreamcode.sands.command;

import cc.dreamcode.command.CommandBase;
import cc.dreamcode.command.annotation.Command;
import cc.dreamcode.command.annotation.Executor;
import cc.dreamcode.sands.config.MessageConfig;
import cc.dreamcode.sands.config.PluginConfig;
import cc.dreamcode.sands.features.spawn.SpawnManager;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;


@Command(name = "spawn", aliases = {"core:spawn"})
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class SpawnCommand implements CommandBase {

    private final SpawnManager spawnManager;
    private final MessageConfig messageConfig;
    private final PluginConfig pluginConfig;



    @Executor(description = "Teleportacja na spawna")
    void spawn(Player player) {
        if (this.spawnManager.isPlayerTeleporting(player)) {
            this.messageConfig.spawnAlreadyTeleportingMessage.send(player);
            return;
        }

        final Location targetLocation = this.spawnManager.getSpawnLocation();


        if (player.hasPermission("sandsplugin.spawn.bypass")) {
            player.teleport(targetLocation);
            this.messageConfig.successMessage.send(player);
            return;
        }

        final AtomicLong time = new AtomicLong(this.pluginConfig.teleportTime.toMillis());


        this.spawnManager.addTeleport(player, Instant.now().plusMillis(time.get()).toEpochMilli());

    }
}
