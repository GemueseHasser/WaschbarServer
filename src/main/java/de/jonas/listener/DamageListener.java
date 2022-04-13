package de.jonas.listener;

import de.jonas.WaschbarServer;
import de.jonas.object.unit.WaschbarUser;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Mithilfe des {@link DamageListener} wird aller Schaden verarbeitet, der einer Entität zugefügt wird.
 */
@NotNull
public final class DamageListener implements Listener {

    //<editor-fold desc="implementation">
    @EventHandler
    public void onDamage(@NotNull final EntityDamageEvent e) {
        // check if entity is a player
        if (e.getEntityType() != EntityType.PLAYER) return;

        // get user
        final Player player = (Player) e.getEntity();
        final WaschbarUser user = WaschbarServer.getInstance().getUserHandler().getUser(player).orElseThrow();

        // check if user is in god mode
        if (!user.getTrollProfile().isGod()) return;

        // cancel event
        e.setCancelled(true);
    }
    //</editor-fold>

}
