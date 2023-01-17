package evans.rpg.plugin;

import evans.rpg.plugin.commands.CommandClass;
import evans.rpg.plugin.listeners.JoinListener;
import evans.rpg.plugin.listeners.PlantListener;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RpgPlugin extends JavaPlugin {

    private final Map<UUID, String> playerReach = new HashMap<UUID, String>();
    private NamespacedKey classKey;

    @Override
    public void onEnable() {
        getLogger().info("onEnable is called!");
        classKey = new NamespacedKey(this, "class");

        this.saveDefaultConfig();
        this.getCommand("class").setExecutor(new CommandClass(this));
        getServer().getPluginManager().registerEvents(new PlantListener(this), this);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        super.onEnable();

    }

    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
        super.onDisable();
    }

    public String getCurrentClass(Player player){
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        String currentClass = pdc.get(classKey, PersistentDataType.STRING);
        return currentClass;
    }

    public void setCurrentClass(Player player, String className){
        player.getPersistentDataContainer().set(classKey, PersistentDataType.STRING, className);
    }

    public boolean getClassFlag(Player player, String flagName){
        String className = getCurrentClass(player);

        return getConfig().getBoolean("classes." + className + ".flags." + flagName);
    }
}
