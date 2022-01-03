package net.vadamdev.mioutmout.utils;

import net.dv8tion.jda.api.entities.Guild;

public class Utils {
    public static String getEmote(Guild guild, String name) {
        return guild.getEmotesByName(name, false).get(0).getAsMention();
    }
}
