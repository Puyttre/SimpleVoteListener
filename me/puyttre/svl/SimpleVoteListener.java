package me.puyttre.svl;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleVoteListener extends JavaPlugin implements Listener {
    
    public FileConfiguration config;
    public Messager messager;
    private TimedCommand timer;
    
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new JoinEvent(this), this);
        this.messager = new Messager(this);
        this.timer = new TimedCommand(this);
        
        getCommand("svl").setExecutor(new Commands(this));
        
        if (!new File(getDataFolder(), "SimpleVoteListener.yml").exists()) {
            saveResource("SimpleVoteListener.yml", false);
            loadConfig();
            messager.log("Config file not found. Generating a new one for you!");
        } else {
            loadConfig();
            messager.log("Config file found. Using it!");
        }
        
        messager.log("SimpleVoteListener 2.4 successfully enabled.");
    }
    
    @Override
    public void onDisable() {
        messager.log("All Offline Players removed from list (Cause: plugin disabled)");
        messager.log("Stopping all timed commands...");
        timer.stopAll();
        messager.log("SimpleVoteListener 2.4 successfully disabled.");
    }
    
    public void loadConfig() {
        this.config = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "SimpleVoteListener.yml"));
    }

    @EventHandler
    public void vote(VotifierEvent e) {
        if (Bukkit.getPlayer(e.getVote().getUsername()) == null) {
            initOfflineVote(e.getVote());
        } else {
            initOnlineVote(e.getVote());
        }
    }
    
    public Set<String> offline = new HashSet();
    
    public void initOfflineVote(Vote v) {
        offline.add(v.getUsername().toLowerCase());
        if (config.isSet("public-message")) {
            Bukkit.broadcastMessage(replaceColor(replace(config.getString("public-message"), v)));
        }
        
        if (config.isSet("timed-commands")) {
            for (String s : config.getStringList("timed-commands")) {
                timer.startTimer(v, s);
            }
        }
        
        if (config.isSet("offline-commands")) {
            for (String s : config.getStringList("offline-commands")) {
                Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), replaceColor(replace(s, v)));
            }
        }
    }
    
    public void initLoginVotes(String name) {
        if (config.isSet("login-commands")) {
            for (String s : config.getStringList("login-commands")) {
                Player p = Bukkit.getPlayer(name);
                Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), replaceColor(s.replaceAll("%name%", p.getDisplayName())));
            }
        }
    }
    
    public void initOnlineVote(Vote v) {
        if (Bukkit.getPlayer(v.getUsername()) == null) return;
        Player p = Bukkit.getPlayer(v.getUsername());
        
        if (config.isSet("public-message")) {
            Bukkit.broadcastMessage(replaceColor(replace(config.getString("public-message"), v)));
        }
        
        if (config.isSet("private-message")) {
            p.sendMessage(replaceColor(replace(config.getString("private-message"), v)));
        }
        
        if (config.isSet("timed-commands")) {
            for (String s : config.getStringList("timed-commands")) {
                timer.startTimer(v, s);
            }
        }
        
        if (config.isSet("online-commands")) {
            for (String s : config.getStringList("online-commands")) {
                Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), replaceColor(replace(s, v)));
            }
        }
    }
    
    public String replace(String string, Vote vote) {
        return string.replaceAll("%name%", vote.getUsername()).replaceAll("%service%", vote.getServiceName()).replaceAll("%time%", vote.getTimeStamp()).replaceAll("%address%", vote.getAddress());
    }
    
    public String replaceColor(String string) {
        return string.replaceAll("(?i)&([a-f0-9])", "\u00A7$1");
    }

}
