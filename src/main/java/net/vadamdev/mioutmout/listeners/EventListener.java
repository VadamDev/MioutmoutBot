package net.vadamdev.mioutmout.listeners;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.vadamdev.mioutmout.Main;
import net.vadamdev.mioutmout.commands.music.MusicInterfaceCommand;
import net.vadamdev.mioutmout.music.GuildMusicManager;
import net.vadamdev.mioutmout.music.PlayerManager;
import net.vadamdev.mioutmout.utils.CMDEmbedMessage;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class EventListener extends ListenerAdapter {
    public static Map<String, String> temporalyChannel = new HashMap<>();

    @Override
    public void onGuildVoiceUpdate(@NotNull GuildVoiceUpdateEvent event) {
        VoiceChannel channel = event.getChannelJoined();
        User user = event.getMember().getUser();

        //Voice Creator
        if(event.getChannelJoined() != null && channel.getId().equals(Main.getConfig().getString("voiceCreatorID"))) {
            event.getGuild().createVoiceChannel("Salon de " + user.getName(), channel.getParent()).setUserlimit(5).queue(voiceChannel -> {
                channel.getMembers().forEach(member -> member.getGuild().moveVoiceMember(member, voiceChannel).queue());
                temporalyChannel.put(user.getName(), voiceChannel.getId());
            });
            return;
        }

        //Temporaly channel supressor
        if(event.getChannelLeft() != null && temporalyChannel.get(user.getName()) != null && event.getChannelLeft().getId().equals(temporalyChannel.get(event.getMember().getUser().getName()))) {
            event.getChannelLeft().delete().complete();
            temporalyChannel.remove(event.getMember().getUser().getName());
        }
    }

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        Guild guild = event.getGuild();
        String buttonId = event.getComponentId();

        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(guild);

        if(musicManager.interfaceMessage != null && musicManager.interfaceMessage.equals(event.getMessage())) {
            if(buttonId.equals("nextTrack")) {
                musicManager.scheduler.nextTrack();
                event.deferEdit().queue();
            }else if(buttonId.equals("leaveChannel")) {
                if(!musicManager.scheduler.queue.isEmpty()) musicManager.scheduler.queue.clear();
                musicManager.audioPlayer.stopTrack();
                musicManager.scheduler.nextTrack();
                event.replyEmbeds(new CMDEmbedMessage("Mioutmout Music - Clear", "Le bot a été déconnecté.", CMDEmbedMessage.EmbedType.SUCCES).build()).queue();
            }else if(buttonId.equals("updateInterface")) {
                musicManager.interfaceMessage.editMessageEmbeds(MusicInterfaceCommand.getInterfaceMessage(musicManager)).queue();
                event.deferEdit().queue();
            }
        }
    }
}
