package net.vadamdev.mioutmout.commands.music;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.vadamdev.mioutmout.music.PlayerManager;
import net.vadamdev.mioutmout.utils.CMDEmbedMessage;
import net.vadamdev.mioutmout.utils.Command;

public class MusicVolumeCommand extends Command {
    public MusicVolumeCommand() {
        super("volume");
    }

    @Override
    public void execute(CommandEvent event, Member sender, String[] args) {
        Guild guild = event.getGuild();

        if(guild.getAudioManager().isConnected()) {
            if(sender.getVoiceState().inVoiceChannel()) {
                if(sender.getVoiceState().getChannel().equals(guild.getAudioManager().getConnectedChannel())) {
                    try {
                        int volume = Integer.parseInt(args[0]);

                        if(volume > 125) {
                            event.reply(new CMDEmbedMessage("Mioutmout Music - Volume", "Le volume ne peux pas être supérieur à 125% !", CMDEmbedMessage.EmbedType.ERROR).build());
                            return;
                        }

                        PlayerManager.getInstance().getMusicManager(event.getGuild()).audioPlayer.setVolume(volume);
                        event.reply(new CMDEmbedMessage("Mioutmout Music - Volume", "Le volume est maintenant à **" + volume + "%**.", CMDEmbedMessage.EmbedType.SUCCES).build());
                    }catch(Exception ignored) {
                        event.reply(new CMDEmbedMessage("Mioutmout Music - Volume", "Vous devez entré un nombre dans le 1er argument !", CMDEmbedMessage.EmbedType.ERROR).build());
                    }
                }else event.reply(new CMDEmbedMessage("Mioutmout Music - Volume", "Vous devez être dans le même channel que le bot pour  utiliser cette commande !", CMDEmbedMessage.EmbedType.ERROR).build());
            }else event.reply(new CMDEmbedMessage("Mioutmout Music - Volume", "Vous devez être dans un channel vocal pour utiliser cette commande !", CMDEmbedMessage.EmbedType.ERROR).build());
        }else event.reply(new CMDEmbedMessage("Mioutmout Music - Volume", "Le bot n'est pas connecté !", CMDEmbedMessage.EmbedType.ERROR).build());
    }
}
