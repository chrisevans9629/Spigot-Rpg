package evans.rpg.plugin.listeners;

import evans.rpg.plugin.RpgPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class InvisibleListener implements Listener {
    private RpgPlugin plugin;

    public InvisibleListener(RpgPlugin plugin){

        this.plugin = plugin;
    }
    @EventHandler
    public void onCrouch(PlayerToggleSneakEvent event){
        Player player = event.getPlayer();
        boolean canHide = plugin.getClassFlag(player, "invisibilitySneak");
        if(!canHide)
            return;
        if(event.isSneaking()){
            PotionEffect effect = PotionEffectType.INVISIBILITY.createEffect(1000, 1);
            player.addPotionEffect(effect);
        }
        else if(player.hasPotionEffect(PotionEffectType.INVISIBILITY)){
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
        }
    }

}
