package me.spikey.playerdescriptions.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class I {

    public static ItemStack setName(ItemStack i, String s) {
        ItemMeta im = i.getItemMeta();

        im.setDisplayName(s);
        ItemStack newItem = i.clone();
        newItem.setItemMeta(im);
        return newItem;
    }

    public static ItemStack getFiller() {
        ItemStack i = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
        ItemMeta itemMeta = i.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RESET + "");
        i.setItemMeta(itemMeta);
        return i;
    }

    public static ItemStack getVisibleFiller() {
        ItemStack i = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta itemMeta = i.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RESET + "");
        i.setItemMeta(itemMeta);
        return i;
    }
}
