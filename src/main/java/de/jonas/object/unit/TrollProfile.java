package de.jonas.object.unit;

import de.jonas.WaschbarServer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Ein {@link TrollProfile} wird für jeden {@link WaschbarUser} instanziiert und in diesem Profil wird alles
 * abgespeichert, was nötig ist zum trollen.
 */
@Getter
@Setter
@NotNull
@RequiredArgsConstructor
public final class TrollProfile {

    /** Der Spieler, auf den sich dieses {@link TrollProfile} bezieht. */
    @NotNull
    private final Player player;
    /** Der Zustand, ob sich dieser Nutzer momentan im Troll-Modus befindet. */
    private boolean troller;
    /** Der Zustand, ob der Nutzer, dem dieses Profil zugeordnet ist unsichtbar ist. */
    private boolean vanish;
    /** Der Zustand, ob der Nutzer, dem dieses Profil zugeordnet ist knockback bekommt. */
    private boolean knockback;


    /**
     * Macht diesen Spieler unsichtbar oder sichtbar, je nachdem welcher Zustand angegeben wird.
     *
     * @param vanish Der Zustand, ob der Spieler unsichtbar oder sichtbar sein soll.
     */
    public void setVanish(final boolean vanish) {
        this.vanish = vanish;

        if (this.vanish) {
            for (@NotNull final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.hidePlayer(
                    WaschbarServer.getInstance(),
                    this.player
                );
            }

            return;
        }

        for (@NotNull final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.showPlayer(
                WaschbarServer.getInstance(),
                this.player
            );
        }
    }

}
