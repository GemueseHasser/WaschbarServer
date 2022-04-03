package de.jonas.listener;

import de.jonas.WaschbarServer;
import de.jonas.object.unit.WaschbarUser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Mithilfe des {@link BlockPlaceBreakListener} wird das Platzieren und das Abbauen eines Blocks gehandhabt. Sobald also
 * ein Block platziert oder abgebaut wird, wird eine bestimmte Aktion ausgeführt.
 */
public final class BlockPlaceBreakListener implements Listener {

    //<editor-fold desc="implementation">
    @EventHandler
    public void onBlockBreak(@NotNull final BlockBreakEvent e) {
        final WaschbarUser user = WaschbarServer.getInstance().getWaschbarUserHandler().getUser(e.getPlayer()).orElseThrow();
        user.reduceBuiltBlocks();
    }

    @EventHandler
    public void onBlockPlace(@NotNull final BlockPlaceEvent e) {
        final WaschbarUser user = WaschbarServer.getInstance().getWaschbarUserHandler().getUser(e.getPlayer()).orElseThrow();
        user.incrementBuiltBlocks();
    }
    //</editor-fold>

}
