package net.vadamdev.mioutmout.commands.voice;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.Member;
import net.vadamdev.mioutmout.utils.CMDEmbedMessage;

public class VoiceRenameCommand extends AbstractVoiceCommand {
    public VoiceRenameCommand() {
        super("voice-rename");
    }

    @Override
    protected void execute(CommandEvent event) {
        Member member = event.getMember();

        if(hasCustomVoiceChannel(member)) {
            getCustomVoiceChannel(member).getManager().setName(event.getArgs()).queue();

            event.reply(new CMDEmbedMessage("Custom Voice Channel", "Vous avez changer le nom de votre channel custom en :\n" + event.getArgs(), CMDEmbedMessage.EmbedType.SUCCES).build());
        }else event.reply(new CMDEmbedMessage("Custom Voice Channel", "Vous devez avoir un channel custom pour éxécuté cette commande !", CMDEmbedMessage.EmbedType.WARNING).build());
    }
}
