package net.vadamdev.mioutmout.commands;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.Member;
import net.vadamdev.mioutmout.utils.Command;

public class GrosCaillouCommand extends Command {
    public GrosCaillouCommand() {
        super("groscaillou", "groscailloux");
    }

    @Override
    public void execute(CommandEvent event, Member sender, String[] args) {
        event.getMessage().delete().queue();
        event.reply("https://tenor.com/view/groscailloux-leskassos-the-rock-gif-21678967");
    }
}
