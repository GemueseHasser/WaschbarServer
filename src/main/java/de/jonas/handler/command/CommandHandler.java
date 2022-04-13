package de.jonas.handler.command;

import de.jonas.WaschbarServer;
import de.jonas.object.command.WaschbarCommand;
import lombok.SneakyThrows;
import org.bukkit.command.Command;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Mithilfe des {@link CommandHandler} kann eine festgelegte Anzahl an {@link Class Klassen} als Befehle registriert
 * werden. In jeder Klasse können eine nicht festgelegte Anzahl an Methoden sein, die die {@link WaschbarCommand
 * Waschbar-Command-Annotation} haben. Alle Methode, die diese Annotation haben, werden in diesem Handler als Befehl mit
 * den Angaben der Annotation registriert.
 */
@NotNull
public final class CommandHandler {

    //<editor-fold desc="CONSTANTS">
    /** Alle durch diesen Handler registrierte Befehle. */
    @NotNull
    private static final Map<String, Command> REGISTERED_COMMANDS = new HashMap<>();
    //</editor-fold>


    //<editor-fold desc="registering">

    /**
     * Registriert eine nicht festgelegte Anzahl an Methoden, mit einer {@link WaschbarCommand Waschbar-Command
     * -Annotaion}, in einer festgelegten Anzahl an Klassen.
     *
     * @param commandClasses Die Klassen, in denen nach den Befehls-Implementationen gesucht wird.
     */
    @SneakyThrows
    public void register(@NotNull final Class<?>[] commandClasses) {
        // get all classes from array
        for (@NotNull final Class<?> clazz : commandClasses) {
            final Object object = clazz.newInstance();
            final Method[] methods = clazz.getDeclaredMethods();
            // get all methods from current class
            for (@NotNull final Method method : methods) {
                // check if current method has annotation
                if (!method.isAnnotationPresent(WaschbarCommand.class)) {
                    continue;
                }

                // declare annotation
                final WaschbarCommand annotation = method.getAnnotation(WaschbarCommand.class);

                // get command information from annotation
                final String command = annotation.command();
                final String[] aliases = annotation.aliases();
                final int min = annotation.minLength();
                final int max = annotation.maxLength();
                final String permission = annotation.permission();
                final String usage = annotation.usage();

                // get all commands from server
                final Map<String, Command> commands = WaschbarServer.getInstance()
                    .getServer()
                    .getCommandMap()
                    .getKnownCommands();

                final Command mainCommand = new de.jonas.object.command.Command(
                    command,
                    permission,
                    min,
                    max,
                    usage,
                    method,
                    object
                ).getCommand();

                // check if command is already registered
                if (commands.containsKey(command) && commands.containsValue(mainCommand)) {
                    WaschbarServer.getInstance().getSLF4JLogger().error(
                        "Der Befehl '/{}' wurde bereits registriert und kann kein zweites Mal registriert werden.",
                        command
                    );
                    continue;
                }

                // register main command
                commands.put(
                    command,
                    mainCommand
                );

                // mark main command as registered
                REGISTERED_COMMANDS.put(
                    command,
                    mainCommand
                );

                // get all aliases from the current command
                for (@NotNull final String alias : aliases) {
                    final Command aliasCommand = new de.jonas.object.command.Command(
                        alias,
                        permission,
                        min,
                        max,
                        usage,
                        method,
                        object
                    ).getCommand();

                    // register alias command
                    commands.put(
                        alias,
                        aliasCommand
                    );

                    // mark alias command as registered
                    REGISTERED_COMMANDS.put(
                        alias,
                        aliasCommand
                    );
                }
            }
        }
    }
    //</editor-fold>

    //<editor-fold desc="unregistering">

    /**
     * Entfernt alle bisher hinzugefügten Befehle durch diesen {@link CommandHandler} aus den Bukkit-Commands.
     */
    public void unregisterAll() {
        final Map<String, Command> registeredCommands = WaschbarServer.getInstance()
            .getServer()
            .getCommandMap()
            .getKnownCommands();

        for (@NotNull final Map.Entry<String, Command> command : REGISTERED_COMMANDS.entrySet()) {
            final String commandName = command.getKey();
            final Command commandCommand = command.getValue();

            registeredCommands.remove(commandName, commandCommand);
        }
    }
    //</editor-fold>

}
