package me.puyttre.svl;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Messager {
    
    private JavaPlugin plugin;
    
    public Messager(JavaPlugin plugin){
        this.plugin = plugin;
    }
    
    public void log(String msg) {
        System.out.println("[SimpleVoteListener] " + msg);
    }
    
    public void warn(String msg) {
        Bukkit.getLogger().warning(msg);
    }
    
    public void severe(String msg) {
        Bukkit.getLogger().severe(msg);
    }
    
    public void error(String msg) {
        System.out.println("[SimpleVoteListener] ERROR: " + msg);
    }
    
}
