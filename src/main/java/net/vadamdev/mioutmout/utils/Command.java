package net.vadamdev.mioutmout.utils;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

public abstract class Command extends com.jagrosh.jdautilities.command.Command {
    /**
     * @author VadamDev
     * @since 29.09.2021
     *
     * It's a better version of the Jagrosh command object
     */

    public Command(String name) {
        super.name = name;
    }

    public Command(String name, String... aliases) {
        super.name = name;
        super.aliases = aliases;
    }

    @Override
    protected void execute(CommandEvent event) {
        if(getRequirePermission() != null && !event.getMember().getPermissions().contains(getRequirePermission())) {
            event.reply(new CMDEmbedMessage("Erreur de permission", "Il vous manque la permission " + getRequirePermission().getName() + " pour éxécuté cette commande !", CMDEmbedMessage.EmbedType.ERROR).build());
        }else execute(event, event.getMember(), event.getArgs().split(" "));
    }

    public Permission getRequirePermission() {
        return null;
    }

    public abstract void execute(CommandEvent event, Member sender, String[] args);
}
