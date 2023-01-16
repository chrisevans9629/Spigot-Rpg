package evans.rpg.plugin;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RpgPlugin extends JavaPlugin {

    private final Map<UUID, String> playerReach = new HashMap<UUID, String>();

    @Override
    public void onEnable() {
        getLogger().info("onEnable is called!");
        this.saveDefaultConfig();
        this.getCommand("class").setExecutor(new CommandClass(this));
        getServer().getPluginManager().registerEvents(new MyListener(), this);
        super.onEnable();

    }

    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
        super.onDisable();
    }
}
