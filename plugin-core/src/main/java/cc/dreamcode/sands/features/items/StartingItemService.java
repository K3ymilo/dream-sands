package cc.dreamcode.sands.features.items;

import cc.dreamcode.sands.config.PluginConfig;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class StartingItemService {

    private final PluginConfig pluginConfig;

    public void giveStartingItems(Player player) {
        player.getInventory().clear();
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);

        giveItemsToInventory(player, pluginConfig.getStartingItems().getRegularJoinItems());
        equipArmor(player, pluginConfig.getStartingItems().getRegularJoinArmor());
    }

    private void giveItemsToInventory(Player player, List<ItemStack> items) {
        PlayerInventory inventory = player.getInventory();
        items.forEach(item -> {
            if (inventory.firstEmpty() != -1) {
                inventory.addItem(item);
            } else {
                player.getWorld().dropItem(player.getLocation(), item);
            }
        });
    }

    private void equipArmor(Player player, Map<String, ItemStack> armor) {
        PlayerInventory inventory = player.getInventory();
        armor.forEach((slot, item) -> {
            switch (slot.toUpperCase()) {
                case "HELMET":
                    inventory.setHelmet(item);
                    break;
                case "CHESTPLATE":
                    inventory.setChestplate(item);
                    break;
                case "LEGGINGS":
                    inventory.setLeggings(item);
                    break;
                case "BOOTS":
                    inventory.setBoots(item);
                    break;
                default:
                    player.sendMessage("Nieprawidlowa nazwa slotu zbroi: " + slot);
                    break;
            }
        });
    }
}