package me.d4rk.dispongie;

import com.mashape.unirest.http.Unirest;
import net.dv8tion.jda.core.JDA;
import org.json.JSONObject;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.message.MessageChannelEvent;


public class ChatListener {

    JDA jda;
    String channel_id;
    String webhook;

    ChatListener(String weeb, JDA gee, String chid) {
        webhook = weeb;
        jda = gee;
        channel_id = chid;
    }

    @Listener
    public void onChat(MessageChannelEvent.Chat event) {

        if(webhook.length() > 5) {
            JSONObject json = new JSONObject();

            json.put("content", event.getRawMessage().toPlain());
            json.put("username", event.getCause().first(Player.class).get().getName());
            json.put("avatar_url", "https://minotar.net/avatar/"+event.getCause().first(Player.class).get().getName());

            try{
                Unirest.post(webhook)
                        .header("Content-Type", "application/json")
                        .body(json)
                        .asJsonAsync();
            }catch(Exception e){ e.printStackTrace(); }
        }else{
            jda.getTextChannelById(channel_id).sendMessage("**"+event.getCause().first(Player.class).get().getName()+": **"+event.getRawMessage().toPlain()).queue();
        }
    }

}
