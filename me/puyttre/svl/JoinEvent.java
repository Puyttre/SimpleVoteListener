package me.puyttre.svl;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {
    
    private SimpleVoteListener plugin;
    
    public JoinEvent(SimpleVoteListener plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent e) {
        if (plugin.offline.contains(e.getPlayer().getName().toLowerCase())) {
            if (plugin.config.isSet("private-message")) {
                e.getPlayer().sendMessage(plugin.replaceColor(plugin.config.getString("private-message").replaceAll("%name%", e.getPlayer().getDisplayName())));
            }
            if (plugin.config.isSet("login-votes")) {
                plugin.initLoginVotes(e.getPlayer().getName());
            }
            plugin.offline.remove(e.getPlayer().getName().toLowerCase());
        }
    }
    
}
