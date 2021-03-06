package net.vadamdev.mioutmout.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.vadamdev.mioutmout.utils.CMDEmbedMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager {
    private static PlayerManager instance;

    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    public PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    public GuildMusicManager getMusicManager(Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(guild, audioPlayerManager);
            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
            return guildMusicManager;
        });
    }

    public void loadAndPlay(TextChannel channel, String source) {
        final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());

        audioPlayerManager.loadItemOrdered(musicManager, source, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                musicManager.scheduler.queue(track);
                channel.sendMessageEmbeds(new CMDEmbedMessage("Mioutmout Music - Play", "Ajout de la musique:\n**" + track.getInfo().title + "** by **" + track.getInfo().author + "**", CMDEmbedMessage.EmbedType.SUCCES).build()).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                final List<AudioTrack> tracks = playlist.getTracks();

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Ajout de la playlist:\n**" + playlist.getName() + "**\n");

                for(int i = 0; i < tracks.size() && i < 5; i++)
                    stringBuilder.append("\n **-** " + playlist.getTracks().get(i).getInfo().title);

                if(tracks.size() >= 5) stringBuilder.append("\n **-** ... (+" + (tracks.size() - 5) + ")");

                channel.sendMessageEmbeds(new CMDEmbedMessage("Mioutmout Music - Play", stringBuilder.toString(), CMDEmbedMessage.EmbedType.SUCCES).build()).queue();

                for(final AudioTrack track : tracks) musicManager.scheduler.queue(track);
            }

            @Override
            public void noMatches() {
                channel.sendMessageEmbeds(new CMDEmbedMessage("Mioutmout Music - Play", source + " n'a pas ??t?? trouv?? !", CMDEmbedMessage.EmbedType.ERROR).build()).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                channel.sendMessageEmbeds(new CMDEmbedMessage("Mioutmout Music - Play", "Erreur lors de la lecture ! (" + exception.getMessage() + ")", CMDEmbedMessage.EmbedType.ERROR).build()).queue();
            }
        });
    }

    public static PlayerManager getInstance() {
        if (instance == null) instance = new PlayerManager();
        return instance;
    }
}
