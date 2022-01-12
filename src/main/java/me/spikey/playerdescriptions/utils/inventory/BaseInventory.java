package me.spikey.playerdescriptions.utils.inventory;

import com.google.common.collect.Maps;
import me.spikey.playerdescriptions.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class BaseInventory implements InventoryHolder, Listener {
    private Inventory inventory;
    private HashMap<Integer, Consumer<ClickType>> clickableItems;

    public BaseInventory(int rows, Plugin plugin, String title) {
        clickableItems = Maps.newHashMap();
        this.inventory = Bukkit.createInventory(this, rows*9, StringUtils.formatColors(title));
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

   public void addItem(int slot, ItemStack item) {
       clickableItems.remove(slot);
       inventory.setItem(slot, item);
   }

   public void addItem(int slot, ItemStack item, Consumer<ClickType> runnable) {
        clickableItems.put(slot, runnable);
        inventory.setItem(slot, item);
   }

   public void fillInventory(ItemStack item) {
        for (int x = 0; x < inventory.getSize(); x++) {
            if (clickableItems.containsKey(x)) clickableItems.remove(x);
            inventory.setItem(x, item);
        }
   }

   public void open(Player player) {
        player.openInventory(inventory);
   }


    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        if (event.getClickedInventory() == null) return;

        if (!event.getClickedInventory().equals(inventory)) return;

        event.setCancelled(true);

        for (Map.Entry<Integer, Consumer<ClickType>> entry : clickableItems.entrySet()) {
            if (event.getSlot() != entry.getKey()) continue;
            if (event.getClick().equals(ClickType.LEFT)) entry.getValue().accept(ClickType.LEFT);
            if (event.getClick().equals(ClickType.RIGHT)) entry.getValue().accept(ClickType.RIGHT);
            if (event.getClick().equals(ClickType.SHIFT_LEFT)) entry.getValue().accept(ClickType.SHIFT_LEFT);
            if (event.getClick().equals(ClickType.SHIFT_RIGHT)) entry.getValue().accept(ClickType.SHIFT_RIGHT);
        }
    }
}
