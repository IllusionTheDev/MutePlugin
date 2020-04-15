package me.imillusion.muteplugin.command.unmute;

import me.imillusion.muteplugin.MutePlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class UnmuteTabCompleter implements TabCompleter {

    private MutePlugin main;

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] strings) {
        if(!sender.hasPermission("mutes.unmute"))
            return null;

        List<String> players = new ArrayList<>();

        if(strings.length == 1)
            for(Player p : Bukkit.getOnlinePlayers())
                if(main.getMuteManager().getMutes().containsKey(p.getUniqueId()))
                    players.add(p.getName());

        return players;
    }

    public UnmuteTabCompleter(MutePlugin main) {
        this.main = main;
    }
}
