package shared.commands;

/**
 * The parent command class that defines the structure of commands
 */
public interface Command {
    /**
     * Prepare the command to be executed on the client
     * This includes being sent to the facade, translated, and sent through the proxy
     */
    void clientExec();

    /**
     * Execute the command on the server
     * After being translated back to Java, executes these steps to update the DB
     */
    void serverExec();
}
