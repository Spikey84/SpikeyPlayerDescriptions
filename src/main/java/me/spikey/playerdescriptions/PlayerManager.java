package me.spikey.playerdescriptions;

import com.google.common.collect.Maps;
import me.spikey.playerdescriptions.utils.SchedulerUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager implements Listener {
    private Main main;
    private HashMap<UUID, HashMap<String, String>> traits;

    public PlayerManager(Main main) {
        this.main = main;
        traits = Maps.newHashMap();

        Bukkit.getPluginManager().registerEvents(this, main);
    }

    public boolean addTrait(Player player, String element, String option) {
        if (!main.isValidElement(element) || !main.isValidOption(element, option)) return false;

        traits.putIfAbsent(player.getUniqueId(), Maps.newHashMap());
        traits.get(player.getUniqueId()).put(element, option);

        SchedulerUtils.runDatabaseAsync((connection -> {
            DatabaseManager.add(connection, player.getUniqueId(), element, option);
        }));
        return true;
    }

    public boolean removeTrait(Player player, String element) {
        if (!main.isValidElement(element)) return false;

        traits.putIfAbsent(player.getUniqueId(), Maps.newHashMap());
        traits.get(player.getUniqueId()).remove(element);
        return true;
    }

    public String getTrait(UUID uuid, String element) {
        if (!main.isValidElement(element)) return "";

        traits.putIfAbsent(uuid, Maps.newHashMap());
        HashMap<String, String> elementOption = traits.get(uuid);
        if (!elementOption.containsKey(element)) return "";
        return elementOption.get(element);
    }


    @EventHandler(priority = EventPriority.HIGH)
    public void preJoin(AsyncPlayerPreLoginEvent event) {
        if (!event.getLoginResult().equals(AsyncPlayerPreLoginEvent.Result.ALLOWED)) return;
        SchedulerUtils.runDatabase((connection -> {
            traits.put(event.getUniqueId(), DatabaseManager.get(connection, event.getUniqueId()));
        }));
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        traits.remove(event.getPlayer().getUniqueId());
    }
}
