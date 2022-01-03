package net.vadamdev.mioutmout;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.vadamdev.mioutmout.commands.ClearCommand;
import net.vadamdev.mioutmout.commands.GrosCaillouCommand;
import net.vadamdev.mioutmout.commands.SetupCommand;
import net.vadamdev.mioutmout.commands.music.*;
import net.vadamdev.mioutmout.commands.voice.VoiceKickCommand;
import net.vadamdev.mioutmout.commands.voice.VoiceRenameCommand;
import net.vadamdev.mioutmout.commands.voice.VoiceSlotCommand;
import net.vadamdev.mioutmout.listeners.EventListener;
import net.vadamdev.mioutmout.listeners.IOListener;
import net.vadamdev.mioutmout.utils.config.Config;
import net.vadamdev.mioutmout.utils.config.FileUtils;

import javax.security.auth.login.LoginException;
import java.util.Arrays;

public class Main {
    private static Config config;

    public static void main(String[] args) throws LoginException {
        config = FileUtils.DATA.getConfig();

        final LoginType loginType = LoginType.RELEASE;

        CommandClientBuilder ccb = new CommandClientBuilder().setOwnerId(loginType.getApplicationId()).setStatus(loginType.getStatus()).setActivity(loginType.getActivity()).setPrefix(loginType.getCommandPrefix());
        registerCommand(ccb);

        ccb.setHelpConsumer(event -> event.reply(new EmbedBuilder().setTitle("Mioutmout - Aide").setDescription("**__Prefix__ :** " + loginType.getCommandPrefix() + "\n\n**Mioutmout Commands :** \n- ``m/setup``\n- ``m/clear`` - Supprime un nombre définis de message\n\n- ``m/voice-rename`` - Renomme votre salon personnalisé\n- ``m/voice-slot`` - Change le nombre de slot de votre salon personnalisé\n- ``m/voice-kick`` - Kick quelqu'un de votre salon personnalisé\n\n- ``m/interface`` - Créé une interface pour gérer le bot de musique\n- ``m/play source`` - Joue une musique dans votre salon\n- ``m/skip`` - Skip la musique actuellement joué\n- ``m/purge`` - Purge la queue\n- ``m/np`` - Donne le titre de la musique actuellement joué\n- ``m/queue`` - Donne la liste de toutes les musiques présente dans la queue\n- ``m/repeat`` - Répète la musique actuellement joué\n- ``m/shuffle`` - Mélange la queue\n- ``m/volume`` - Change le volume (en %)\n- ``m/leave`` - Purge la queue et déconnecte le bot").setFooter("Mioutmout - By VadamDev#0001", event.getJDA().getSelfUser().getAvatarUrl()).setThumbnail(event.getJDA().getSelfUser().getAvatarUrl()).build()));

        JDABuilder jdaBuilder = JDABuilder.createDefault(loginType.getToken()).setAutoReconnect(true).addEventListeners(ccb.build());
        jdaBuilder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        registerListeners(jdaBuilder);
        jdaBuilder.build();
    }

    private static void registerListeners(JDABuilder builder) {
        Arrays.asList(
                new EventListener(),
                new IOListener()/*,
                new RoleReactionListener()*/
        ).forEach(builder::addEventListeners);
    }

    private static void registerCommand(CommandClientBuilder builder) {
        Arrays.asList(
                new SetupCommand(),
                new ClearCommand(),
                //new RoleReactionCommand(),

                new MusicPlayCommand(),
                new MusicInterfaceCommand(),
                new MusicQueueCommand(),
                new MusicVolumeCommand(),
                new MusicRepeatCommand(),
                new MusicShuffleCommand(),
                new MusicNowPlayingCommand(),
                new MusicSkipCommand(),
                new MusicPurgeCommand(),
                //new MusicSkipToCommand(),
                new MusicLeaveCommand(),

                new GrosCaillouCommand(),

                new VoiceRenameCommand(),
                new VoiceSlotCommand(),
                new VoiceKickCommand()
        ).forEach(builder::addCommand);
    }

    public static Config getConfig() {
        return config;
    }
}
