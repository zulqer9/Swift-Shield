package Project;

import java.util.Random;
import java.util.Scanner;

/**
 * Provides security best practices and interactive handbook for users.
 */
public class SecurityHandbook {
    static final String RESET = "\u001B[0m";
    static final String PINK = "\u001B[38;5;129m";
    static final String GREEN = "\u001B[38;5;46m";
    static final String RED = "\u001B[38;5;196m";
    static final String BOLD = "\u001B[1m";
    static final String BG_BLACK = "\u001B[48;5;0m";

    /**
     * Displays the security handbook with best practices and handles user navigation.
     * @throws Exception if any error occurs during display
     */
    public static void display() throws Exception {
        MainMenu.clearScreen();
        System.out.println(BG_BLACK + BOLD + GREEN);
        System.out.println(BOLD + PINK + "╔═════════════════════════ Security Handbook ════════════════════╗" + RESET);
        System.out.println(PINK + "║   Practices to Shield Your Identity                            ║" + RESET);
        System.out.println(PINK + "║                                                                ║" + RESET);
        System.out.println(PINK + "║  1. Always use strong, unique passwords per account            ║" + RESET);
        System.out.println(PINK + "║  2. Your password should never be reused                       ║" + RESET);
        System.out.println(PINK + "║  3. Avoid using your name, birthdates or keyboard patterns     ║" + RESET);
        System.out.println(PINK + "║  4. Use multi-factor authentication wherever possible          ║" + RESET);
        System.out.println(PINK + "║  5. Always verify the legitimacy of sites before logging in    ║" + RESET);
        System.out.println(PINK + "║  6. Avoid saving passwords in browsers                         ║" + RESET);
        System.out.println(PINK + "║  7. Change your passwords every 90–180 days                    ║" + RESET);
        System.out.println(PINK + "║                                                                ║" + RESET);
        System.out.println(PINK + "╚════════════════════════════════════════════════════════════════╝" + RESET);
        System.out.println(GREEN + "[M] Main Menu  [X] Exit" + RESET);

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("Enter Action: ");
            String input = sc.nextLine().toLowerCase();
            if ("m".equals(input)) {
                MainMenu.display();
                return;
            } else if ("x".equals(input)) {
                ExitSystem.display();
                return;
            } else {
                System.out.println(RED + "Invalid input. Please enter 'M' for Main Menu or 'X' to Exit." + RESET);
            }
        }
    }
}