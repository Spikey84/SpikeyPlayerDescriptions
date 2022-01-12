package me.spikey.playerdescriptions;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TraitsCommand implements CommandExecutor {
    private Main main;
    private PlayerManager playerManager;

    public TraitsCommand(Main main, PlayerManager playerManager) {

        this.main = main;
        this.playerManager = playerManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This is a player only command!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            sendHelpMenu(player, "Please use /traits [trait] [option]");
            return true;
        }

        if (!main.getValidElements().contains(args[0])) {
            if (args[0].equals("help")) sendHelpMenu(player, "Please use /traits [trait] [option]");
            sendHelpMenu(player, "Invalid trait.");
            return true;
        }

        String element = args[0];

        if (args.length < 2) {
            sendOptions(player, element, "Possible values for %s.".formatted(element));
            return true;
        }

        if (args[1].equals("remove")) {
            if (playerManager.removeTrait(player, element)) {
                player.sendMessage("This trait has been cleared.");
            } else {
                player.sendMessage("This trait is already cleared.");
            }
            return true;
        }

        if (!main.isValidOption(element, args[1])) {
            sendOptions(player, element, "Possible values for %s.".formatted(element));
            return true;
        }

        String option = args[1];

        if (playerManager.addTrait(player, element, option)) {
            player.sendMessage("Trait %s has been set to: %s".formatted(element, option));
        } else {
            player.sendMessage(ChatColor.RED + "ERROR, please report this to your server owner.");
        }
        return true;
    }

    public void sendHelpMenu(Player player, String msg) {
        player.sendMessage(msg);
        StringBuilder elements = new StringBuilder();
        for (String string : main.getValidElements()) {
            elements.append(string).append(", ");
        }
        elements.replace(elements.length()-2, elements.length(), "");
        player.sendMessage("Possible Traits: %s".formatted(elements.toString()));

    }

    public void sendOptions(Player player, String element, String msg) {
        StringBuilder options = new StringBuilder();
        for (String string : main.getValidOptions(element)) {
            options.append(string).append(", ");
        }
        options.replace(options.length()-2, options.length(), "");
        player.sendMessage(msg + ": " + options);
    }
}
