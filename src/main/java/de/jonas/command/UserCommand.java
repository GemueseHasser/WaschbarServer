package de.jonas.command;

import de.jonas.WaschbarServer;
import de.jonas.object.command.WaschbarCommand;
import de.jonas.object.unit.WaschbarUser;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention.NONE;

public final class UserCommand {

    //<editor-fold desc="CONSTANTS">
    /** Die Nachricht, die einem User gesendet wird, wenn der eingegebene andere User nicht online ist. */
    @NotNull
    private static final String PLAYER_NOT_ONLINE = "Der Spieler ist nicht auf diesem Netwerk online!";
    //</editor-fold>


    //<editor-fold desc="command: msg">

    /**
     * Mit diesem Befehl kann ein Spieler einem anderen Spieler eine private Nachricht senden.
     *
     * @param player Der Spieler, der diesen Befehl ausführt.
     * @param args   Die Argumente, die der Spieler zusätzlich zu dem Befehl eingibt.
     */
    @WaschbarCommand(
        command = "msg",
        minLength = 1,
        maxLength = Integer.MAX_VALUE,
        usage = "/msg <player> <message>",
        aliases = {"tell", "message"}
    )
    public void msg(
        @NotNull final Player player,
        @NotNull final String[] args
    ) {
        final Player target = Bukkit.getPlayer(args[0]);

        if (target == null || !target.isOnline()) {
            player.sendMessage(
                new ComponentBuilder(WaschbarServer.getPrefix())
                    .append(
                        " " + PLAYER_NOT_ONLINE,
                        NONE
                    ).color(ChatColor.GRAY)
                    .create()
            );

            return;
        }

        if (target.equals(player)) {
            player.sendMessage(TextComponent.fromLegacyText(
                WaschbarServer.getPrefix() + "Dir selber musst du keine Nachricht schreiben ;)"
            ));

            return;
        }

        final StringBuilder message = new StringBuilder();

        for (int i = 1; i < args.length; i++) {
            message.append(args[i]).append(" ");
        }

        final WaschbarUser user = WaschbarServer.getInstance().getWaschbarUserHandler().getUser(player).orElseThrow();
        final WaschbarUser targetUser = WaschbarServer.getInstance().getWaschbarUserHandler().getUser(target).orElseThrow();

        player.sendMessage(TextComponent.fromLegacyText(
            user.getWholeCustomName() + ChatColor.DARK_GRAY + " -> " + targetUser.getWholeCustomName() + ChatColor.DARK_GRAY
                + " : " + message
        ));

        target.sendMessage(TextComponent.fromLegacyText(
            user.getWholeCustomName() + ChatColor.DARK_GRAY + " -> " + targetUser.getWholeCustomName() + ChatColor.DARK_GRAY
                + " : " + message
        ));
    }
    //</editor-fold>

}
