package me.imillusion.muteplugin.manager;

import lombok.Getter;
import me.imillusion.muteplugin.MutePlugin;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MuteManager {

    private MutePlugin main;

    @Getter
    private Map<UUID, Instant> mutes = new HashMap<>(); //I would usually make my own mute class but this only has 2 arguments

    private Map<String, Long> multipliers = new HashMap<>();

    public void save()
    {
        FileConfiguration cfg = main.getFileManager().getMutes();
        File file = new File(main.getDataFolder(), "mutes.yml");

        for(UUID uuid : mutes.keySet())
            if(mutes.get(uuid).getEpochSecond() > Instant.now().getEpochSecond())
                cfg.set("mutes.uuid", mutes.get(uuid).getEpochSecond());

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
                mutes.put(UUID.fromString(key), Instant.ofEpochSecond(cfg.getLong("mutes." + key)));
    }

    public void mute(UUID user, Instant instant)
    {
        mutes.put(user, instant);
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

        for(String s : input.split("\\d"))
        {
            String mult = s.replaceAll("\\d", "");

            if(!multipliers.containsKey(mult))
                return 0L;

            String num = s.replace(mult, "");

            if(!StringUtils.isNumeric(num))
                return 0L;

            long time = Long.parseLong(num);

            seconds += (time * multipliers.get(mult));
        }

        if(seconds == 0)
            return 0L;

        return Instant.now().getEpochSecond() + seconds;
    }

}
