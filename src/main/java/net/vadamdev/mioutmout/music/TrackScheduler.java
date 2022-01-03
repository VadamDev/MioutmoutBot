package net.vadamdev.mioutmout.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.managers.AudioManager;
import net.vadamdev.mioutmout.commands.music.MusicInterfaceCommand;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {
    private final Guild guild;
    public final AudioPlayer player;
    public BlockingQueue<AudioTrack> queue;
    public boolean repeating = false;

    public TrackScheduler(Guild guild, AudioPlayer player) {
        this.guild = guild;
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
    }

    public void queue(AudioTrack track) {
        if(!player.startTrack(track, true)) queue.offer(track);
    }

    public void nextTrack() {
        if(queue.isEmpty()) {
            GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(guild);

            if(musicManager.interfaceMessage != null) {
                musicManager.interfaceMessage.editMessageEmbeds(new EmbedBuilder().setColor(Color.red).setTitle("Mioutmout Music - Interface").setDescription("Ce panel n'est plus relié à Mioutmout Music.\nVeuillez en créé un nouveau avec la commande m/interface").build()).queue();
                musicManager.interfaceMessage = null;
            }

            AudioManager audioManager = guild.getAudioManager();
            if(audioManager.getConnectedChannel() != null) audioManager.closeAudioConnection();
        }else player.startTrack(queue.poll(), false);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if(endReason.mayStartNext) {
            if(this.repeating) {
                this.player.startTrack(track.makeClone(), false);
                return;
            }

            nextTrack();
        }
    }

    public AudioTrack getNextTrack() {
        try {
            return new ArrayList<>(queue).get(0);
        }catch(Exception ignored) {
            return null;
        }
    }
}
