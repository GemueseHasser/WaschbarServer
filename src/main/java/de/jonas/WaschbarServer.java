package de.jonas;

import de.jonas.listener.JoinListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Die Haupt- und Main-Klasse dieses Plugins, worin das gesamte Plugin initialisiert wird. Diese Klasse wird vom Server
 * aufgerufen, da sie ein {@link JavaPlugin} darstellt.
 */
public final class WaschbarServer extends JavaPlugin {

    //<editor-fold desc="STATIC FIELDS">
    /** Die Instanz, mit der man auf dieses Plugin zugreifen kann. */
    @Getter
    private static WaschbarServer instance;
    //</editor-fold>

    //<editor-fold desc="setup and startup">
    @Override
    public void onEnable() {
        super.onEnable();

        // declare instance
        instance = this;

        // load listener
        final PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new JoinListener(), this);

        getSLF4JLogger().info("Das Plugin wurde erfolgreich geladen.");
    }
    //</editor-fold>

    //<editor-fold desc="stop">
    @Override
    public void onDisable() {
        super.onDisable();
    }
    //</editor-fold>

}
