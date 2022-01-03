package net.vadamdev.mioutmout.commands.voice;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.Member;
import net.vadamdev.mioutmout.utils.CMDEmbedMessage;

public class VoiceKickCommand extends AbstractVoiceCommand {
    public VoiceKickCommand() {
        super("voice-kick");
    }

    @Override
    protected void execute(CommandEvent event) {
        Member member = event.getMember();

        if(hasCustomVoiceChannel(member)) {
            Member target = event.getMessage().getMentionedMembers().get(0);

            System.out.println(target.getUser().getName());

            if(getCustomVoiceChannel(member).getMembers().contains(target)) {
                member.getGuild().kickVoiceMember(target).queue();
                event.reply(new CMDEmbedMessage("Custom Voice Channel", target.getUser().getName() + " a été expluser de votre salon !", CMDEmbedMessage.EmbedType.SUCCES).build());
            }else event.reply(new CMDEmbedMessage("Custom Voice Channel", target.getUser().getName() + " n'est pas dans votre salon !", CMDEmbedMessage.EmbedType.WARNING).build());

        }else event.reply(new CMDEmbedMessage("Custom Voice Channel", "Vous devez avoir un channel custom pour éxécuté cette commande !", CMDEmbedMessage.EmbedType.WARNING).build());
    }
}
