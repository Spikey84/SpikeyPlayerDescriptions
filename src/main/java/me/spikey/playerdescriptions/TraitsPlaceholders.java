package me.spikey.playerdescriptions;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TraitsPlaceholders extends PlaceholderExpansion {
    public final Main plugin;
    private PlayerManager playerManager;
    public TraitsPlaceholders(Main plugin, PlayerManager playerManager) {

        this.plugin = plugin;
        this.playerManager = playerManager;
    }

    @Override
    public @NotNull String getAuthor() {
        return "SpikeyNoob";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "pd";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String str) {
        String[] params = str.split("\\.");
        if (params.length == 0) return ChatColor.RED + "" + ChatColor.BOLD + "INVALID PLACEHOLDER";

        for (String element : plugin.getValidElements()) {
            if (params[0].equals(element)) {
                return playerManager.getTrait(player.getUniqueId(), element);
            }
        }

        return null;
    }
}
