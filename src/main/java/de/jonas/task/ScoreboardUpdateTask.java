package de.jonas.task;

import de.jonas.WaschbarServer;
import de.jonas.handler.scoreboard.ScoreboardHandler;
import de.jonas.object.unit.WaschbarUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

/**
 * Der {@link ScoreboardUpdateTask} stellt eine sich konstant wiederholende Prozedur dar, womit das Scoreboard aller
 * Spieler, die sich momentan auf dem Servernetzwerk befinden, über den {@link ScoreboardHandler} aktualisiert wird.
 */
@NotNull
public final class ScoreboardUpdateTask extends BukkitRunnable {

    //<editor-fold desc="implementation">
    @Override
    public void run() {
        for (@NotNull final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            final WaschbarUser user = WaschbarServer.getInstance().getWaschbarUserHandler().getUser(onlinePlayer).orElseThrow();
            ScoreboardHandler.setScoreboard(user);
        }
    }
    //</editor-fold>

}
