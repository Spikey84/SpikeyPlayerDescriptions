package me.spikey.playerdescriptions;

import com.google.common.collect.Lists;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TraitsTab implements TabCompleter {
    private Main main;
    public TraitsTab(Main main) {

        this.main = main;
    }
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> tab = Lists.newArrayList();
        if (args.length == 1) {
            tab = main.getValidElements().stream().toList();
        } else if (args.length == 2 && main.isValidElement(args[0])){
            tab.addAll(main.getValidOptions(args[0]));
            tab.add("remove");
        }
        return tab;
    }
}
