package de.jonas.handler.scoreboard;

import de.jonas.object.unit.WaschbarUser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

/**
 * Mithilfe des {@link ScoreboardHandler} lässt sich das Scoreboard für einen bestimmten Nutzer mit allen aktuellen
 * daten setzen. Man kann das Scoreboard beliebig oft setzen, um es zu aktualisieren.
 */
public final class ScoreboardHandler {

    /**
     * Setzt das Scoreboard für einen bestimmten Nutzer mit allen aktuellen Daten. Man kann das Scoreboard beliebig oft
     * setzen, um es zu aktualisieren.
     *
     * @param user Der Nutzer, dem das Scoreboard gesetzt werden soll.
     */
    public static void setScoreboard(@NotNull final WaschbarUser user) {
        // get scoreboard manager
        final ScoreboardManager manager = Bukkit.getScoreboardManager();

        // create new scoreboard
        final Scoreboard scoreboard = manager.getNewScoreboard();

        // set objective properties
        final Objective objective = scoreboard.registerNewObjective("abcde", "abcde");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(
            ChatColor.GOLD.toString() + ChatColor.BOLD + "Waschbär-Server"
        );

        // empty line
        objective.getScore(ChatColor.LIGHT_PURPLE.toString()).setScore(10);

        // show built blocks
        objective.getScore(ChatColor.AQUA.toString() + ChatColor.BOLD + "Platzierte Blöcke:").setScore(9);

        final Team builtBlocksTeam = scoreboard.registerNewTeam("builtBlocks");
        builtBlocksTeam.setPrefix(ChatColor.GRAY + "➤ ");
        builtBlocksTeam.setSuffix(ChatColor.WHITE.toString() + ChatColor.BOLD + user.getBuiltBlocks());
        builtBlocksTeam.addEntry(ChatColor.BLACK.toString());

        objective.getScore(ChatColor.BLACK.toString()).setScore(8);

        // empty line
        objective.getScore(ChatColor.DARK_PURPLE.toString()).setScore(7);

        // set scoreboard
        user.getPlayer().setScoreboard(scoreboard);
    }

}
