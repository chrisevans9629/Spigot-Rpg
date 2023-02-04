package evans.rpg.plugin.listeners;

import evans.rpg.plugin.RpgPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.logging.Logger;

public class PlantListener implements Listener {

    private RpgPlugin plugin;
    private Logger logger;
    public PlantListener(RpgPlugin plugin){
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        boolean multiPlant = plugin.getClassFlag(event.getPlayer(), "plantMany");
        if(multiPlant) {
            Block block = event.getBlock();
            if(block.getBlockData() instanceof Ageable){

                logger.info("broke a crop!!");
                for (BlockFace face : BlockFace.values()){
                    Block relative = block.getRelative(face);
                    BreakBlock(relative);
                }
            }
        }
    }

    public void BreakBlock(Block block){
        if(block.getBlockData() instanceof Ageable){
            Ageable age = (Ageable) block.getBlockData();
            if(age.getAge() != age.getMaximumAge()){
                return;
            }
            block.breakNaturally();
        }
    }

    public int PlaceBlock(Block block, int stackCount, int count){
        for (BlockFace face : BlockFace.values()){
            Block relative = block.getRelative(face);
            if(relative.canPlace(block.getBlockData()) && !(relative.getBlockData() instanceof Ageable) && count <= stackCount){
                relative.setBlockData(block.getBlockData());
                relative.setType(block.getType());
                logger.info("placed block count: " + count);
                count += 1;
                count += PlaceBlock(relative, stackCount, count);
            }
        }

        return count;
    }

    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent event){
        Player player = event.getPlayer();
        ItemStack stack = player.getItemInUse();

        boolean multiPlant = plugin.getClassFlag(player, "plantMany");
        if(multiPlant){

            Block block = event.getBlockPlaced();
            if(block.getBlockData() instanceof Ageable){

                logger.info("placed a crop!!");
                int stackCount = stack.getAmount();
                int amountPlaced = PlaceBlock(block, stackCount, 0);
                if(amountPlaced >= stackCount){
                    logger.info("removing stack: " + stack.getType().name());
                    player.getInventory().remove(stack);
                }
                else{
                    logger.info("decreasing count to: " + amountPlaced);
                    stack.setAmount(amountPlaced);
                }
            }
        }
    }
}