package net.vadamdev.mioutmout.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;

public class GuildMusicManager {
    public final AudioPlayer audioPlayer;
    public final TrackScheduler scheduler;
    private AudioHandler sendHandler;
    public Message interfaceMessage;

    public GuildMusicManager(Guild guild, AudioPlayerManager audioPlayerManager) {
        this.audioPlayer = audioPlayerManager.createPlayer();
        this.audioPlayer.setVolume(25);
        this.scheduler = new TrackScheduler(guild, this.audioPlayer);
        this.audioPlayer.addListener(this.scheduler);
        this.sendHandler = new AudioHandler(this.audioPlayer);
    }

    public AudioHandler getSendHandler() {
        return sendHandler;
    }
}
