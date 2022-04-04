package de.jonas.handler.util;

import lombok.SneakyThrows;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Mithilfe des {@link ConfigurationHandler} lassen sich Daten aus Configuration-Dateien abrufen und auch abspeichern.
 */
public final class ConfigurationHandler {

    /**
     * Ruft aus einer bestimmten Datei Daten ab, die unter einem bestimmten Pfad abgespeichert sind.
     *
     * @param file Die Datei, aus der Daten abgerufen werden sollen.
     * @param path Der Pfad, unter dem die Daten in der Datei abgespeichert werden.
     *
     * @return Die Daten, die sich in der Datei unter dem bestimmten Pfad befinden.
     */
    public static Object getConfiguration(
        @NotNull final String file,
        @NotNull final String path
    ) {
        final File configFile = new File("plugins/WaschbarServer", file);
        final FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        return config.get(path);
    }

    /**
     * Speichert Daten unter einem bestimmten Pfad in einer bestimmten Datei ab.
     *
     * @param file Die Datei, in der die Daten abgespeichert werden sollen.
     * @param path Der Pfad, unter dem die Daten in der Datei abgespeichert werden sollen.
     * @param data Die Daten, die in der Datei abgespeichert werden sollen.
     */
    @SneakyThrows
    public static void setConfiguration(
        @NotNull final String file,
        @NotNull final String path,
        @NotNull final Object data
    ) {
        final File configFile = new File("plugins/WaschbarServer", file);
        final FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        config.set(path, data);
        config.save(configFile);
    }

}
