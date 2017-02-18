package me.d4rk.dispongie;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.impl.JDAImpl;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.utils.SimpleLog;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import javax.security.auth.login.LoginException;

@Plugin(id = "dispongie", name = "Dispongie", version = "1.0")
public class Dispongie {


    JDA jda;
    String channel_id = "ID DO CANAL";
    String bot_token = "TOKEN DO BOT";
    String webhook = "LINK DO WEBHOOK";

    @Listener
    public void onServerStart(GameStartedServerEvent event) {

        JDAImpl.LOG.setLevel(SimpleLog.Level.OFF);

        try {
            jda = new JDABuilder(AccountType.BOT)
                    .setToken(bot_token)
                    .addListener(new DiscordListener(channel_id))
                    .buildBlocking();
        }
        catch (LoginException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (RateLimitedException e) {
            e.printStackTrace();
        }

        Sponge.getEventManager().registerListeners(this, new ChatListener(webhook, jda, channel_id));
        Sponge.getEventManager().registerListeners(this, new DeathListener(jda, channel_id));
        Sponge.getEventManager().registerListeners(this, new ConnectionListener(jda, channel_id));


        jda.getTextChannelById(channel_id).sendMessage("**Servidor iniciado!**").queue();
    }

    @Listener
    public void onServerStop(GameStoppedServerEvent event) {
        jda.getTextChannelById(channel_id).sendMessage("**Servidor desligado!**").queue();
        jda.shutdown();

        Sponge.getEventManager().unregisterPluginListeners(this);
    }

}