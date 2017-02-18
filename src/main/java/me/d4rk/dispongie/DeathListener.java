package me.d4rk.dispongie;

import net.dv8tion.jda.core.JDA;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DestructEntityEvent;

public class DeathListener {

    JDA jda;
    String channel_id;

    DeathListener(JDA gee, String chid) {
        jda = gee;
        channel_id = chid;
    }

    @Listener
    public void onPlayerDeath(DestructEntityEvent.Death event) {
        if (event.getTargetEntity() instanceof Player) {
            jda.getTextChannelById(channel_id).sendMessage("**"+event.getMessage().toPlain()+"**").queue();
        }
    }

}
