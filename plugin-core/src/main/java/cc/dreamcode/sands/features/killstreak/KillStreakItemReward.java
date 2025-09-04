package cc.dreamcode.sands.features.killstreak;

import cc.dreamcode.utilities.bukkit.InventoryUtil;
import cc.dreamcode.utilities.bukkit.builder.ItemBuilder;
import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.SerializationData;
import eu.okaeri.configs.serdes.serializable.ConfigSerializable;
import lombok.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class KillStreakItemReward implements ConfigSerializable, IKillStreakReward {

    private ItemStack itemStack;

    @SuppressWarnings("unused")
    public static KillStreakItemReward deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {
        return new KillStreakItemReward(data.get("itemStack", ItemStack.class));
    }

    @Override
    public void give(final Player player) {
        InventoryUtil.giveItem(player, ItemBuilder.of(this.itemStack)
                .fixColors()
                .toItemStack());
    }

    @Override
    public void serialize(@NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        data.add("itemStack", this.itemStack);
    }

}