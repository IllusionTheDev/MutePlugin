package me.imillusion.muteplugin.command.unmute;

import me.imillusion.muteplugin.MutePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.UUID;

public class UnmuteCommand implements CommandExecutor {

    private MutePlugin main;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!sender.hasPermission("mutes.unmute"))
        {
            sender.sendMessage(main.getMessage("unmute-no-permission"));
            return true;
        }

        if(args.length != 1 || Bukkit.getPlayerExact(args[0]) == null)
        {
            sender.sendMessage(main.getMessage("unmute-invalid-format"));
            return true;
        }

        Player p = Bukkit.getPlayerExact(args[0]);
        UUID uuid = p.getUniqueId();

        if(!main.getMuteManager().getMutes().containsKey(uuid))
        {
            sender.sendMessage(main.getMessage("unmute-player-not-muted"));
            return true;
        }

        main.getMuteManager().getMutes().remove(uuid);

        String broadcast = main.getRawMessage("unmute-success.broadcast");
        String target = main.getRawMessage("unmute-success.target");
        String player = main.getRawMessage("unmute-success.player");

        broadcast = broadcast.replace("%playet%", sender.getName()).replace("%target%", p.getName());
        target = target.replace("%playet%", sender.getName()).replace("%target%", p.getName());
        player = player.replace("%playet%", sender.getName()).replace("%target%", p.getName());

        if(!broadcast.isEmpty())
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', broadcast));

        p.sendMessage(ChatColor.translateAlternateColorCodes('&', target));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', player));

        return true;
    }

    public UnmuteCommand(MutePlugin main) {
        this.main = main;
    }
}
