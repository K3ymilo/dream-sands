package cc.dreamcode.sands.command;

import cc.dreamcode.command.CommandBase;
import cc.dreamcode.command.annotation.Async;
import cc.dreamcode.command.annotation.Command;
import cc.dreamcode.command.annotation.Executor;
import cc.dreamcode.command.annotation.Permission;
import cc.dreamcode.notice.bukkit.BukkitNotice;
import cc.dreamcode.sands.config.MessageConfig;
import cc.dreamcode.sands.config.PluginConfig;
import cc.dreamcode.utilities.TimeUtil;
import eu.okaeri.configs.exception.OkaeriException;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;

@Command(name = "sands")
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class SandsCommand implements CommandBase {

    private final PluginConfig pluginConfig;
    private final MessageConfig messageConfig;

    @Async
    @Permission("Dream-Sands.reload")
    @Executor(path = "reload", description = "Przeladowuje konfiguracje.")
    BukkitNotice reload() {
        final long time = System.currentTimeMillis();

        try {
            this.messageConfig.load();
            this.pluginConfig.load();

            return this.messageConfig.reloaded
                    .with("time", TimeUtil.format(System.currentTimeMillis() - time));
        }
        catch (NullPointerException | OkaeriException e) {
            e.printStackTrace();

            return this.messageConfig.reloadError
                    .with("error", e.getMessage());
        }
    }
}
