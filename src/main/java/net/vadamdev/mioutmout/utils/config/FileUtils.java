package net.vadamdev.mioutmout.utils.config;

import java.io.File;
import java.io.IOException;

public enum FileUtils {
    DATA("data");

    private Config config;

    FileUtils(String name) {
        this(getDataFolder(), name);
    }

    FileUtils(File dataFolder, String name) {
        try {
            this.config = new Config(dataFolder,name + ".json");
        }catch (IOException e) { e.printStackTrace(); }

        save();
    }

    public Config getConfig() {
        return config;
    }

    public void save() {
        config.save();
    }

    public static File getDataFolder() {
        File file = new File("MioutmoutBot");

        if(!file.exists()) file.mkdirs();
        return file;
    }

    public static File getRoleReactionDataFolder() {
        File file = new File("MioutmoutBot/roleReactions");

        if(!file.exists()) file.mkdirs();
        return file;
    }
}
