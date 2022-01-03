package net.vadamdev.mioutmout.commands.music;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.vadamdev.mioutmout.music.PlayerManager;
import net.vadamdev.mioutmout.utils.CMDEmbedMessage;
import net.vadamdev.mioutmout.utils.Command;

public class MusicNowPlayingCommand extends Command {
    public MusicNowPlayingCommand() {
        super("nowplaying", "np");
    }

    @Override
    public void execute(CommandEvent event, Member sender, String[] args) {
        Guild guild = event.getGuild();

        if(guild.getAudioManager().isConnected()) {
            if(sender.getVoiceState().inVoiceChannel()) {
                if(sender.getVoiceState().getChannel().equals(guild.getAudioManager().getConnectedChannel())) {
                    AudioTrack track = PlayerManager.getInstance().getMusicManager(event.getGuild()).audioPlayer.getPlayingTrack();
                    event.getTextChannel().sendMessageEmbeds(new CMDEmbedMessage("Mioutmout Music - Now Playing", "La musique qui est actuellement joué est:\n**" + track.getInfo().title + "** by **" + track.getInfo().author + "**", CMDEmbedMessage.EmbedType.SUCCES).build()).queue();
                }else event.reply(new CMDEmbedMessage("Mioutmout Music - Now Playing", "Vous devez être dans le même channel que le bot pour  utiliser cette commande !", CMDEmbedMessage.EmbedType.ERROR).build());
            }else event.reply(new CMDEmbedMessage("Mioutmout Music - Now Playing", "Vous devez être dans un channel vocal pour utiliser cette commande !", CMDEmbedMessage.EmbedType.ERROR).build());
        }else event.reply(new CMDEmbedMessage("Mioutmout Music - Now Playing", "Le bot n'est pas connecté !", CMDEmbedMessage.EmbedType.ERROR).build());
    }
}
