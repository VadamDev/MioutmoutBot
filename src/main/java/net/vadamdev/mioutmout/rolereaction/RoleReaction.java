package net.vadamdev.mioutmout.rolereaction;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.interactions.components.Button;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class RoleReaction {
    private TextChannel channel;
    private Map<Emote, Role> reactionMap;
    private ReactionType reactionType;
    private String embedTitle, embedHeader, messageID;

    private Message roleReactionMessage;

    public RoleReaction(TextChannel channel, Map<Emote, Role> reactionMap, String embedTitle, String embedHeader) {
        this.channel = channel;
        this.reactionMap = reactionMap;
        this.reactionType = ReactionType.BUTTON;
        this.embedTitle = embedTitle;
        this.embedHeader = embedHeader;
    }

    public void sendRoleReactionMessage() {
        MessageEmbed embed = getEmbedMessage();

        if(reactionType.equals(ReactionType.EMOTE)) {
            channel.sendMessageEmbeds(embed).queue(message -> {
                reactionMap.keySet().forEach(emote -> message.addReaction(emote).queue());
                roleReactionMessage = message;
            });
        }else if(reactionType.equals(ReactionType.BUTTON)) {
            Collection<Button> buttons = new ArrayList<>();

            reactionMap.forEach((emote, role) -> buttons.add(Button.primary(role.getName().toLowerCase(), Emoji.fromEmote(emote))));

            channel.sendMessageEmbeds(embed).setActionRow(buttons).queue(message -> {
                messageID = message.getId();
                roleReactionMessage = message;
            });
        }

        save();
    }

    public void updateRoleReactionMessage() {
        MessageEmbed embed = getEmbedMessage();

        if(reactionType.equals(ReactionType.EMOTE)) {
            roleReactionMessage.editMessageEmbeds(embed).queue(message -> {
                reactionMap.keySet().forEach(emote -> message.addReaction(emote).queue());
                roleReactionMessage = message;
            });
        }else if(reactionType.equals(ReactionType.BUTTON)) {
            Collection<Button> buttons = new ArrayList<>();

            reactionMap.forEach((emote, role) -> buttons.add(Button.primary(role.getName().toLowerCase(), Emoji.fromEmote(emote))));

            roleReactionMessage.editMessageEmbeds(embed).setActionRow(buttons).queue(message -> {
                messageID = message.getId();
                roleReactionMessage = message;
            });
        }

        save();
    }

    public void addRole(MessageReactionAddEvent event) {
        event.getChannel().retrieveMessageById(event.getMessageId()).queue(message -> {
            if(message.getId().equals(roleReactionMessage.getId())) {
                Emote reactionEmote = event.getReactionEmote().getEmote();

                if(reactionMap.containsKey(reactionEmote)) {
                    event.getGuild().addRoleToMember(event.getMember(), reactionMap.get(reactionEmote)).queue();
                }
            }
        });
    }

    public void removeRole(MessageReactionRemoveEvent event) {
        event.getChannel().retrieveMessageById(event.getMessageId()).queue(message -> {
            if(message.getId().equals(roleReactionMessage.getId())) {
                Emote reactionEmote = event.getReactionEmote().getEmote();

                if(reactionMap.containsKey(reactionEmote)) {
                    event.getGuild().removeRoleFromMember(event.getMember(), reactionMap.get(reactionEmote)).queue();
                }
            }
        });
    }

    public void save() {

    }

    public Map<Emote, Role> getReactionMap() {
        return reactionMap;
    }

    public String getMessageID() {
        return messageID;
    }

    public RoleReactionBuilder builder() {
        return new RoleReactionBuilder().setReactionMap(reactionMap).setEmbedTitle(embedTitle).setChannel(channel);
    }

    private MessageEmbed getEmbedMessage() {
        StringBuilder str = new StringBuilder();

        if(embedHeader != null) str.append(embedHeader + "\n\n");
        reactionMap.forEach((emote, role) -> str.append(emote.getAsMention() + " : " + role.getName() + "\n"));

        return new EmbedBuilder().setTitle(embedTitle).setDescription(str.toString()).build();
    }

    public String toJson() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }

    public static RoleReaction read(String json) {
        return new Gson().fromJson(json, RoleReaction.class);
    }

    public enum ReactionType {
        EMOTE, BUTTON
    }
}
