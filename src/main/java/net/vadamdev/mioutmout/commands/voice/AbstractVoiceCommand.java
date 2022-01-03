package net.vadamdev.mioutmout.commands.voice;

import com.jagrosh.jdautilities.command.Command;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.vadamdev.mioutmout.listeners.EventListener;

public abstract class AbstractVoiceCommand extends Command {
    public AbstractVoiceCommand(String name) {
        this.name = name;
    }

    public VoiceChannel getCustomVoiceChannel(Member member) {
        return member.getGuild().getVoiceChannelById(getCustomVoiceChannelID(member));
    }

    public String getCustomVoiceChannelID(Member member) {
        return EventListener.temporalyChannel.get(member.getUser().getName());
    }

    public boolean hasCustomVoiceChannel(Member member) {
        return EventListener.temporalyChannel.get(member.getUser().getName()) != null;
    }
}
