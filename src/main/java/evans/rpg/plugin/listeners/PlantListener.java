package evans.rpg.plugin.listeners;

import evans.rpg.plugin.RpgPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;

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
                Ageable age = (Ageable) block.getBlockData();
                if(age.getAge() != age.getMaximumAge()){
                    logger.info("crop not ready... canceling");
                    event.setCancelled(true);
                    return;
                }
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

    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent event){
        boolean multiPlant = plugin.getClassFlag(event.getPlayer(), "plantMany");
        if(multiPlant){

            Block block = event.getBlockPlaced();
            if(block.getBlockData() instanceof Ageable){
                logger.info("placed a crop!!");
                for (BlockFace face : BlockFace.values()){
                    Block relative = block.getRelative(face);
                    if(relative.canPlace(block.getBlockData()) && !(relative.getBlockData() instanceof Ageable)){
                        relative.setBlockData(block.getBlockData());
                        relative.setType(block.getType());
                    }
                }
            }
        }
    }
}