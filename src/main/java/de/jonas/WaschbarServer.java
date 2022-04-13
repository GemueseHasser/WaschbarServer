package de.jonas;

import de.jonas.command.AdminCommand;
import de.jonas.command.UserCommand;
import de.jonas.handler.command.CommandHandler;
import de.jonas.handler.unit.WaschbarUserHandler;
import de.jonas.listener.BlockPlaceBreakListener;
import de.jonas.listener.ChatListener;
import de.jonas.listener.DamageListener;
import de.jonas.listener.JoinQuitListener;
import de.jonas.listener.MoveListener;
import de.jonas.object.unit.WaschbarUser;
import de.jonas.task.ScoreboardUpdateTask;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * Die Haupt- und Main-Klasse dieses Plugins, worin das gesamte Plugin initialisiert wird. Diese Klasse wird vom Server
 * aufgerufen, da sie ein {@link JavaPlugin} darstellt.
 */
public final class WaschbarServer extends JavaPlugin {

    //<editor-fold desc="CONSTANTS">
    /** Der zeitliche Abstand, in dem das Scoreboard eines Spielers konstant aktualisiert wird. */
    private static final int SCOREBOARD_UPDATING_PERIOD = 10;
    //</editor-fold>


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
    private final WaschbarUserHandler userHandler = new WaschbarUserHandler();
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
        prefix = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + ChatColor.BOLD + "Waschbär"
            + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;

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
        pm.registerEvents(new DamageListener(), this);
        pm.registerEvents(new MoveListener(), this);

        // schedule periodic scoreboard updating
        getSLF4JLogger().info("Schedule periodic scoreboard updating.");
        new ScoreboardUpdateTask().runTaskTimer(
            this,
            0,
            SCOREBOARD_UPDATING_PERIOD
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
        this.userHandler.saveAllUsers();

        // kick all players to guarantee a correct restart
        for (@NotNull final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.kickPlayer(
                ChatColor.GRAY.toString() + ChatColor.BOLD + "Du wurdet gekickt, um einen korrekten Neustart zu "
                    + "garantieren. Bitte joine neu!"
            );
        }

        getSLF4JLogger().info("The plugin has been stopped");
    }
    //</editor-fold>

}
