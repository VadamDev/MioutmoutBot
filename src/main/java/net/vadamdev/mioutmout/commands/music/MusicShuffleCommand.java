package net.vadamdev.mioutmout.commands.music;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.vadamdev.mioutmout.music.GuildMusicManager;
import net.vadamdev.mioutmout.music.PlayerManager;
import net.vadamdev.mioutmout.utils.CMDEmbedMessage;
import net.vadamdev.mioutmout.utils.Command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MusicShuffleCommand extends Command {
    public MusicShuffleCommand() {
        super("shuffle");
    }

    @Override
    public void execute(CommandEvent event, Member sender, String[] args) {
        Guild guild = event.getGuild();

        if(guild.getAudioManager().isConnected()) {
            if(sender.getVoiceState().inVoiceChannel()) {
                if(sender.getVoiceState().getChannel().equals(guild.getAudioManager().getConnectedChannel())) {
                    GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());

                    List<AudioTrack> tracks = new ArrayList<>(musicManager.scheduler.queue);
                    Collections.shuffle(tracks);

                    musicManager.scheduler.queue = new LinkedBlockingQueue<>(tracks);

                    event.reply(new CMDEmbedMessage("Mioutmout Music - Shuffle", "Toutes les musiques présente dans la queue on été mélanger.", CMDEmbedMessage.EmbedType.SUCCES).build());
                }else event.reply(new CMDEmbedMessage("Mioutmout Music - Shuffle", "Vous devez être dans le même channel que le bot pour  utiliser cette commande !", CMDEmbedMessage.EmbedType.ERROR).build());
            }else event.reply(new CMDEmbedMessage("Mioutmout Music - Shuffle", "Vous devez être dans un channel vocal pour utiliser cette commande !", CMDEmbedMessage.EmbedType.ERROR).build());
        }else event.reply(new CMDEmbedMessage("Mioutmout Music - Shuffle", "Le bot n'est pas connecté !", CMDEmbedMessage.EmbedType.ERROR).build());
    }
}
