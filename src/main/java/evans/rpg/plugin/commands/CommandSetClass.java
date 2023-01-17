package evans.rpg.plugin.commands;

import evans.rpg.plugin.RpgPlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class CommandSetClass {


    private final RpgPlugin plugin;

    public CommandSetClass(RpgPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean Execute(CommandSender sender, String className, String[] args) {
        boolean isForce = args.length > 1 && args[1].equals("--force");

        Player player = (Player)sender;

        String currentClass = plugin.getCurrentClass(player);

        if(currentClass != null && !isForce)
        {
            sender.sendMessage("Your class already is: " + currentClass);
            return true;
        }

        for(ItemStack item : player.getInventory().getContents()){
            player.getInventory().remove(item);
        }

        ArrayList<ItemStack> stacks = new ArrayList<>();

        String classItemPath = "classes." + className + ".items";

        ConfigurationSection itemSection = plugin.getConfig().getConfigurationSection(classItemPath);
        if(itemSection == null){
            sender.sendMessage("items missing");
            return true;
        }
        Set<String> items = itemSection.getKeys(false);

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
        plugin.setCurrentClass(player, className);
        return true;
    }

    private void AddEnchantments(String classItemPath, Logger logger, String item, ItemStack stack) {
        ConfigurationSection enchantSection = plugin.getConfig().getConfigurationSection(classItemPath + "." + item + ".enchantments");

        if(enchantSection != null)
        {
            Map<String, Object> enchantments = enchantSection.getValues(false);

            for (String enchantment : enchantments.keySet()){
                logger.info("enchantment: " + enchantment);
                Enchantment enchantItem = Enchantment.getByKey(NamespacedKey.minecraft(enchantment.toLowerCase()));
                int level = (Integer)enchantments.get(enchantment);
                assert enchantItem != null;
                stack.addEnchantment(enchantItem, level);
            }
        }
    }
}
