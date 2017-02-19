package me.d4rk.dispongie;

import com.google.inject.Inject;
import org.json.JSONObject;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.impl.JDAImpl;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.utils.SimpleLog;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.plugin.Plugin;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;

@Plugin(id = "dispongie", name = "Dispongie", version = "1.0")
public class Dispongie {

    private JDA jda;
    private String channel_id;
    private String bot_token;
    private String webhook;
    private JSONObject config;

    @Inject
    @ConfigDir(sharedRoot = true)
    private Path configDir;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {


        File f = new File(configDir + "//dispongie.json");
        if(!f.exists()) {
            try {
                JSONObject config = new JSONObject();
                config.put("Token", "");
                config.put("Channel_ID", "");
                config.put("Webhook_Url", "");
                FileWriter newfile = new FileWriter(configDir + "//dispongie.json");
                newfile.write(config.toString());
                newfile.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        try {
            config = new JSONObject(new String(Files.readAllBytes(f.toPath()),"UTF-8"));
        }catch (Exception e){
            e.printStackTrace();
        }
        bot_token = config.getString("Token");
        channel_id = config.getString("Channel_ID");
        webhook = config.getString("Webhook_Url");

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