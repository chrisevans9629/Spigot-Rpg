package evans.rpg.plugin.listeners;

import evans.rpg.plugin.RpgPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffectType;

public class JoinListener implements Listener {

    private RpgPlugin plugin;

    public  JoinListener(RpgPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public  void onPlayerJoin(PlayerJoinEvent event){
        //org.bukkit.event.player.PlayerInteractEvent;

        event.setJoinMessage("Welcome, " + event.getPlayer().getName() + "!");
        AddEffects(event.getPlayer());
    }

    @EventHandler
    public  void onPlayerInteract(PlayerInteractEvent event){
        AddEffects(event.getPlayer());
    }

    public void AddEffects(Player player){
        boolean speed = plugin.getClassFlag(player, "speed");
        boolean haste = plugin.getClassFlag(player, "haste");
        boolean resistance = plugin.getClassFlag(player, "resistance");
        if(speed){
            SetConstantEffect(player, PotionEffectType.SPEED);
        }
        else if(haste){
            SetConstantEffect(player, PotionEffectType.FAST_DIGGING);
        }
        else if(resistance){
            SetConstantEffect(player, PotionEffectType.DAMAGE_RESISTANCE);
        }
    }

    public void SetConstantEffect(Player player, PotionEffectType type){
        if(!player.hasPotionEffect(type)){
            player.addPotionEffect(type.createEffect(Integer.MAX_VALUE, 1));
        }
    }
}
