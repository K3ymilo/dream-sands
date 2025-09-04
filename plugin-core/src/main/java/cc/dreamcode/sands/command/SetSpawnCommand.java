package cc.dreamcode.sands.command;

import cc.dreamcode.command.CommandBase;
import cc.dreamcode.command.annotation.Command;
import cc.dreamcode.command.annotation.Executor;
import cc.dreamcode.command.annotation.Permission;
import cc.dreamcode.sands.config.MessageConfig;
import cc.dreamcode.sands.features.spawn.SpawnManager;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Command(name = "setspawn", aliases = {"sands:setspawn", "ustawspawn", "sands:ustawpsawn"})
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class SetSpawnCommand implements CommandBase {

    private final MessageConfig messageConfig;
    private final SpawnManager spawnManager;

    @Permission("sandsplugin.setspawn")
    @Executor(path = "global", description = "Ustawia spawn dla globalnego swiata.")
    void setGlobalSpawn(Player player) {
        spawnManager.setSpawnLocation(player.getLocation());
        messageConfig.setSpawnLocation.send(player);
    }

}
