package de.jonas;

import de.jonas.command.AdminCommand;
import de.jonas.command.UserCommand;
import de.jonas.handler.command.CommandHandler;
import de.jonas.handler.unit.WaschbarUserHandler;
import de.jonas.listener.BlockPlaceBreakListener;
import de.jonas.listener.ChatListener;
import de.jonas.listener.JoinQuitListener;
import de.jonas.object.unit.WaschbarUser;
import de.jonas.task.ScoreboardUpdateTask;
import de.jonas.task.UserUpdateTask;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * Die Haupt- und Main-Klasse dieses Plugins, worin das gesamte Plugin initialisiert wird. Diese Klasse wird vom Server
 * aufgerufen, da sie ein {@link JavaPlugin} darstellt.
 */
public final class WaschbarServer extends JavaPlugin {

    //<editor-fold desc="STATIC FIELDS">
    /** Die Instanz, mit der man auf dieses Plugin zugreifen kann. */
    @Getter
    private static WaschbarServer instance;
    /** Der Prefix dieses Plugins. */
    @Getter
    private static String prefix;
    //</editor-fold>

    //<editor-fold desc="LOCAL FIELDS">
    /** Der {@link WaschbarUserHandler}, mit dem alle {@link WaschbarUser} auf diesem Netzwerk verarbeitet werden. */
    @Getter
    @NotNull
    private final WaschbarUserHandler waschbarUserHandler = new WaschbarUserHandler();
    /** Mithilfe des {@link CommandHandler} werden alle Befehle registriert. */
    @NotNull
    private final CommandHandler commandHandler = new CommandHandler();
    //</editor-fold>


    //<editor-fold desc="setup and startup">
    @Override
    public void onEnable() {
        super.onEnable();

        // declare instance
        instance = this;

        // declare prefix
        prefix = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + ChatColor.BOLD + "Waschbär" + ChatColor.DARK_GRAY + "] "
            + ChatColor.GRAY;

        // register commands
        getSLF4JLogger().info("Register all commands.");
        this.commandHandler.register(
            new Class[]{
                AdminCommand.class,
                UserCommand.class,
            }
        );

        // load listener
        getSLF4JLogger().info("Load all listener.");
        final PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new JoinQuitListener(), this);
        pm.registerEvents(new ChatListener(), this);
        pm.registerEvents(new BlockPlaceBreakListener(), this);

        // schedule periodic scoreboard updating
        getSLF4JLogger().info("Schedule periodic scoreboard updating.");
        new ScoreboardUpdateTask().runTaskTimer(
            this,
            0,
            10
        );

        // schedule periodic user updating
        getSLF4JLogger().info("Schedule periodic user updating.");
        new UserUpdateTask().runTaskTimer(
            this,
            0,
            1
        );

        getSLF4JLogger().info("The plugin was loaded successfully.");
    }
    //</editor-fold>


    //<editor-fold desc="stop">
    @Override
    public void onDisable() {
        super.onDisable();

        // unregister all commands
        this.commandHandler.unregisterAll();

        // save all users
        this.waschbarUserHandler.saveAllUsers();

        getSLF4JLogger().info("The plugin has been stopped");
    }
    //</editor-fold>

}
