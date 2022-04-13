package de.jonas.listener;

import de.jonas.WaschbarServer;
import de.jonas.object.unit.WaschbarUser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Mithilfe des {@link MoveListener} wird jede Bewegung eines Spielers erfasst und verarbeitet.
 */
@NotNull
public final class MoveListener implements Listener {

    //<editor-fold desc="implementation">
    @EventHandler
    public void onMove(@NotNull final PlayerMoveEvent e) {
        // get user
        final Player player = e.getPlayer();
        final WaschbarUser user = WaschbarServer.getInstance().getUserHandler().getUser(player).orElseThrow();

        // check if user is frozen
        if (!user.getTrollProfile().isFreeze()) return;

        // cancel event
        e.setCancelled(true);
    }
    //</editor-fold>

}
