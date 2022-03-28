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
 * Es werden alle Befehle implementiert, die ein Administrator ausführen kann. Ein Befehl wird mit der {@link
 * WaschbarCommand Annotation} eingeleitet, welche für den {@link de.jonas.handler.command.CommandHandler} benötigt
 * wird. Die Befehle in dieser Klasse können nur mithilfe eines {@link de.jonas.handler.command.CommandHandler}
 * registriert werden, da die formalen Merkmale des Befehls so übergeben werden, dass nur der Handler damit weiter
 * arbeiten kann, da jeder Befehl nur auf der {@link WaschbarCommand Annotation} basiert.
 */
@NotNull
public final class AdminCommand {

    //<editor-fold desc="command: nick">
    /**
     * Mit diesem Befehl kann sich ein Spieler einen Nickname geben oder kann einem anderen Spieler einen Nickname
     * geben.
     *
     * @param player Der Spieler, der diesen Befehl ausführt.
     * @param args   Die Argumente, die der Spieler zusätzlich zu dem Befehl eingibt.
     */
    @WaschbarCommand(
        command = "nick",
        minLength = 1,
        maxLength = 2,
        permission = "waschbar.nick",
        usage = "/nick <name> | /nick <player> <name>"
    )
    public void nick(
        @NotNull final Player player,
        @NotNull final String[] args
    ) {
        if (args.length == 1) {
            nickUser(player, args[0]);

            player.sendMessage(TextComponent.fromLegacyText(
                WaschbarServer.getPrefix() + "Du hast dich mit dem Namen " + args[0] + " genickt."
            ));

            return;
        }

        final Player target = Bukkit.getPlayer(args[0]);

        if (target == null || !target.isOnline()) {
            player.sendMessage(
                new ComponentBuilder(WaschbarServer.getPrefix())
                    .append(
                        " Der Spieler ist nicht online!",
                        NONE
                    ).color(ChatColor.GRAY)
                    .create()
            );

            return;
        }

        nickUser(target, args[1]);

        player.sendMessage(TextComponent.fromLegacyText(
            WaschbarServer.getPrefix() + "Du hast den Spieler " + target.getName() + " als " + args[1] + " genickt."
        ));
    }

    /**
     * Mit diesem Befehl kann ein Spieler seinen Namen zurücksetzen, wenn er sich vorher einen Nickname gegeben hat.
     * Zudem hat er auch die Möglichkeit den Nickname eines anderen Spielers zurückzusetzen.
     *
     * @param player Der Spieler, der diesen Befehl ausführt.
     * @param args   Die Argumente, die der Spieler zusätzlich zu dem Befehl eingibt.
     */
    @WaschbarCommand(
        command = "unnick",
        maxLength = 1,
        permission = "waschbar.nick",
        usage = "/unnick | /unnick <player>"
    )
    public void unnick(
        @NotNull final Player player,
        @NotNull final String[] args
    ) {
        if (args.length == 0) {
            final WaschbarUser user = WaschbarServer.getInstance().getWaschbarUserHandler().getUser(player).orElseThrow();
            user.unnick();

            player.sendMessage(TextComponent.fromLegacyText(
                WaschbarServer.getPrefix() + "Du hast deinen Namen zurückgesetzt."
            ));

            return;
        }

        final Player target = Bukkit.getPlayer(args[0]);

        if (target == null || !target.isOnline()) {
            player.sendMessage(
                new ComponentBuilder(WaschbarServer.getPrefix())
                    .append(
                        " Der Spieler ist nicht online!",
                        NONE
                    ).color(ChatColor.GRAY)
                    .create()
            );

            return;
        }

        final WaschbarUser user = WaschbarServer.getInstance().getWaschbarUserHandler().getUser(target).orElseThrow();
        user.unnick();

        player.sendMessage(TextComponent.fromLegacyText(
            WaschbarServer.getPrefix() + "Du hast den Namen des Spielers " + args[0] + " zurückgesetzt."
        ));
    }

    /**
     * Nickt einen Nutzer mit einem bestimmten Namen, sofern der Name maximal 15 Zeichen beinhaltet.
     *
     * @param player Der Spieler, der genickt werden soll.
     * @param name   Der Name mit dem der Spieler genickt werden soll.
     */
    private void nickUser(
        @NotNull final Player player,
        @NotNull final String name
    ) {
        final WaschbarUser user = WaschbarServer.getInstance().getWaschbarUserHandler().getUser(player).orElseThrow();

        if (name.length() > 15) {
            player.sendMessage(
                new ComponentBuilder(WaschbarServer.getPrefix())
                    .append(
                        " Bitte wähle einen kürzeren Namen!",
                        NONE
                    ).color(ChatColor.GRAY)
                    .create()
            );

            return;
        }

        user.nick(name);
    }
    //</editor-fold>

}
