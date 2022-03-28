package de.jonas.listener;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Mithilfe eines {@link JoinListener} werden alle Aktionen geregelt, die ausgeführt werden sollen, wenn ein Spieler den
 * Server betritt.
 */
public final class JoinListener implements Listener {

    //<editor-fold desc="implementation">
    @EventHandler
    public void onJoin(@NotNull final PlayerJoinEvent e) {
        e.setJoinMessage(
            ChatColor.DARK_GRAY + "Der Spieler " + ChatColor.YELLOW + ChatColor.BOLD + e.getPlayer().getName()
                + ChatColor.DARK_GRAY + " hat den Server betreten."
        );
    }
    //</editor-fold>

}
