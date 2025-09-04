package cc.dreamcode.sands.features.spawn;

import cc.dreamcode.sands.config.PluginConfig;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.tasker.core.Tasker;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class SpawnManager {

    private final PluginConfig pluginConfig;
    private final Tasker tasker;

    private final Map<UUID, Long> teleport = new HashMap<>();

    public void addTeleport(@NonNull Player player, long time) {
        this.teleport.put(player.getUniqueId(), time);
    }

    public void removeTeleport(@NonNull Player player) {
        this.teleport.remove(player.getUniqueId());
    }

    public Optional<Long> getTeleport(@NonNull Player player) {
        return Optional.ofNullable(this.teleport.get(player.getUniqueId()));
    }

    public void setSpawnLocation(@NonNull Location location) {
        this.tasker.newSharedChain("config-update")
                .async(() -> {
                    this.pluginConfig.spawnLocation = location;
                    this.pluginConfig.save();
                })
                .execute();
    }

    public boolean isPlayerTeleporting(@NonNull Player player) {
        return this.getTeleport(player)
                .map(teleport -> teleport > System.currentTimeMillis())
                .orElse(false);
    }

    public Location getSpawnLocation() {
        return this.pluginConfig.spawnLocation;
    }
}