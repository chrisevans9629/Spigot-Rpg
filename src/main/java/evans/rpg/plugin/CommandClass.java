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
            sender.sendMessage("Usage: /class name");
            return true;
        }
        String className = args[0];
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

            logger.info("count: " + count);
            ItemStack stack = new ItemStack(material);
            if(count > 0){
                stack.setAmount(count);
            }

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

            stacks.add(stack);
        }
//
//        if(className.equals("fighter")){
//
//
//            ArrayList<Material> armor = new ArrayList<Material>();
//            armor.add(Material.IRON_HELMET);
//            armor.add(Material.IRON_CHESTPLATE);
//            armor.add(Material.IRON_BOOTS);
//            armor.add(Material.IRON_LEGGINGS);
//
//
//            for (Material armo : armor) {
//                ItemStack stack = new ItemStack(armo);
//                stack.addEnchantment(Enchantment.MENDING, 1);
//                stack.addEnchantment(Enchantment.DURABILITY, 3);
//                stack.addEnchantment(Enchantment.THORNS, 3);
//                stack.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
//                stacks.add(stack);
//            }
//
//            stacks.add(new ItemStack(Material.SHIELD));
//
//            ItemStack sword = new ItemStack(Material.IRON_SWORD);
//            sword.addEnchantment(Enchantment.DURABILITY, 3);
//            sword.addEnchantment(Enchantment.LOOT_BONUS_MOBS, 3);
//            sword.addEnchantment(Enchantment.DAMAGE_ALL, 5);
//            stacks.add(sword);
//
//            ItemStack bow = new ItemStack(Material.BOW);
//            bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
//            bow.addEnchantment(Enchantment.ARROW_DAMAGE, 3);
//            bow.addEnchantment(Enchantment.DURABILITY, 3);
//            bow.addEnchantment(Enchantment.ARROW_FIRE, 1);
//
//            stacks.add(bow);
//        }
//        else if(className.equals("builder")){
//
//        }
//        else{
//            sender.sendMessage("Class was not recognized: " + className);
//            return true;
//        }

        for (ItemStack item : stacks){
            player.getInventory().addItem(item);
        }
        pdc.set(classKey, PersistentDataType.STRING, className);
        return true;
    }
}
