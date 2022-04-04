package de.jonas.object.unit;

import de.jonas.handler.util.ConfigurationHandler;
import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention.NONE;

/**
 * Ein {@link WaschbarUser} stellt ein User-Objekt dar, welches für jeden Spieler instanziiert wird, der sich auf diesem
 * Server befindet. Ein {@link WaschbarUser} basiert auf einem normalen Spieler und erweitert diesen.
 */
@NotNull
public final class WaschbarUser {

    //<editor-fold desc="CONSTANTS">
    /** Der Footer der Tablist, der konstant für jeden Spieler gesetzt wird. */
    @NotNull
    private static final BaseComponent[] PLAYER_LIST_FOOTER =
        new ComponentBuilder(
            "\n\n"
        )
            .append(
                "Der Server der ",
                NONE
            ).color(net.md_5.bungee.api.ChatColor.GRAY)
            .append(
                "Waschbären",
                NONE
            ).color(net.md_5.bungee.api.ChatColor.DARK_AQUA).bold(true)
            .append(
                "!",
                NONE
            ).color(net.md_5.bungee.api.ChatColor.GRAY)
            .append(
                "\n\n",
                NONE
            )
            .create();
    //</editor-fold>


    //<editor-fold desc="LOCAL FIELDS">
    /** Der Spieler, auf dem dieser {@link WaschbarUser} basiert. */
    @Getter
    @NotNull
    private final Player player;
    /** Der Name, aus dem der gesamte CustomName besteht, jedoch nur der Name an sich. */
    @Getter
    private String customName;
    /** Der Name, der auf jeden Spieler angepasst wird und der auch überall angezeigt wird. */
    @Getter
    private String wholeCustomName;
    /** Die Blöcke, die ein Spieler auf diesem Server gebaut hat. */
    @Getter
    private int builtBlocks;
    //</editor-fold>


    //<editor-fold desc="CONSTRUCTORS">

    /**
     * Erzeugt eine neue und vollständig unabhängige Instanz eines {@link WaschbarUser} mithilfe eines {@link Player
     * Spielers}. Ein {@link WaschbarUser} stellt ein User-Objekt dar, welches für jeden Spieler instanziiert wird, der
     * sich auf diesem * Server befindet. Ein {@link WaschbarUser} basiert auf einem normalen Spieler und erweitert
     * diesen.
     *
     * @param player Der Spieler, der die Grundlage für diesen {@link WaschbarUser} bildet.
     */
    public WaschbarUser(@NotNull final Player player) {
        this.player = player;

        // set player name
        setName();

        final Object configBlocks = ConfigurationHandler.getConfiguration(
            "builtBlocks.yml",
            "blocks." + player.getUniqueId().toString()
        );

        if (configBlocks == null) return;

        // load built blocks from config
        this.builtBlocks = (int) configBlocks;
    }
    //</editor-fold>


    /**
     * Lädt alle grafischen Anzeigen für den Nutzer und die mit dem Nutzer zusammenhängen.
     */
    public void loadDisplay() {
        // set custom name
        this.player.setDisplayName(this.customName);
        this.player.setPlayerListName(this.customName);

        // load tablist
        this.player.setPlayerListHeaderFooter(
            new ComponentBuilder(
                "\n\n"
            ).color(net.md_5.bungee.api.ChatColor.GRAY)
                .append(
                    "Willkommen, ",
                    NONE
                ).color(net.md_5.bungee.api.ChatColor.GRAY)
                .append(
                    this.customName,
                    NONE
                ).bold(true)
                .append(
                    "\n\n",
                    NONE
                )
                .create(),
            PLAYER_LIST_FOOTER
        );
    }

    /**
     * Speichert alle Daten dieses Nutzers ab.
     */
    public void saveUser() {
        // save built blocks
        ConfigurationHandler.setConfiguration(
            "builtBlocks.yml",
            "blocks." + player.getUniqueId().toString(),
            this.builtBlocks
        );
    }

    /**
     * Nickt einen Spieler mithilfe eines bestimmten Namen.
     *
     * @param name Der Name, mit dem der Spieler genickt werden soll.
     */
    public void nick(@NotNull final String name) {
        this.player.setDisplayName(name);
        setName();
        this.player.setPlayerListName(this.customName);
    }

    /**
     * Setzt den Namen des Nutzers wieder auf seinen ursprünglichen, richtigen Namen.
     */
    public void unnick() {
        this.player.setDisplayName(this.player.getName());
        setName();
        this.player.setPlayerListName(this.customName);
    }

    /**
     * Erhöht die Anzahl an gebauten Blöcken dieses Nutzers um 1.
     */
    public void incrementBuiltBlocks() {
        this.builtBlocks += 1;
    }

    /**
     * Reduziert die Anzahl an gebauten Blöcken dieses Nutzers um 1.
     */
    public void reduceBuiltBlocks() {
        this.builtBlocks -= 1;
    }

    /**
     * Setzt den Namen des Nutzers.
     */
    private void setName() {
        if (player.isOp()) {
            this.customName = ChatColor.DARK_RED.toString() + ChatColor.BOLD + player.getDisplayName();
        } else {
            this.customName = ChatColor.GRAY.toString() + ChatColor.BOLD + player.getDisplayName();
        }

        if (player.isOp()) {
            this.wholeCustomName = ChatColor.RED.toString() + ChatColor.BOLD + "[" + this.customName
                + ChatColor.RED + ChatColor.BOLD + "]";
        } else {
            this.wholeCustomName = ChatColor.DARK_GRAY + "[" + this.customName + ChatColor.DARK_GRAY + "]";
        }
    }

}
