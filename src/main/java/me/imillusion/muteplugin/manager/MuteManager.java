package me.imillusion.muteplugin.manager;

import lombok.Getter;
import me.imillusion.muteplugin.MutePlugin;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MuteManager {

    private MutePlugin main;

    //----- REGEX -----

    private Pattern format = Pattern.compile("(\\d+[a-zA-Z]?)");

    @Getter
    private Map<UUID, Long> mutes = new HashMap<>(); //I would usually make my own mute class but this only has 2 arguments

    private Map<String, Long> multipliers = new HashMap<>();

    public void save()
    {
        FileConfiguration cfg = main.getFileManager().getMutes();
        File file = new File(main.getDataFolder(), "mutes.yml");

        for(UUID uuid : mutes.keySet())
            if(mutes.get(uuid) > Instant.now().getEpochSecond())
                cfg.set("mutes.uuid", mutes.get(uuid));

        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void load()
    {
        FileConfiguration cfg = main.getFileManager().getMutes();

        if(cfg.contains("mutes"))
            for(String key : cfg.getConfigurationSection("mutes").getKeys(false))
                mutes.put(UUID.fromString(key), cfg.getLong("mutes." + key));
    }

    public void mute(UUID user, long time)
    {
        mutes.put(user, time);
    }

    public MuteManager(MutePlugin main) {
        this.main = main;

        load();

        multipliers.put("s", 1L);
        multipliers.put("m", 60L);
        multipliers.put("h", 3600L);
        multipliers.put("d", 86400L);
        multipliers.put("w", 604800L);
    }

    public long parseTotalSeconds(String input)
    {
        long seconds = 0;

        for(int i = 1; i <= input.length() / 2; i++)
        {
            String group = input.substring((i-1) * 2, i * 2);

            String num = String.valueOf(group.charAt(0));
            String mult = String.valueOf(group.charAt(1));

            if(!num.matches("\\d+") || !multipliers.containsKey(mult))
                return 0L;

            seconds += (Long.valueOf(num) * multipliers.get(mult));

        }

        return Instant.now().getEpochSecond() + seconds;
    }

}
