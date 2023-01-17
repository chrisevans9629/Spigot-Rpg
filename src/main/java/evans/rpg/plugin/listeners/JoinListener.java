package evans.rpg.plugin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    @EventHandler
    public  void onPlayerJoin(PlayerJoinEvent event){
        //org.bukkit.event.player.PlayerInteractEvent;

        event.setJoinMessage("Welcome, " + event.getPlayer().getName() + "!");

    }
}
