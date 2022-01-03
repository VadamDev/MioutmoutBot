package net.vadamdev.mioutmout.commands.music;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.vadamdev.mioutmout.music.GuildMusicManager;
import net.vadamdev.mioutmout.music.PlayerManager;
import net.vadamdev.mioutmout.utils.CMDEmbedMessage;
import net.vadamdev.mioutmout.utils.Command;

public class MusicPurgeCommand extends Command {
    public MusicPurgeCommand() {
        super("purge");
    }

    @Override
    public void execute(CommandEvent event, Member sender, String[] args) {
        Guild guild = event.getGuild();

        if(guild.getAudioManager().isConnected()) {
            if(sender.getVoiceState().inVoiceChannel()) {
                if(sender.getVoiceState().getChannel().equals(guild.getAudioManager().getConnectedChannel())) {
                    final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
                    if(!musicManager.scheduler.queue.isEmpty()) musicManager.scheduler.queue.clear();
                    event.reply(new CMDEmbedMessage("Mioutmout Music - Purge", "La queue a été purgé.", CMDEmbedMessage.EmbedType.SUCCES).build());
                }else event.reply(new CMDEmbedMessage("Mioutmout Music - Purge", "Vous devez être dans le même channel que le bot pour  utiliser cette commande !", CMDEmbedMessage.EmbedType.ERROR).build());
            }else event.reply(new CMDEmbedMessage("Mioutmout Music - Purge", "Vous devez être dans un channel vocal pour utiliser cette commande !", CMDEmbedMessage.EmbedType.ERROR).build());
        }else event.reply(new CMDEmbedMessage("Mioutmout Music - Purge", "Le bot n'est pas connecté !", CMDEmbedMessage.EmbedType.ERROR).build());
    }
}
