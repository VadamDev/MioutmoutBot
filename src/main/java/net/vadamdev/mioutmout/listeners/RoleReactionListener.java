package net.vadamdev.mioutmout.listeners;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.vadamdev.mioutmout.commands.RoleReactionCommand;
import net.vadamdev.mioutmout.rolereaction.RoleReactionManager;
import org.jetbrains.annotations.NotNull;

public class RoleReactionListener extends ListenerAdapter {
    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        if(event.getMember().getUser().isBot()) return;

        RoleReactionManager.roleReactions.forEach(roleReaction -> roleReaction.addRole(event));

        //Rolereaction creator

        if(RoleReactionCommand.roleReactionBuilder != null && event.getChannel().equals(RoleReactionCommand.builderChannel)) {
            event.getChannel().retrieveMessageById(event.getMessageId()).queue(message -> {
                if(!message.getMentionedRoles().isEmpty()) {
                    Role role = message.getMentionedRoles().get(0);

                    if(!RoleReactionCommand.roleReactionBuilder.contains(role)) {
                        RoleReactionCommand.roleReactionBuilder.put(event.getReactionEmote().getEmote(), role);
                        event.getChannel().sendMessage(event.getReactionEmote().getEmote().getAsMention() + " a été mis pour le role " + role.getAsMention()).queue();
                    }else {
                        RoleReactionCommand.roleReactionBuilder.remove(event.getReactionEmote().getEmote());
                        event.getChannel().sendMessage(role.getAsMention() + " a été supprimé de la liste !").queue();
                    }
                }
            });
        }
    }

    @Override
    public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
        if(event.getMember().getUser().isBot()) return;

        RoleReactionManager.roleReactions.forEach(roleReaction -> roleReaction.removeRole(event));
    }

    @Override
    public void onButtonClick(ButtonClickEvent event) {
        RoleReactionManager.roleReactions.forEach(roleReaction -> {
            roleReaction.getReactionMap().values().stream().filter(role -> role.getName().toLowerCase().equals(event.getComponentId())).findFirst().ifPresent(role -> event.getGuild().addRoleToMember(event.getMember(), role).queue());
        });
    }
}
