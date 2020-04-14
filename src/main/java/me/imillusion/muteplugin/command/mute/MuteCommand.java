package me.imillusion.muteplugin.command.mute;

import me.imillusion.muteplugin.MutePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.UUID;

public class MuteCommand implements CommandExecutor {

    private MutePlugin main;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!sender.hasPermission("mutes.mute"))
        {
            sender.sendMessage(main.getMessage("mute-no-permission"));
            return true;
        }

        if(args.length != 2 || Bukkit.getPlayerExact(args[0]) == null || main.getMuteManager().parseTotalSeconds(args[1]) == 0)
        {
            sender.sendMessage(main.getMessage("mute-invalid-format"));
            return true;
        }

        Player p = Bukkit.getPlayerExact(args[0]);
        UUID uuid = p.getUniqueId();

        if(main.getMuteManager().getMutes().containsKey(uuid))
        {
            sender.sendMessage(main.getMessage("mute-already-muted"));
            return true;
        }

        Instant instant = Instant.ofEpochSecond(main.getMuteManager().parseTotalSeconds(args[1]));

        main.getMuteManager().getMutes().put(uuid, instant);

        String broadcast = main.getRawMessage("mute-success.broadcast");
        String target = main.getRawMessage("mute-success.target");
        String player = main.getRawMessage("mute-success.player");

        broadcast = broadcast.replace("%playet%", sender.getName()).replace("%target%", p.getName());
        target = target.replace("%playet%", sender.getName()).replace("%target%", p.getName());
        player = player.replace("%playet%", sender.getName()).replace("%target%", p.getName());

        if(!broadcast.isEmpty())
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', broadcast));

        p.sendMessage(ChatColor.translateAlternateColorCodes('&', target));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', player));

        return true;
    }

    public MuteCommand(MutePlugin main) {
        this.main = main;
    }
}
