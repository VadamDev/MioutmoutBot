package net.vadamdev.mioutmout.rolereaction;

import net.vadamdev.mioutmout.utils.config.FileUtils;
import net.vadamdev.mioutmout.utils.config.json.JSONReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RoleReactionManager {
    public static List<RoleReaction> roleReactions = new ArrayList<>();

    public void onStart() {
        try {
            for(File file : Objects.requireNonNull(FileUtils.getRoleReactionDataFolder().listFiles())) {
                roleReactions.add(RoleReaction.read(new JSONReader(file).toJSONObject().toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public void addAutoResponder(RoleReaction roleReaction) {
        roleReactions.add(roleReaction);

        try {
            Config data = new Config(FileUtils.getRoleReactionDataFolder(), roleReaction.getName() + ".json");
            data.setJsonObject(new JSONObject(roleReaction.toJson()));
            data.save();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeAutoResponder(RoleReaction roleReaction) {
        roleReactions.remove(roleReaction);

        File file = new File(FileUtils.getRoleReactionDataFolder(), roleReaction.getName() + ".json");
        if(file.exists()) file.delete();
    }*/

    public static RoleReaction getRoleReactionById(String id) {
        return roleReactions.stream().filter(roleReaction -> roleReaction.getMessageID().equals(id)).findFirst().orElse(null);
    }
}
