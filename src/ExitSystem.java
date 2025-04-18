package Project;
import java.util.Random;

/**
 * Handles the exit process and displays a farewell message for the Swift Shield application.
 */
public class ExitSystem {
    static final String RESET = "\u001B[0m";
    static final String GREEN = "\u001B[38;5;46m";
    static final String BOLD = "\u001B[1m";
    static final String RED = "\u001B[31m";
    static final String YELLOW = "\u001B[33m";
    static final String PINK = "\u001B[35m";

    /**
     * Displays a random exit message and thanks the user before closing the application.
     * @throws Exception if interrupted during display
     */
    public static void display() throws Exception {
        MainMenu.clearScreen();

        String[] messages = {
            "You're now shielded. Log off safely.",
            "Silently locking the vaults...",
            "Goodbye, Defender. Until next time.",
            "Safe exit in progress. Trust the shield.",
            "Audit complete. System secure."
        };

        String msg = messages[new Random().nextInt(messages.length)];

        System.out.println(PINK + BOLD + "╔═════════════════ Shutting Down... ════════════╗");
        System.out.println("║                                               ║");
        System.out.printf(YELLOW + "║  %-42s   ║\n", msg);
        System.out.println("║                                               ║");
        System.out.println(RED + BOLD + "║  " + "Thanks for using Swift Shield!" + "               ║");
        System.out.println("║                                               ║");
        System.out.println(PINK + "╚═══════════════════════════════════════════════╝" + RESET);

        Thread.sleep(3000);
        for (int i = 0; i < 5; i++) {
            Thread.sleep(2500);
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
        System.exit(0);
    }
}