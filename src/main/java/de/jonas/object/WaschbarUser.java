package de.jonas.object;

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
            "Der Server der "
        ).color(net.md_5.bungee.api.ChatColor.DARK_GRAY)
            .append(
                "Waschbären",
                NONE
            ).color(net.md_5.bungee.api.ChatColor.DARK_AQUA).bold(true)
            .append(
                "!",
                NONE
            ).color(net.md_5.bungee.api.ChatColor.DARK_GRAY)
            .create();
    //</editor-fold>


    //<editor-fold desc="LOCAL FIELDS">
    /** Der Spieler, auf dem dieser {@link WaschbarUser} basiert. */
    @Getter
    @NotNull
    private final Player player;
    /** Der Name, der auf jeden Spieler angepasst wird und der auch überall angezeigt wird. */
    @Getter
    @NotNull
    private final String customName;
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

        if (player.isOp()) {
            this.customName = ChatColor.RED.toString() + ChatColor.BOLD + "["
                + ChatColor.DARK_RED + ChatColor.BOLD + player.getDisplayName() + ChatColor.RED + ChatColor.BOLD + "[";
        } else {
            this.customName = ChatColor.GRAY + "[" + ChatColor.BOLD + player.getDisplayName() + ChatColor.GRAY + "]";
        }
    }
    //</editor-fold>


    /**
     * Lädt alle grafischen Anzeigen für den Nutzer und die mit dem Nutzer zusammenhängen.
     */
    public void loadDisplay() {
        // set custom name
        player.setDisplayName(this.customName);
        player.setPlayerListName(this.customName);

        // load tablist
        player.setPlayerListHeaderFooter(
            new ComponentBuilder(
                "Willkommen, "
            ).color(net.md_5.bungee.api.ChatColor.GRAY)
                .append(
                    this.customName,
                    NONE
                ).bold(true)
                .create(),
            PLAYER_LIST_FOOTER
        );
    }

}
