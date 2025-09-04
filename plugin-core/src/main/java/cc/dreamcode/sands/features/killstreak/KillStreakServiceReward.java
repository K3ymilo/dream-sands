package cc.dreamcode.sands.features.killstreak;


import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.SerializationData;
import eu.okaeri.configs.serdes.serializable.ConfigSerializable;
import lombok.*;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class KillStreakServiceReward implements ConfigSerializable, IKillStreakReward {

    private List<String> commands;

    @SuppressWarnings("unused")
    public static KillStreakServiceReward deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {
        return new KillStreakServiceReward(data.getAsList("commands", String.class));
    }

    @Override
    public void give(final Player player) {
        ConsoleCommandSender sender = Bukkit.getConsoleSender();
        this.commands.forEach(command -> Bukkit.dispatchCommand(sender, command.replace("{player}", player.getName())));
    }

    @Override
    public void serialize(@NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        data.addCollection("commands", this.commands, String.class);
    }
}