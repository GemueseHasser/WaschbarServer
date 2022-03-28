package de.jonas.handler;

import de.jonas.object.WaschbarUser;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Mithilfe eines {@link WaschbarUserHandler} können beliebig viele {@link WaschbarUser} verarbeitet werden. Es können
 * User hinzugefügt, entfernt und herausgefiltert werden.
 */
@NotNull
public final class WaschbarUserHandler {

    //<editor-fold desc="LOCAL FIELDS">
    /** Eine Liste aller {@link WaschbarUser}, welche momentan in diesem Handler registriert sind. */
    @NotNull
    private final List<WaschbarUser> onlineUsers = new ArrayList<>();
    //</editor-fold>


    /**
     * Lässt einen bestimmten {@link Player Spieler} diesen {@link WaschbarUserHandler} joinen.
     *
     * @param player Der Spieler, der diesem Handler joinen soll.
     */
    public void join(@NotNull final Player player) {
        for (@NotNull final WaschbarUser user : this.onlineUsers) {
            if (!user.getPlayer().equals(player)) continue;

            this.onlineUsers.remove(user);
        }

        this.onlineUsers.add(new WaschbarUser(player));
    }

    /**
     * Entfernt einen bestimmten {@link Player Spieler} wieder aus diesem {@link WaschbarUserHandler}.
     *
     * @param player Der Spieler, der aus diesem Handler entfernt werden soll.
     */
    public void quit(@NotNull final Player player) {
        final WaschbarUser user = getUser(player).orElseThrow();

        this.onlineUsers.remove(user);
    }

    /**
     * Gibt einen {@link WaschbarUser} zurück, der auf einem bestimmten Spieler basiert, zufalls dieser in diesem
     * Handler registriert ist.
     *
     * @param player Der Spieler, auf dessen Basis der {@link WaschbarUser} herausgefiltert werden soll.
     *
     * @return Einen {@link WaschbarUser}, welcher auf der Basis eines bestimmten Spielers aus diesem Handler
     *     herausgefiltert wird, zufalls dieser Spieler in diesem Handler bereits registriert ist.
     */
    @NotNull
    public Optional<WaschbarUser> getUser(@NotNull final Player player) {
        for (@NotNull final WaschbarUser user : this.onlineUsers) {
            if (!user.getPlayer().equals(player)) continue;

            return Optional.of(user);
        }

        return Optional.empty();
    }

}
