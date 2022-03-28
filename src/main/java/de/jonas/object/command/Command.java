package de.jonas.object.command;

import de.jonas.WaschbarServer;
import de.jonas.handler.command.CommandHandler;
import lombok.SneakyThrows;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.lang.reflect.Method;

/**
 * Ein {@link Command} wird von dem {@link CommandHandler} instanziiert und es wird ein neuer Bukkit-Command erzeugt.
 */
@NotNull
public final class Command {

    //<editor-fold desc="CONSTANTS">
    /** Die Error-Nachricht, die ausgegeben wird, zufalls der Command-Sender kein User ist. */
    @NotNull
    private static final String NO_PLAYER_ERROR = "Du musst ein Spieler sein!";
    /** Die Error-Nachricht, die ausgegeben wird, zufalls der User keine Rechte für den Befehl hat. */
    @NotNull
    private static final String NO_PERMISSIONS_ERROR = "Dazu hast du keine Rechte!";
    //</editor-fold>


    //<editor-fold desc="LOCAL FIELDS">
    /** Der Name bzw die Bezeichnung des Befehls. */
    @NotNull
    private final String commandName;
    /** Die Permission, die für diesen Befehl nötig ist. */
    @NotNull
    private final String permission;
    /** Die minimale Argumenten-Länge, die dieser Befehl haben muss. */
    @Range(from = 0, to = Integer.MAX_VALUE)
    private final int minLength;
    /** Die maximale Argumenten-Länge, die dieser Befehl haben muss. */
    @Range(from = 0, to = Integer.MAX_VALUE)
    private final int maxLength;
    /** Die richtige Benutzung des Befehls. */
    @NotNull
    private final String usage;
    /** Die Methode, die ausgeführt wird, sobald alle formalen Überprüfungen abgeschlossen sind. */
    @NotNull
    private final Method method;
    /** Die Instanz, in der die Methode nach allen formalen Überprüfungen ausgeführt wird. */
    @NotNull
    private final Object object;
    //</editor-fold>


    //<editor-fold desc="CONSTRUCTORS">

    /**
     * Erzeugt einen neuen und vollständig unabhängigen {@link Command}.
     *
     * @param commandName Der Name bzw die Bezeichnung des Befehls.
     * @param permission  Die Permission, die für diesen Befehl nötig ist.
     * @param minLength   Die minimale Argumenten-Länge, die dieser Befehl haben muss.
     * @param maxLength   Die maximale Argumenten-Länge, die dieser Befehl haben muss.
     * @param usage       Die richtige Benutzung des Befehls.
     * @param method      Die Methode, die ausgeführt wird, sobald alle formalen Überprüfungen abgeschlossen sind.
     * @param object      Die Instanz, in der die Methode nach allen formalen Überprüfungen ausgeführt wird.
     */
    public Command(
        @NotNull final String commandName,
        @NotNull final String permission,
        @Range(from = 0, to = Integer.MAX_VALUE) final int minLength,
        @Range(from = 0, to = Integer.MAX_VALUE) final int maxLength,
        @NotNull final String usage,
        @NotNull final Method method,
        @NotNull final Object object
    ) {
        this.commandName = commandName;
        this.permission = permission;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.usage = usage;
        this.method = method;
        this.object = object;
    }
    //</editor-fold>


    //<editor-fold desc="get bukkit command">

    /**
     * Der derzeitige jeweilige Befehl.
     *
     * @return Der derzeitige jeweilige Befehl.
     */
    @NotNull
    public org.bukkit.command.Command getCommand() {
        return new org.bukkit.command.Command(commandName) {
            @Override
            @SneakyThrows
            public boolean execute(
                @NotNull final CommandSender sender,
                @NotNull final String commandLabel,
                @NotNull final String[] args
            ) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(new TextComponent(WaschbarServer.getPrefix() + NO_PLAYER_ERROR));
                    return true;
                }

                final Player player = (Player) sender;

                if (!player.hasPermission(permission)) {
                    player.sendMessage(new TextComponent(WaschbarServer.getPrefix() + NO_PERMISSIONS_ERROR));
                    return true;
                }

                if (!(args.length >= minLength && args.length <= maxLength)) {
                    player.sendMessage(new TextComponent(WaschbarServer.getPrefix() + "Bitte benutze " + usage));
                    return true;
                }

                method.invoke(object, player, args);
                return true;
            }
        };
    }
    //</editor-fold>

}
