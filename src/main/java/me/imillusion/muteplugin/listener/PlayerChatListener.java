package me.imillusion.muteplugin.listener;

import me.imillusion.muteplugin.MutePlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.time.Instant;

public class PlayerChatListener implements Listener {

    private MutePlugin main;

    @EventHandler
    private void onChat(AsyncPlayerChatEvent e)
    {
        if(main.getMuteManager().getMutes().containsKey(e.getPlayer().getUniqueId()))
        {
            long time = main.getMuteManager().getMutes().get(e.getPlayer().getUniqueId());

            if(time < Instant.now().getEpochSecond())
            {
                main.getMuteManager().getMutes().remove(e.getPlayer().getUniqueId());
                return;
            }

            e.setCancelled(true);
            e.getPlayer().sendMessage(main.getMessage("player-muted"));
        }
    }


    public PlayerChatListener(MutePlugin main) {
        this.main = main;
    }
}
