package evans.rpg.plugin;

import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

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
            sender.sendMessage("Classes: " + String.join(",", classes));
            return true;
        }
        else{
            CommandSetClass setClass = new CommandSetClass(plugin);
            return setClass.Execute(sender, className, args);
        }

    }


}
