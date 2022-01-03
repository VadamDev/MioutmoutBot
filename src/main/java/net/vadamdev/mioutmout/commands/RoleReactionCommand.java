package net.vadamdev.mioutmout.commands;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.vadamdev.mioutmout.rolereaction.RoleReaction;
import net.vadamdev.mioutmout.rolereaction.RoleReactionBuilder;
import net.vadamdev.mioutmout.rolereaction.RoleReactionManager;
import net.vadamdev.mioutmout.utils.CMDEmbedMessage;
import net.vadamdev.mioutmout.utils.Command;

import java.util.List;

public class RoleReactionCommand extends Command {
    public static TextChannel builderChannel;
    public static RoleReactionBuilder roleReactionBuilder;
    private boolean isUpdater;

    public RoleReactionCommand() {
        super("rolereaction");
    }

    @Override
    public void execute(CommandEvent event, Member sender, String[] args) {
        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("builder")) {
                if(builderChannel != null) {
                    event.reply(new CMDEmbedMessage("Erreur : RoleReaction Builder", "Un RoleReaction Builder est déjà en cours !", CMDEmbedMessage.EmbedType.WARNING).build());
                    return;
                }

                sender.getGuild().createTextChannel("RoleReaction Builder").queue(channel -> {
                    channel.createPermissionOverride(event.getGuild().getRoleById(event.getGuild().getId())).setDeny(Permission.VIEW_CHANNEL).queue();
                    channel.createPermissionOverride(sender).setAllow(Permission.VIEW_CHANNEL).queue();

                    channel.sendMessageEmbeds(new CMDEmbedMessage("RoleReaction - Builder", sender.getAsMention() + ", pour créé un nouveau RoleReaction, vous devez mentionner le role choisis puis réagir sur se même message avec un emote.", CMDEmbedMessage.EmbedType.SUCCES).build()).queue();
                    channel.sendMessage(sender.getAsMention()).queue(msg -> msg.delete().queue());
                    builderChannel = channel;
                });

                roleReactionBuilder = new RoleReactionBuilder();
            }
        }else if(args.length == 2) {
            if(args[0].equalsIgnoreCase("finish")) {
                if(builderChannel == null) {
                    event.reply(new CMDEmbedMessage("Erreur : RoleReaction Builder", "Il n'y a pas de RoleReaction Builder en cours !", CMDEmbedMessage.EmbedType.WARNING).build());
                    return;
                }

                List<TextChannel> mentionnedChannels = event.getMessage().getMentionedChannels();

                if(!mentionnedChannels.isEmpty()) {
                    roleReactionBuilder.setChannel(mentionnedChannels.get(0));

                    RoleReaction roleReaction = roleReactionBuilder.build();
                    if(!isUpdater) roleReaction.sendRoleReactionMessage();
                    else roleReaction.updateRoleReactionMessage();
                    RoleReactionManager.roleReactions.add(roleReaction);

                    builderChannel.delete().queue();

                    isUpdater = false;
                    builderChannel = null;
                    roleReactionBuilder = null;
                }else event.reply(new CMDEmbedMessage("Erreur : RoleReaction Builder", "Vous devez spécifier le channel dans lequel le RoleReaction sera !", CMDEmbedMessage.EmbedType.WARNING).build());

                return;
            }else if(args[0].equalsIgnoreCase("update")) {
                if(builderChannel != null) {
                    event.reply(new CMDEmbedMessage("Erreur : RoleReaction Builder", "Un RoleReaction Builder est déjà en cours !", CMDEmbedMessage.EmbedType.WARNING).build());
                    return;
                }

                RoleReaction roleReaction = RoleReactionManager.getRoleReactionById(args[1]);

                if(roleReaction != null) {
                    sender.getGuild().createTextChannel("RoleReaction Updater").queue(channel -> {
                        channel.createPermissionOverride(event.getGuild().getRoleById(event.getGuild().getId())).setDeny(Permission.VIEW_CHANNEL).queue();
                        channel.createPermissionOverride(sender).setAllow(Permission.VIEW_CHANNEL).queue();

                        channel.sendMessageEmbeds(new CMDEmbedMessage("RoleReaction - Editor", sender.getAsMention() + ", pour modifier un RoleReaction, vous devez mentionner le role choisis puis réagir sur se même message avec un emote.", CMDEmbedMessage.EmbedType.SUCCES).build()).queue();
                        channel.sendMessage(sender.getAsMention()).queue(msg -> msg.delete().queue());
                        builderChannel = channel;
                    });

                    isUpdater = true;
                    roleReactionBuilder = roleReaction.builder();
                }else event.reply(new CMDEmbedMessage("Erreur : RoleReaction Update", "Aucun RoleReaction n'a pus être trouvé !", CMDEmbedMessage.EmbedType.ERROR).build());
            }
        }

        if(args.length >= 2) {
            if(args[0].equalsIgnoreCase("title")) {
                if(builderChannel == null) {
                    event.reply(new CMDEmbedMessage("Erreur : RoleReaction Builder", "Il n'y a pas de RoleReaction Builder en cours !", CMDEmbedMessage.EmbedType.WARNING).build());
                    return;
                }

                StringBuilder str = new StringBuilder();
                for(int i = 1; i < args.length; i++) str.append(args[i] + " ");

                String title = str.toString();

                roleReactionBuilder.setEmbedTitle(title);

                event.reply(new CMDEmbedMessage("RoleReaction Builder", "Vous avez nommé votre RoleReaction en " + title, CMDEmbedMessage.EmbedType.SUCCES).build());
            }else if(args[0].equalsIgnoreCase("header")) {
                if(builderChannel == null) {
                    event.reply(new CMDEmbedMessage("Erreur : RoleReaction Builder", "Il n'y a pas de RoleReaction Builder en cours !", CMDEmbedMessage.EmbedType.WARNING).build());
                    return;
                }

                StringBuilder str = new StringBuilder();
                for(int i = 1; i < args.length; i++) str.append(args[i] + " ");

                String header = str.toString();

                roleReactionBuilder.setEmbedHeader(header);

                event.reply(new CMDEmbedMessage("RoleReaction Builder", "Nouveau header du rolereaction : " + header, CMDEmbedMessage.EmbedType.SUCCES).build());
            }
        }
    }

    @Override
    public Permission getRequirePermission() {
        return Permission.MESSAGE_MANAGE;
    }
}
