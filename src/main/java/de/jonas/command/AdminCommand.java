package de.jonas.command;

import de.jonas.WaschbarServer;
import de.jonas.object.command.WaschbarCommand;
import de.jonas.object.unit.WaschbarUser;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutExplosion;
import net.minecraft.server.v1_12_R1.Vec3D;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Collections;

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

    //<editor-fold desc="CONSTANTS">
    /** Die Nachricht, die einem User gesendet wird, wenn der eingegebene andere User nicht online ist. */
    @NotNull
    private static final String PLAYER_NOT_ONLINE = "Der Spieler ist nicht auf diesem Netwerk online!";
    /** Die Größe des Clears, wenn der Chat gecleared werden soll. */
    private static final int CHAT_CLEAR_SIZE = 100;

    //<editor-fold desc="command: gamemode">
    /** Die Nummer für Gamemode Überleben. */
    private static final int GAMEMODE_SURVIVAL = 0;
    /** Die Nummer für Gamemode Kreativ. */
    private static final int GAMEMODE_CREATIVE = 1;
    /** Die Nummer für Gamemode Abenteuer. */
    private static final int GAMEMODE_ADVENTURE = 2;
    /** Die Nummer für Gamemode Zuschauer. */
    private static final int GAMEMODE_SPECTATOR = 3;
    //</editor-fold>

    //<editor-fold desc="command: speed">
    /** Die minimale Geschwindigkeit, auf die man seine Geschwindigkeit setzen kann. */
    private static final int MIN_SPEED = 1;
    /** Die maximale Geschwindigkeit, auf die man seine Geschwindigkeit setzen kann. */
    private static final int MAX_SPEED = 10;
    /** Die standard Geschwindigkeit beim Laufen. */
    private static final float DEFAULT_WALK_SPEED = 0.2F;
    /** Die standard Geschwindigkeit beim Fliegen. */
    private static final float DEFAULT_FLY_SPEED = 0.1F;
    /** Der Divisor, durch den die Geschwindigkeit beim Setzen geteilt wird. */
    private static final int DIVISOR = 10;
    //</editor-fold>

    //</editor-fold>

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
                        " " + PLAYER_NOT_ONLINE,
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
                        " " + PLAYER_NOT_ONLINE,
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

    //<editor-fold desc="command: gamemode">

    /**
     * Es wird der Gamemode-Befehl implementiert, womit man den Gamemode von sich selbst oder von jemandem anderen
     * editieren kann.
     *
     * @param player Der Spieler, der diesen Befehl ausführt.
     * @param args   Die Argumente, die der Spieler zusätzlich zu dem Befehl eingibt.
     */
    @WaschbarCommand(
        command = "gamemode",
        minLength = 1,
        maxLength = 2,
        permission = "waschbar.gamemode",
        usage = "/gamemode <gamemode> [<player>]",
        aliases = {"gm"}
    )
    public void gamemode(
        @NotNull final Player player,
        @NotNull final String[] args
    ) {
        final boolean isSame = args.length == 1;
        final Player target = isSame ? player : Bukkit.getPlayer(args[1]);

        if (target == null || !target.isOnline()) {
            player.sendMessage(TextComponent.fromLegacyText(
                WaschbarServer.getPrefix() + " " + PLAYER_NOT_ONLINE
            ));
            return;
        }

        switch (args[0]) {
            case "survival":
            case "überleben":
            case GAMEMODE_SURVIVAL + "":
                target.setGameMode(GameMode.SURVIVAL);
                player.sendMessage(TextComponent.fromLegacyText(
                    WaschbarServer.getPrefix() + getGamemodeMessage(GAMEMODE_SURVIVAL, isSame, target.getName())
                ));
                break;

            case "creative":
            case "kreativ":
            case GAMEMODE_CREATIVE + "":
                target.setGameMode(GameMode.CREATIVE);
                player.sendMessage(TextComponent.fromLegacyText(
                    WaschbarServer.getPrefix() + getGamemodeMessage(GAMEMODE_CREATIVE, isSame, target.getName())
                ));
                break;

            case "adventure":
            case "abenteuer":
            case GAMEMODE_ADVENTURE + "":
                target.setGameMode(GameMode.ADVENTURE);
                player.sendMessage(TextComponent.fromLegacyText(
                    WaschbarServer.getPrefix() + getGamemodeMessage(GAMEMODE_ADVENTURE, isSame, target.getName())
                ));
                break;

            case "spectator":
            case "zuschauer":
            case GAMEMODE_SPECTATOR + "":
                target.setGameMode(GameMode.SPECTATOR);
                player.sendMessage(TextComponent.fromLegacyText(
                    WaschbarServer.getPrefix() + getGamemodeMessage(GAMEMODE_SPECTATOR, isSame, target.getName())
                ));
                break;

            default:
                player.sendMessage(TextComponent.fromLegacyText(
                    WaschbarServer.getPrefix() + "Bitte benutze /gamemode <1|2|3>"
                ));
                break;
        }
    }

    /**
     * Gibt die Nachricht zurück, die ausgegeben wird, wenn der Gamemode-Befehl erfolgreich ausgeführt wurde.
     *
     * @param gm   Der Gamemode, welcher gewählt wurde.
     * @param same Der Zustand, ob der Spieler selbst den Gamemode gewechselt hat, oder ob der Spieler den Gamemode
     *             eines anderen Spielers gewechselt hat.
     * @param name Der Name des Spielers, dessen Gamemode gewechselt wurde.
     *
     * @return Die Nachricht, die ausgegeben wird, wenn der Befehl erfolgreich ausgeführt wurde.
     */
    private String getGamemodeMessage(
        @Range(from = 0, to = Integer.MAX_VALUE) final int gm,
        final boolean same,
        @Nullable final String name
    ) {
        final String gamemode = gm == 0 ? "Überleben" : gm == 1 ? "Kreativ" : gm == 2 ? "Abenteuer" : "Zuschauer";
        return (same ? "Du wurdest " : "Der Spieler " + name + " wurde ")
            + "in Gamemode " + ChatColor.RED + gamemode + ChatColor.GRAY + " versetzt!";
    }
    //</editor-fold>

    //<editor-fold desc="command: speed">

    /**
     * Es wird der Befehl implementiert, womit man seine Geschwindigkeit editieren kann.
     *
     * @param player Der Spieler, der diesen Befehl ausführt.
     * @param args   Die Argumente, die der Spieler zusätzlich zu dem Befehl eingibt.
     */
    @WaschbarCommand(
        command = "speed",
        minLength = 1,
        maxLength = 1,
        permission = "waschbar.speed",
        usage = "/speed <1-10>"
    )
    public void speed(
        @NotNull final Player player,
        @NotNull final String[] args
    ) {
        if (args[0].equalsIgnoreCase("default") || args[0].equalsIgnoreCase("d")) {
            player.setWalkSpeed(DEFAULT_WALK_SPEED);
            player.setFlySpeed(DEFAULT_FLY_SPEED);

            player.sendMessage(TextComponent.fromLegacyText(
                WaschbarServer.getPrefix() + "Deine Geschwindigkeit wurde wieder normalisiert!"
            ));
            return;
        }
        // declare speed
        final int speed;

        // try to set speed from arguments
        try {
            speed = Integer.parseInt(args[0]);
        } catch (@NotNull final IllegalArgumentException ignored) {
            player.sendMessage(TextComponent.fromLegacyText(
                WaschbarServer.getPrefix() + "Bitte wähle eine Geschwindigkeit zwischen 1 und 10!"
            ));
            return;
        }

        // check if speed is correct
        if (speed < MIN_SPEED || speed > MAX_SPEED) {
            player.sendMessage(TextComponent.fromLegacyText(
                WaschbarServer.getPrefix() + "Bitte wähle eine Geschwindigkeit zwischen 1 und 10!"
            ));
            return;
        }

        // set players speed
        player.setFlySpeed((float) speed / DIVISOR);
        player.setWalkSpeed((float) speed / DIVISOR);

        player.sendMessage(TextComponent.fromLegacyText(
            WaschbarServer.getPrefix() + "Deine Geschwindigkeit wurde auf " + speed + " geändert!"
        ));
    }
    //</editor-fold>

    //<editor-fold desc="command: chatclear">

    /**
     * Es wird der Befehl implementiert, womit man seine Geschwindigkeit editieren kann.
     *
     * @param player Der Spieler, der diesen Befehl ausführt.
     * @param args   Die Argumente, die der Spieler zusätzlich zu dem Befehl eingibt.
     */
    @WaschbarCommand(
        command = "chatclear",
        permission = "waschbar.cc",
        usage = "/chatclear | /cc",
        aliases = {"cc"}
    )
    public void clearChat(
        @NotNull final Player player,
        @NotNull final String[] args
    ) {
        for (int i = 0; i < CHAT_CLEAR_SIZE; i++) {
            for (@NotNull final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.sendMessage(TextComponent.fromLegacyText(""));
            }
        }
    }
    //</editor-fold>

    //<editor-fold desc="command: troll">

    /**
     * Dieser Troll Befehl umfasst mehrere Trolls, die durch die entsprechenden Argumente eingeleitet werden.
     *
     * @param player Der Spieler, der diesen Befehl ausführt.
     * @param args   Die Argumente, die der Spieler zusätzlich zu dem Befehl eingibt.
     */
    @WaschbarCommand(
        command = "troll",
        permission = "waschbar.troll",
        usage = "/troll help",
        maxLength = 2
    )
    public void troll(
        @NotNull final Player player,
        @NotNull final String[] args
    ) {
        final WaschbarUser user = WaschbarServer.getInstance().getWaschbarUserHandler().getUser(player).orElseThrow();

        if (args.length == 0) {
            if (user.getTrollProfile().isTroller()) {
                player.sendMessage(TextComponent.fromLegacyText(
                    WaschbarServer.getPrefix() + "Du bist nun kein Troller mehr und sichtbar!"
                ));

                user.getTrollProfile().setVanish(false);
            } else {
                player.sendMessage(TextComponent.fromLegacyText(
                    WaschbarServer.getPrefix() + "Du bist nun ein Troller und unsichtbar!"
                ));

                user.getTrollProfile().setVanish(true);
            }

            user.getTrollProfile().setTroller(!user.getTrollProfile().isTroller());
            return;
        }

        if (!user.getTrollProfile().isTroller()) {
            player.sendMessage(TextComponent.fromLegacyText(
                WaschbarServer.getPrefix() + "Du bist kein Troller!"
            ));
            return;
        }

        if (args.length == 1) {
            switch (args[0]) {
                case "help":
                    player.sendMessage(TextComponent.fromLegacyText(
                        ChatColor.GREEN
                            + "\nTroll - Hilfe \n\n"
                            + ChatColor.GRAY
                            + "/troll - Macht dich zu einem Troller\n\n"
                            + "/troll vanish - Macht dich sichtbar / unsichtbar \n\n"
                            + "/troll god - Versetzt dich in den God-Mode \n\n"
                            + "/troll freeze <player> - Friert einen Spieler ein\n\n"
                    ));
                    break;

                case "vanish":
                    if (user.getTrollProfile().isVanish()) {
                        user.getTrollProfile().setVanish(false);

                        player.sendMessage(TextComponent.fromLegacyText(
                            WaschbarServer.getPrefix() + "Du bist nun wieder sichtbar!"
                        ));
                        break;
                    }

                    user.getTrollProfile().setVanish(true);

                    player.sendMessage(TextComponent.fromLegacyText(
                        WaschbarServer.getPrefix() + "Du bist nun unsichtbar!"
                    ));
                    break;

                case "god":
                    if (user.getTrollProfile().isGod()) {
                        user.getTrollProfile().setGod(false);

                        player.sendMessage(TextComponent.fromLegacyText(
                            WaschbarServer.getPrefix() + "Du befindest dich nun nicht mehr im God-Mode!"
                        ));
                        break;
                    }

                    user.getTrollProfile().setGod(true);

                    player.sendMessage(TextComponent.fromLegacyText(
                        WaschbarServer.getPrefix() + "Du wurdest in den God-Mode versetzt!"
                    ));
                    break;

                default:
                    player.performCommand("troll help");
                    break;
            }
        }

        final Player target = Bukkit.getPlayer(args[1]);
        final WaschbarUser targetUser = WaschbarServer.getInstance().getWaschbarUserHandler().getUser(target).orElseThrow();

        switch (args[0]) {
            case "freeze":
                if (targetUser.getTrollProfile().isFreeze()) {
                    targetUser.getTrollProfile().setFreeze(false);

                    player.sendMessage(TextComponent.fromLegacyText(
                        WaschbarServer.getPrefix() + "Du hast den Spieler " + target.getName() + " aufgetaut!"
                    ));
                    break;
                }

                targetUser.getTrollProfile().setFreeze(true);

                player.sendMessage(TextComponent.fromLegacyText(
                    WaschbarServer.getPrefix() + "Du hast den Spieler " + target.getName() + " eingefroren!"
                ));
                break;

            case "crash":
                ((CraftPlayer) target).getHandle().playerConnection.sendPacket(
                    new PacketPlayOutExplosion(
                        Double.MAX_VALUE,
                        Double.MAX_VALUE,
                        Double.MAX_VALUE,
                        Float.MAX_VALUE,
                        Collections.emptyList(),
                        new Vec3D(
                            Double.MAX_VALUE,
                            Double.MAX_VALUE,
                            Double.MAX_VALUE
                        )
                    )
                );

                player.sendMessage(TextComponent.fromLegacyText(
                    WaschbarServer.getPrefix() + "Du hast den Spieler " + target.getName() + " gecrasht!"
                ));
                break;


            default:
                player.performCommand("troll help");
                break;
        }
    }
    //</editor-fold>
}
