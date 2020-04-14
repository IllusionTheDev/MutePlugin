package me.imillusion.muteplugin.manager;

import lombok.Getter;
import me.imillusion.muteplugin.MutePlugin;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileManager {

    private MutePlugin main;

    @Getter
    private FileConfiguration mutes;
    @Getter
    private FileConfiguration messages;

    public FileManager(MutePlugin main)
    {
        this.main = main;

        mutes = getYML(new File(main.getDataFolder(), "mutes.yml"));
        messages = getYML(new File(main.getDataFolder(), "messages.yml"));
    }

    private FileConfiguration getYML(File file) {
        FileConfiguration cfg = new YamlConfiguration();
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                main.saveResource(file.getName(), false);
            }

            cfg.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return cfg;
    }

}
