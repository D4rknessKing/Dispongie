package me.d4rk.dispongie;

import net.dv8tion.jda.core.JDA;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;


public class ConnectionListener {

    JDA jda;
    String channel_id;

    ConnectionListener(JDA gee, String chid) {
        jda = gee;
        channel_id = chid;
    }

    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join event) {
        jda.getTextChannelById(channel_id).sendMessage("**"+event.getMessage().toPlain()+"**").queue();
    }

    @Listener
    public void onPlayerQuit(ClientConnectionEvent.Disconnect event) {
        jda.getTextChannelById(channel_id).sendMessage("**"+event.getMessage().toPlain()+"**").queue();
    }

}
