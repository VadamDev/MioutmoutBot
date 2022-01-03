package net.vadamdev.mioutmout.commands.music;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.Button;
import net.vadamdev.mioutmout.music.GuildMusicManager;
import net.vadamdev.mioutmout.music.PlayerManager;
import net.vadamdev.mioutmout.utils.CMDEmbedMessage;
import net.vadamdev.mioutmout.utils.Command;
import org.apache.commons.lang.StringUtils;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class MusicInterfaceCommand extends Command {
    public MusicInterfaceCommand() {
        super("interface", "mi", "musicinterface");
    }

    @Override
    public void execute(CommandEvent event, Member sender, String[] args) {
        Guild guild = event.getGuild();

        if(guild.getAudioManager().isConnected()) {
            if(sender.getVoiceState().inVoiceChannel()) {
                if(sender.getVoiceState().getChannel().equals(guild.getAudioManager().getConnectedChannel())) {
                    final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());

                    event.getTextChannel().sendMessageEmbeds(getInterfaceMessage(musicManager)).setActionRow(
                            Button.primary("nextTrack", "Musique Suivante"),
                            Button.danger("leaveChannel", "Déconnecter le Bot"),
                            Button.primary("updateInterface", "Update le Panel")).queue(msg -> musicManager.interfaceMessage = msg);
                }else event.reply(new CMDEmbedMessage("Mioutmout Music - Interface", "Vous devez être dans le même channel que le bot pour  utiliser cette commande !", CMDEmbedMessage.EmbedType.ERROR).build());
            }else event.reply(new CMDEmbedMessage("Mioutmout Music - Interface", "Vous devez être dans un channel vocal pour utiliser cette commande !", CMDEmbedMessage.EmbedType.ERROR).build());
        }else event.reply(new CMDEmbedMessage("Mioutmout Music - Interface", "Le bot n'est pas connecté !", CMDEmbedMessage.EmbedType.ERROR).build());
    }

    public static MessageEmbed getInterfaceMessage(GuildMusicManager musicManager) {
        long rawTrackDuration = musicManager.audioPlayer.getPlayingTrack().getDuration();
        long rawTrackTime = musicManager.audioPlayer.getPlayingTrack().getPosition();

        String formattedNextTrack = musicManager.scheduler.getNextTrack() == null ? "Aucune" : musicManager.scheduler.getNextTrack().getInfo().title;

        return new EmbedBuilder().setColor(Color.orange).setTitle("Mioutmout Music - Interface").setDescription(musicManager.audioPlayer.getPlayingTrack().getInfo().title +
                        ":\n[" + getPlayBar(25, TimeUnit.MILLISECONDS.toSeconds(rawTrackTime), TimeUnit.MILLISECONDS.toSeconds(rawTrackDuration)) + "]\n[" + getTimeBar(rawTrackTime, rawTrackDuration) + "] - " + getProgressionPercentage(rawTrackTime, rawTrackDuration) + "\n\n" +
                        "Volume: " + musicManager.audioPlayer.getVolume() + "%\n\n" +
                        "Musique suivante:\n" + formattedNextTrack + "\n").build();
    }

    private static String getTimeBar(long trackTime, long trackDuration) {
        return String.format("%dm, %ds",TimeUnit.MILLISECONDS.toMinutes(trackTime), TimeUnit.MILLISECONDS.toSeconds(trackTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(trackTime)))
                + " / " +
                String.format("%d min, %ds",TimeUnit.MILLISECONDS.toMinutes(trackDuration), TimeUnit.MILLISECONDS.toSeconds(trackDuration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(trackDuration)));
    }

    private static String getProgressionPercentage(long trackTime, long trackDuration) {
        return trackTime * 100 / trackDuration + "%";
    }

    private static String getPlayBar(int barLenght, long value, long maxValue) {
        StringBuilder str = new StringBuilder(StringUtils.repeat("━", barLenght));
        long barState = value * barLenght / maxValue;

        for(int i = 0; i < barState; i++) {
            if(i == (barState - 1)) str.setCharAt(i, '►');
            else str.setCharAt(i, '▬');
        }

        return str.toString();
    }
}