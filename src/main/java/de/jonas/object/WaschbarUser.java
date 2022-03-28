package de.jonas.object;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Ein {@link WaschbarUser} stellt ein User-Objekt dar, welches für jeden Spieler instanziiert wird, der sich auf diesem
 * Server befindet. Ein {@link WaschbarUser} basiert auf einem normalen Spieler und erweitert diesen.
 */
@NotNull
public final class WaschbarUser {

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

}
