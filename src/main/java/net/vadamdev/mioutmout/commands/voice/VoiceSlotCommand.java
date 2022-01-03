package net.vadamdev.mioutmout.commands.voice;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.Member;
import net.vadamdev.mioutmout.utils.CMDEmbedMessage;

public class VoiceSlotCommand extends AbstractVoiceCommand {
    public VoiceSlotCommand() {
        super("voice-slot");
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] args = event.getArgs().split(" ");
        Member member = event.getMember();

        if(hasCustomVoiceChannel(member)) {
            int slots;

            try {
                slots = Integer.parseInt(args[0]);;
            }catch (Exception ignored) {
                event.reply(new CMDEmbedMessage("Custom Voice Channel", "Vous devez spécifier le nouveau nombre de slots !", CMDEmbedMessage.EmbedType.ERROR).build());
                return;
            }

            if(slots > 99) {
                event.reply(new CMDEmbedMessage("Custom Voice Channel", "Le nombre de slot de votre channel est trop élevé ! (Max : 99)", CMDEmbedMessage.EmbedType.ERROR).build());
                return;
            }


            getCustomVoiceChannel(member).getManager().setUserLimit(Integer.parseInt(args[0])).queue();

            event.reply(new CMDEmbedMessage("Custom Voice Channel", "Le nombre de slot de votre channel est maintenant de " + args[0], CMDEmbedMessage.EmbedType.SUCCES).build());
        }else event.reply(new CMDEmbedMessage("Custom Voice Channel", "Vous devez avoir un channel custom pour éxécuté cette commande !", CMDEmbedMessage.EmbedType.WARNING).build());
    }
}
