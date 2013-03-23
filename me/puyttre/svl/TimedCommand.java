package me.puyttre.svl;

import com.vexsoftware.votifier.model.Vote;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Bukkit;

public class TimedCommand {
    
    private SimpleVoteListener plugin;
    
    public Set<String> commands = new HashSet();
    
    public TimedCommand(SimpleVoteListener plugin) {
        this.plugin = plugin;
    }
    
    public void startTimer(final Vote v, String s) {
        if (!s.contains(";")) {
            plugin.messager.error("You did not specify a time for the commands to dispatch. eg. 'tell %name% Its been 10 seconds since you voted!;10s'");
            return;
        }
        final String[] split = s.split(";");
        long time = Integer.parseInt(split[1].substring(0, split[1].length() - 1));
        if (split[1].toLowerCase().endsWith("s")) {
            time = time * 20;
        } else if (split[1].toLowerCase().endsWith("m")) {
            time = (time * 20) * 60;
        } else if (split[1].toLowerCase().endsWith("h")) {
            time = ((time * 20) * 60) * 60;
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                Bukkit.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), plugin.replaceColor(plugin.replace(split[0], v)));
                commands.remove(split[0]);
            }
        }, time);
        commands.add(split[0]);
    }
    
    public void stopAll() {
        for (String s : commands) {
            Bukkit.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), plugin.replaceColor(s.replaceAll("%name%", "player")));
        }
    }
    
}
