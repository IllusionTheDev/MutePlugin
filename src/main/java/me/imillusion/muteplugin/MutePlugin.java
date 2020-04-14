package me.imillusion.muteplugin;

import lombok.Getter;
import me.imillusion.muteplugin.command.mute.MuteCommand;
import me.imillusion.muteplugin.command.mute.MuteTabCompleter;
import me.imillusion.muteplugin.command.unmute.UnmuteCommand;
import me.imillusion.muteplugin.command.unmute.UnmuteTabCompleter;
import me.imillusion.muteplugin.listener.PlayerChatListener;
import me.imillusion.muteplugin.manager.FileManager;
import me.imillusion.muteplugin.manager.MuteManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class MutePlugin extends JavaPlugin {

    @Getter
    private MuteManager muteManager;
    @Getter
    private FileManager fileManager;

    @Override
    public void onEnable() {
        fileManager = new FileManager(this);
        muteManager = new MuteManager(this);

        getCommand("mute").setExecutor(new MuteCommand(this));
        getCommand("mute").setTabCompleter(new MuteTabCompleter(this));

        getCommand("unmute").setExecutor(new UnmuteCommand(this));
        getCommand("unmute").setTabCompleter(new UnmuteTabCompleter(this));

        Bukkit.getPluginManager().registerEvents(new PlayerChatListener(this), this);

        getLogger().info("Loaded.");
    }

    @Override
    public void onDisable() {
        muteManager.save();
    }

    public String getMessage(String name)
    {
        return ChatColor.translateAlternateColorCodes('&', fileManager.getMessages().getString(name));
    }

    public String getRawMessage(String name)
    {
        return fileManager.getMessages().getString(name);
    }
}
