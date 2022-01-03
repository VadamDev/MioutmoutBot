package net.vadamdev.mioutmout.commands.music;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.vadamdev.mioutmout.music.PlayerManager;
import net.vadamdev.mioutmout.music.TrackScheduler;
import net.vadamdev.mioutmout.utils.CMDEmbedMessage;
import net.vadamdev.mioutmout.utils.Command;

public class MusicRepeatCommand extends Command {
    public MusicRepeatCommand() {
        super("repeat");
    }

    @Override
    public void execute(CommandEvent event, Member sender, String[] args) {
        Guild guild = event.getGuild();

        if(guild.getAudioManager().isConnected()) {
            if(sender.getVoiceState().inVoiceChannel()) {
                if(sender.getVoiceState().getChannel().equals(guild.getAudioManager().getConnectedChannel())) {
                    TrackScheduler scheduler = PlayerManager.getInstance().getMusicManager(event.getGuild()).scheduler;
                    scheduler.repeating = !scheduler.repeating;
                    event.reply(new CMDEmbedMessage("Mioutmout Music - Repeat", scheduler.repeating ? "Cette musique sera joué en boucle." : "Cette musique ne sera plus joué en boucle.", CMDEmbedMessage.EmbedType.SUCCES).build());
                }else event.reply(new CMDEmbedMessage("Mioutmout Music - Repeat", "Vous devez être dans le même channel que le bot pour  utiliser cette commande !", CMDEmbedMessage.EmbedType.ERROR).build());
            }else event.reply(new CMDEmbedMessage("Mioutmout Music - Repeat", "Vous devez être dans un channel vocal pour utiliser cette commande !", CMDEmbedMessage.EmbedType.ERROR).build());
        }else event.reply(new CMDEmbedMessage("Mioutmout Music - Repeat", "Le bot n'est pas connecté !", CMDEmbedMessage.EmbedType.ERROR).build());
    }
}
