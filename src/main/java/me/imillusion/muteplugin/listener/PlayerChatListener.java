package me.imillusion.muteplugin.listener;

import me.imillusion.muteplugin.MutePlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener implements Listener {

    private MutePlugin main;

    @EventHandler
    private void onChat(AsyncPlayerChatEvent e)
    {
        if(main.getMuteManager().getMutes().containsKey(e.getPlayer().getUniqueId()))
        {
            e.setCancelled(true);
            e.getPlayer().sendMessage(main.getMessage("player-muted"));
        }
    }


    public PlayerChatListener(MutePlugin main) {
        this.main = main;
    }
}
