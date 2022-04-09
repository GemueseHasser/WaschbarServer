package de.jonas.task;

import de.jonas.WaschbarServer;
import de.jonas.object.unit.WaschbarUser;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

/**
 * Der {@link UserUpdateTask} stellt eine sich konstant wiederholende Prozedur dar, womit jedes Nutzerobjekt, welches
 * sich aktuell auf dem Servernetzwerk befindet, individuell aktualisiert wird.
 */
@NotNull
public final class UserUpdateTask extends BukkitRunnable {

    //<editor-fold desc="implementation">
    @Override
    public void run() {
        for (@NotNull final WaschbarUser user : WaschbarServer.getInstance().getWaschbarUserHandler().getOnlineUsers()) {
            if (!user.getTrollProfile().isKnockback()) continue;

            user.getPlayer().setVelocity(new Vector());
        }
    }
    //</editor-fold>

}
