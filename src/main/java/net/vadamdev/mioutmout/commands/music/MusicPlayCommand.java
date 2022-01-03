package net.vadamdev.mioutmout.commands.music;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.vadamdev.mioutmout.music.PlayerManager;
import net.vadamdev.mioutmout.utils.CMDEmbedMessage;
import net.vadamdev.mioutmout.utils.Command;

import java.net.URI;
import java.net.URISyntaxException;

public class MusicPlayCommand extends Command {
    public MusicPlayCommand() {
        super("play");
    }

    @Override
    public void execute(CommandEvent event, Member sender, String[] args) {
        Guild guild = event.getGuild();

        if(sender.getVoiceState().inVoiceChannel()) {
            if(!guild.getAudioManager().isConnected()) {
                guild.getAudioManager().openAudioConnection(sender.getVoiceState().getChannel());
                loadAndPlayTrack(event);
            }else if(sender.getVoiceState().getChannel().equals(guild.getAudioManager().getConnectedChannel())) loadAndPlayTrack(event);
            else event.reply(new CMDEmbedMessage("Mioutmout Music - Play", "Vous devez être dans le même channel que le bot pour  utiliser cette commande !", CMDEmbedMessage.EmbedType.ERROR).build());
        }else event.reply(new CMDEmbedMessage("Mioutmout Music - Play", "Vous devez être dans un channel vocal pour utiliser cette commande !", CMDEmbedMessage.EmbedType.ERROR).build());
    }

    private void loadAndPlayTrack(CommandEvent event) {
        String link = String.join(" ", event.getArgs());
        if(!isUrl(link)) link = "ytsearch:" + link;
        PlayerManager.getInstance().loadAndPlay(event.getTextChannel(), link);
    }

    private boolean isUrl(String url) {
        try {
            new URI(url);
            return true;
        }catch (URISyntaxException ignored) {
            return false;
        }
    }
}
