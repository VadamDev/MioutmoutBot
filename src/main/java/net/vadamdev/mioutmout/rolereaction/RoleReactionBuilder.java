package net.vadamdev.mioutmout.rolereaction;

import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.Map;

public class RoleReactionBuilder {
    private TextChannel channel;
    private Map<Emote, Role> reactionMap;
    private RoleReaction.ReactionType reactionType;
    private String embedTitle, embedHeader;

    private Message roleReactionMessage;

    public RoleReactionBuilder() {
        this.reactionMap = new HashMap<>();
        this.embedTitle = "Exemple";
    }

    public RoleReactionBuilder put(Emote emote, Role role) {
        reactionMap.put(emote, role);
        return this;
    }

    public RoleReactionBuilder remove(Emote emote) {
        reactionMap.remove(emote);
        return this;
    }

    public RoleReactionBuilder setReactionMap(Map<Emote, Role> reactionMap) {
        this.reactionMap = reactionMap;
        return this;
    }

    public RoleReactionBuilder setEmbedTitle(String embedTitle) {
        this.embedTitle = embedTitle;
        return this;
    }

    public RoleReactionBuilder setEmbedHeader(String embedHeader) {
        this.embedHeader = embedHeader;
        return this;
    }

    public RoleReactionBuilder setChannel(TextChannel channel) {
        this.channel = channel;
        return this;
    }

    public void setReactionType(RoleReaction.ReactionType reactionType) {
        this.reactionType = reactionType;
    }

    public void setRoleReactionMessage(Message roleReactionMessage) {
        this.roleReactionMessage = roleReactionMessage;
    }

    public boolean contains(Role role) {
        return reactionMap.containsValue(role);
    }

    public RoleReaction build() {
        return new RoleReaction(channel, reactionMap, embedTitle, embedHeader);
    }
}
