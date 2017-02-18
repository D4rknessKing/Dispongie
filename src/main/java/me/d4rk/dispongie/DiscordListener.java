package me.d4rk.dispongie;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class DiscordListener extends ListenerAdapter {

    String channel_id;

    DiscordListener(String chid) {
        channel_id = chid;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor() != event.getJDA().getSelfUser() && !event.getAuthor().isFake() && event.getTextChannel().getId().equals(channel_id) ) {
            Text discord = Text.builder("[Discord]").color(TextColors.BLUE).append(
                    Text.builder(" <"+event.getAuthor().getName()+"> "+event.getMessage().getRawContent()).color(TextColors.WHITE).build()).build();
            Sponge.getServer().getBroadcastChannel().send(discord);
        }
    }

}
