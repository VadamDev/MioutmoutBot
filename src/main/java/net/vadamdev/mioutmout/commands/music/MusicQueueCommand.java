package net.vadamdev.mioutmout.commands.music;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.vadamdev.mioutmout.music.PlayerManager;
import net.vadamdev.mioutmout.music.TrackScheduler;
import net.vadamdev.mioutmout.utils.CMDEmbedMessage;
import net.vadamdev.mioutmout.utils.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class MusicQueueCommand extends Command {
    public MusicQueueCommand() {
        super("queue");
    }

    @Override
    public void execute(CommandEvent event, Member sender, String[] args) {
        Guild guild = event.getGuild();

        if(guild.getAudioManager().isConnected()) {
            if(sender.getVoiceState().inVoiceChannel()) {
                if(sender.getVoiceState().getChannel().equals(guild.getAudioManager().getConnectedChannel())) {
                    List<AudioTrack> tracks = new ArrayList<>(PlayerManager.getInstance().getMusicManager(event.getGuild()).scheduler.queue);

                    if(tracks.isEmpty()) {
                        event.reply(new CMDEmbedMessage("Mioutmout Music - Queue", "Il n'y a aucune musique dans la file d'attente.", CMDEmbedMessage.EmbedType.ERROR).build());
                        return;
                    }

                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Les musiques actuellement en attente sont:\n");

                    for(int i = 0; i < tracks.size() && i < 10; i++)
                        stringBuilder.append("\n **-** " + tracks.get(i).getInfo().title);

                    if(tracks.size() >= 10) stringBuilder.append("\n **-** ... (+" + (tracks.size() - 10) + ")");

                    event.reply(new CMDEmbedMessage("Mioutmout Music - Queue", stringBuilder.toString(), CMDEmbedMessage.EmbedType.SUCCES).build());
                }else event.reply(new CMDEmbedMessage("Mioutmout Music - Queue", "Vous devez être dans le même channel que le bot pour  utiliser cette commande !", CMDEmbedMessage.EmbedType.ERROR).build());
            }else event.reply(new CMDEmbedMessage("Mioutmout Music - Queue", "Vous devez être dans un channel vocal pour utiliser cette commande !", CMDEmbedMessage.EmbedType.ERROR).build());
        }else event.reply(new CMDEmbedMessage("Mioutmout Music - Queue", "Le bot n'est pas connecté !", CMDEmbedMessage.EmbedType.ERROR).build());
    }
}
