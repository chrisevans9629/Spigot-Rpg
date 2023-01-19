package evans.rpg.plugin.commands;

import evans.rpg.plugin.RpgPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class CommandClass implements CommandExecutor {

    private RpgPlugin plugin;

    public CommandClass(RpgPlugin plugin){
        this.plugin = plugin;
    }

    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args) {

        if(!(sender instanceof Player) || args.length < 1){
            sender.sendMessage("Usage: /class class_name|list");
            return true;
        }
        String className = args[0];

        if(className.equals("list")){
            Set<String> classes = plugin.getConfig().getConfigurationSection("classes").getKeys(false);

            for (String classItem : classes){
                String description = plugin.getConfig().getString("classes." + classItem + ".description");
                sender.sendMessage(ChatColor.GREEN + classItem + ": " + ChatColor.WHITE + description);
            }
            return true;
        }
        else{
            CommandSetClass setClass = new CommandSetClass(plugin);
            return setClass.Execute(sender, className, args);
        }

    }


}
