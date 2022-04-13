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

/**
 * Es werden alle Befehle implementiert, die ein normaler Nutzer ausführen kann. Ein Befehl wird mit der {@link
 * WaschbarCommand Annotation} eingeleitet, welche für den {@link de.jonas.handler.command.CommandHandler} benötigt
 * wird. Die Befehle in dieser Klasse können nur mithilfe eines {@link de.jonas.handler.command.CommandHandler}
 * registriert werden, da die formalen Merkmale des Befehls so übergeben werden, dass nur der Handler damit weiter
 * arbeiten kann, da jeder Befehl nur auf der {@link WaschbarCommand Annotation} basiert.
 */
@NotNull
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

        // check if player sends a message to himself
        if (target.equals(player)) {
            player.sendMessage(TextComponent.fromLegacyText(
                WaschbarServer.getPrefix() + "Dir selber musst du keine Nachricht schreiben ;)"
            ));

            return;
        }

        final WaschbarUser user = WaschbarServer.getInstance().getUserHandler().getUser(player).orElseThrow();

        // send message
        sendMessage(user, target, args);
    }

    /**
     * Mit diesem Befehl kann ein Spieler einem anderen Spieler eine private Nachricht senden, mit dem er bereits
     * geschrieben hat. Somit muss er nicht wieder den Namen des Spielers eingeben, mit dem er zuletzt geschrieben hat.
     *
     * @param player Der Spieler, der diesen Befehl ausführt.
     * @param args   Die Argumente, die der Spieler zusätzlich zu dem Befehl eingibt.
     */
    @WaschbarCommand(
        command = "r",
        maxLength = Integer.MAX_VALUE,
        usage = "/r <message>"
    )
    public void returnMsg(
        @NotNull final Player player,
        @NotNull final String[] args
    ) {
        final WaschbarUser user = WaschbarServer.getInstance().getUserHandler().getUser(player).orElseThrow();

        // check if user already has a conversation
        if (user.getLastConversation() == null) {
            player.sendMessage(TextComponent.fromLegacyText(
                WaschbarServer.getPrefix() + "Du hast bisher mit niemandem geschrieben, dem du antworten könntest."
            ));

            return;
        }

        // send message
        sendMessage(user, user.getLastConversation(), args);
    }

    /**
     * Sendet eine Nachricht, die als Array abgespeichert ist (Wort für Wort) von einem Spieler zu einem anderen.
     *
     * @param user   Der Nutzer, der die Nachricht sendet.
     * @param target Der Spieler, der die Nachricht empfangen soll.
     * @param args   Die Nachricht, die Wort für Wort als Array abgespeichert wird.
     */
    private void sendMessage(
        @NotNull final WaschbarUser user,
        @NotNull final Player target,
        @NotNull final String[] args
    ) {
        // check if target is online
        if (!target.isOnline()) {
            user.getPlayer().sendMessage(
                new ComponentBuilder(WaschbarServer.getPrefix())
                    .append(
                        " " + PLAYER_NOT_ONLINE,
                        NONE
                    ).color(ChatColor.GRAY)
                    .create()
            );

            return;
        }

        final StringBuilder message = new StringBuilder();

        for (int i = 1; i < args.length; i++) {
            message.append(args[i]).append(" ");
        }

        final WaschbarUser targetUser = WaschbarServer.getInstance().getUserHandler().getUser(target).orElseThrow();

        // set last conversation
        user.setLastConversation(target);
        targetUser.setLastConversation(user.getPlayer());

        // send message
        user.getPlayer().sendMessage(TextComponent.fromLegacyText(
            user.getWholeCustomName() + ChatColor.DARK_GRAY + " -> " + targetUser.getWholeCustomName()
                + ChatColor.DARK_GRAY + " : " + message
        ));

        target.sendMessage(TextComponent.fromLegacyText(
            user.getWholeCustomName() + ChatColor.DARK_GRAY + " -> " + targetUser.getWholeCustomName()
                + ChatColor.DARK_GRAY + " : " + message
        ));
    }
    //</editor-fold>

}
