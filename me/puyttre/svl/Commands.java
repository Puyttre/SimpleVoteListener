package me.puyttre.svl;

import java.io.File;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

public class Commands implements CommandExecutor {
    
    private SimpleVoteListener plugin;
    
    public Commands(SimpleVoteListener plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.isOp()) return true;
        if (cmd.getName().toLowerCase().equals("svl")) {
            if (args.length > 0) {
                if (args[0].equals("reload")) {
                    plugin.config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "SimpleVoteListener.yml"));
                    sender.sendMessage(ChatColor.GREEN + "[SVL] Config reloaded.");
                } else {
                    sender.sendMessage(ChatColor.RED + "Unknown command.");
                }
            } else {
                sender.sendMessage(ChatColor.YELLOW + "SVL: " + ChatColor.BLUE + "reload");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Unknown command.");
        }
        return true;
    }
    
}
