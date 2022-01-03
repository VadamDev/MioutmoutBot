package net.vadamdev.mioutmout.commands;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.vadamdev.mioutmout.Main;
import net.vadamdev.mioutmout.utils.CMDEmbedMessage;
import net.vadamdev.mioutmout.utils.Command;

public class SetupCommand extends Command {
    public SetupCommand() {
        super("setup", "s");
    }

    @Override
    public void execute(CommandEvent event, Member sender, String[] args) {
        if(args.length == 0) {
            event.reply("m/setup <channelCreator / io> (ID)");
        }else if(args.length == 1) {
            if(args[0].equalsIgnoreCase("io")) {
                Main.getConfig().set("IOMessagesChannel", event.getChannel().getId());
                Main.getConfig().save();

                event.reply(new CMDEmbedMessage("Setup - IO", "Vous avez définis ce channel en mode IO.", CMDEmbedMessage.EmbedType.SUCCES).build());
            }
        }else if(args.length == 2) {
            if(args[0].equalsIgnoreCase("channelCreator")) {
                net.dv8tion.jda.api.entities.Category category = event.getGuild().getCategoryById(args[1]);

                if(category != null) {
                    event.getGuild().createVoiceChannel("\uD83D\uDD0A Créé un salon", category).setUserlimit(1).queue(voiceChannel -> {
                        Main.getConfig().set("voiceCreatorID", voiceChannel.getId());
                        Main.getConfig().save();
                    });

                    event.reply(new CMDEmbedMessage("Setup - Voice Channel Creator", "Le channel à été créé avec succes !", CMDEmbedMessage.EmbedType.SUCCES).build());
                }else event.reply(new CMDEmbedMessage("Erreur : Voice Channel Creator", "Cette catégorie n'existe pas !", CMDEmbedMessage.EmbedType.ERROR).build());
            }
        }
    }

    @Override
    public Permission getRequirePermission() {
        return Permission.MANAGE_CHANNEL;
    }
}
