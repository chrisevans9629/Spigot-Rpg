package evans.rpg.plugin;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
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
        Boolean isForce = args.length > 1 && args[1].equals("--force");

        Player player = (Player)sender;
        NamespacedKey classKey = new NamespacedKey(plugin, "class");

        PersistentDataContainer pdc = player.getPersistentDataContainer();
        String currentClass = pdc.get(classKey, PersistentDataType.STRING);

        if(currentClass != null && !isForce)
        {
            sender.sendMessage("Your class already is: " + currentClass);
            return true;
        }
        ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();

        String classItemPath = "classes." + className + ".items";

        ConfigurationSection itemSection = plugin.getConfig().getConfigurationSection(classItemPath);
        if(itemSection == null){
            sender.sendMessage("items missing");
            return true;
        }
        Set<String> items = itemSection.getKeys(false);

        if(items == null)
        {
            sender.sendMessage("class " + className + " has no items");
            return true;
        }
        Logger logger = plugin.getLogger();
        for (String item : items){
            logger.info("item: " + item);

            Material material = Material.getMaterial(item);
            int count = plugin.getConfig().getInt(classItemPath + "." + item + ".count");
            if(material == null){
                sender.sendMessage("material not found: " + item);
                return true;
            }

            logger.info("count: " + count);
            ItemStack stack = new ItemStack(material);
            if(count > 0){
                stack.setAmount(count);
            }
            AddEnchantments(classItemPath, logger, item, stack);

            stacks.add(stack);
        }

        for (ItemStack item : stacks){
            player.getInventory().addItem(item);
        }
        pdc.set(classKey, PersistentDataType.STRING, className);
        return true;
    }

    private void AddEnchantments(String classItemPath, Logger logger, String item, ItemStack stack) {
        ConfigurationSection enchantSection = plugin.getConfig().getConfigurationSection(classItemPath + "." + item + ".enchantments");

        if(enchantSection != null)
        {
            Map<String, Object> enchantments = enchantSection.getValues(false);

            if(enchantments != null){
                for (String enchantment : enchantments.keySet()){
                    logger.info("enchantment: " + enchantment);
                    Enchantment ench = Enchantment.getByKey(NamespacedKey.minecraft(enchantment.toLowerCase()));
                    int level = (Integer)enchantments.get(enchantment);
                    stack.addEnchantment(ench, level);
                }
            }
        }
    }
}
