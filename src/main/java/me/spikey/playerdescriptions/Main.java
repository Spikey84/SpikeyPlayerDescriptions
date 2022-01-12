package me.spikey.playerdescriptions;

import com.google.common.collect.Maps;
import me.spikey.playerdescriptions.utils.SchedulerUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class Main extends JavaPlugin {
    private HashMap<String, List<String>> options;

    private PlayerManager playerManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        DatabaseManager.initDatabase(this);
        SchedulerUtils.setPlugin(this);
        SchedulerUtils.runDatabase((DatabaseManager::create));

        options = Maps.newHashMap();
        ConfigurationSection elements = getConfig().getConfigurationSection("elements");
        assert elements != null;
        for (String elementName : elements.getKeys(false)) {
            List<String> ops = elements.getStringList(elementName);

            options.put(elementName.toLowerCase(Locale.ROOT), ops);
        }



        playerManager = new PlayerManager(this);

        getCommand("traits").setExecutor(new TraitsCommand(this, playerManager));
        getCommand("traits").setTabCompleter(new TraitsTab(this));

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new TraitsPlaceholders(this, playerManager).register();
        }
    }


    public List<String> getValidOptions(String element) {
        if (options.get(element) == null) return null;

        return options.get(element);
    }

    public boolean isValidOption(String element, String option) {
        return options.get(element) != null && options.get(element).contains(option);
    }

    public Set<String> getValidElements() {
        if (options == null) return null;

        return options.keySet();
    }

    public boolean isValidElement(String element) {
        return options != null && options.containsKey(element);
    }

}
