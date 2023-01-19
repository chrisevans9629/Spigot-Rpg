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
        event.setJoinMessage("Welcome, " + event.getPlayer().getName() + "!");
        plugin.AddEffects(event.getPlayer());
    }



}
