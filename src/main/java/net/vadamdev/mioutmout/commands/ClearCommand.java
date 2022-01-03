package net.vadamdev.mioutmout.commands;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.vadamdev.mioutmout.utils.Command;

import java.awt.*;

public class ClearCommand extends Command {
    public ClearCommand() {
        super("clear");
    }

    @Override
    public void execute(CommandEvent event, Member sender, String[] args) {
        MessageChannel channel = event.getChannel();

        if(args.length == 1) {
            int toRemove;

            try {
                toRemove = Integer.parseInt(args[0]) + 1;

                if(toRemove - 1 == 0) {
                    event.reply(new EmbedBuilder().setTitle("Erreur : Suppresion des messages").setColor(Color.RED).setDescription("Aucun message n'ont pus être supprimer !").build());
                    return;
                }
            }catch(Exception ignored) {
                event.reply(new EmbedBuilder().setTitle("Erreur : Suppresion des messages").setColor(Color.RED).setDescription("Vous devez spécifier le nombre de message à supprimer !").build());
                return;
            }

            channel.purgeMessages(event.getChannel().getHistory().retrievePast(toRemove).complete());

            event.reply(new EmbedBuilder().setTitle("Suppresion des messages").setColor(Color.ORANGE).setDescription("J'ai supprimé " + (toRemove - 1) + " messages !").build());
        }else event.reply(new EmbedBuilder().setTitle("Erreur : Suppresion des messages").setColor(Color.RED).setDescription("Vous devez spécifier le nombre de message à supprimer !").build());

        //channel.getHistory().getRetrievedHistory().forEach(message -> message.delete().queue());
    }

    @Override
    public Permission getRequirePermission() {
        return Permission.MESSAGE_MANAGE;
    }
}
