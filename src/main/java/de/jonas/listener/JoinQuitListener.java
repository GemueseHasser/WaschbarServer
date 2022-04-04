package de.jonas.listener;

import de.jonas.WaschbarServer;
import de.jonas.object.unit.WaschbarUser;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachment;
import org.jetbrains.annotations.NotNull;

/**
 * Mithilfe eines {@link JoinQuitListener} werden alle Aktionen geregelt, die ausgeführt werden sollen, wenn ein Spieler
 * den Server betritt, aber auch wenn ein Spieler den Server wieder verlässt.
 */
public final class JoinQuitListener implements Listener {

    //<editor-fold desc="join">
    @EventHandler
    public void onJoin(@NotNull final PlayerJoinEvent e) {
        // set default permission
        final PermissionAttachment attachment = e.getPlayer().addAttachment(WaschbarServer.getInstance());
        attachment.setPermission("waschbar.default", true);

        WaschbarServer.getInstance().getWaschbarUserHandler().join(e.getPlayer());
        final WaschbarUser user = WaschbarServer.getInstance().getWaschbarUserHandler().getUser(e.getPlayer()).orElseThrow();

        // load display
        user.loadDisplay();

        // send join message
        e.setJoinMessage(
            ChatColor.DARK_GRAY + "Der Waschbär " + user.getCustomName() + ChatColor.DARK_GRAY + " hat den Server betreten."
        );
    }
    //</editor-fold>

    //<editor-fold desc="quit">
    @EventHandler
    public void onQuit(@NotNull final PlayerQuitEvent e) {
        final WaschbarUser user = WaschbarServer.getInstance().getWaschbarUserHandler().getUser(e.getPlayer()).orElseThrow();

        user.saveUser();

        e.setQuitMessage(
            ChatColor.DARK_GRAY + "Der Waschbär " + user.getCustomName() + ChatColor.DARK_GRAY + " hat den Server verlassen."
        );

        WaschbarServer.getInstance().getWaschbarUserHandler().quit(e.getPlayer());
    }
    //</editor-fold>

}
