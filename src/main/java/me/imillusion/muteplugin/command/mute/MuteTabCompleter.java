package me.imillusion.muteplugin.command.mute;

import me.imillusion.muteplugin.MutePlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MuteTabCompleter implements TabCompleter {

    private MutePlugin main;

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] strings) {
        if(!sender.hasPermission("mutes.mute"))
            return null;

        List<String> list = new ArrayList<>();

        for(Player p : Bukkit.getOnlinePlayers())
            if(!main.getMuteManager().getMutes().containsKey(p.getUniqueId()))
                list.add(p.getName());

        return list;
    }

    public MuteTabCompleter(MutePlugin main) {
        this.main = main;
    }
}
