package net.vadamdev.mioutmout.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.vadamdev.mioutmout.Main;
import net.vadamdev.mioutmout.utils.Utils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class IOListener extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        Guild guild = event.getGuild();
        Member member = event.getMember();
        User user = member.getUser();

        TextChannel textChannel = guild.getTextChannelById(Main.getConfig().getString("IOMessagesChannel"));
        textChannel.sendMessageEmbeds(new EmbedBuilder().setTitle("Bienvenue " + user.getName() + " ! " + Utils.getEmote(guild, "mioutmout")).setDescription(user.getName() + " , je te souhaite la bienvenue sur le discord **Feed The Modders**").setThumbnail(user.getAvatarUrl()).setColor(Color.YELLOW).build()).complete();

        guild.addRoleToMember(member, guild.getRoleById("715681900760399923")).queue();
        guild.addRoleToMember(member, guild.getRoleById("792757536650428416")).queue();
    }
}
