package de.jonas.listener;

import de.jonas.WaschbarServer;
import de.jonas.object.unit.WaschbarUser;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Mithilfe des {@link ChatListener} wird das Chat-Verhalten aller Nutzer geregelt.
 */
@NotNull
public final class ChatListener implements Listener {

    //<editor-fold desc="implementation">
    @EventHandler
    public void onChat(@NotNull final AsyncPlayerChatEvent e) {
        // get user
        final WaschbarUser user = WaschbarServer.getInstance().getUserHandler().getUser(e.getPlayer()).orElseThrow();

        // create custom message from message
        final String message = e.getMessage()
            .replaceAll("<3", "❤")
            .replaceAll("#penis", "╰⋃╯");

        // check if player is op
        if (e.getPlayer().isOp()) {
            e.setFormat(user.getWholeCustomName() + ChatColor.GRAY + " > " + ChatColor.WHITE
                + ChatColor.translateAlternateColorCodes('&', message));
        } else {
            e.setFormat(user.getWholeCustomName() + ChatColor.GRAY + " > " + ChatColor.WHITE + message);
        }
    }
    //</editor-fold>

}
