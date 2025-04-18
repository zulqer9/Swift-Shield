package Project;
import java.util.Scanner;

/**
 * Handles the main menu interface and user navigation for the Swift Shield application.
 */
public class MainMenu {
    static final String RESET = "\u001B[0m";
    static final String BOLD = "\u001B[1m";
    static final String GREEN = "\u001B[38;5;46m";
    static final String BG_GREEN = "\u001B[48;5;46m";
    static final String BG_BLACK = "\u001B[48;5;0m";
    static final String WHITE = "\u001B[37m";
    static final String RED = "\u001B[31m";
    static final Scanner scanner = new Scanner(System.in);
    static boolean firstMenuEntry = true; // Add this line

    /**
     * Displays the main menu and processes user choices until exit is selected.
     * @throws Exception if any error occurs during menu handling
     */
    public static void display() throws Exception {
        int choice;
        do {
            clearScreen();
            drawMenu();
            choice = getChoice();
            handleChoice(choice);
        } while (choice != 4);
    }
    
    /**
     * Draws the main menu options to the console.
     */
    static void drawMenu() {
        System.out.println(BG_BLACK + BOLD + GREEN);
        System.out.println("╔═══════════════════ Main Menu ═══════════════════╗");
        System.out.println("║                                                 ║");
        System.out.println("║     1. Instant Password Generation              ║");
        System.out.println("║     2. Current Password Optimizer               ║");
        System.out.println("║     3. Security Handbook                        ║");
        System.out.println("║                                                 ║");
        System.out.println("║     4. Exit System                              ║");
        System.out.println("║                                                 ║");
        System.out.println("╚═════════════════════════════════════════════════╝" + RESET);
    }
    
    /**
     * Prompts the user to enter a menu choice and validates the input.
     * @return the user's menu choice as an integer
     */
    static int getChoice() {
        int choice;
        while (true) {
            System.out.print(BOLD + WHITE + "Enter choice (1-4): " + RESET);
            String input = scanner.nextLine().trim();

            // Ignore the very first empty input after welcome screen
            if (firstMenuEntry && input.isEmpty()) {
                firstMenuEntry = false;
                clearScreen();
                drawMenu();
                continue;
            }

            if (input.isEmpty()) {
                continue;
            }

            try {
                choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= 4) break;
                System.out.println(BOLD + RED + "Invalid input. Please enter a number between 1 and 4." + RESET);
            } catch (NumberFormatException e) {
                System.out.println(BOLD + RED + "Invalid input. Please enter a number between 1 and 4." + RESET);
            }
        }
        return choice;
    }
    
    /**
     * Handles the user's menu choice by invoking the appropriate feature.
     * @param choice the user's selected menu option
     * @throws Exception if any error occurs during feature execution
     */
    static void handleChoice(int choice) throws Exception {
        switch (choice) {
            case 1 -> PasswordGeneration.display();
            case 2 -> PasswordOptimizer.display();
            case 3 -> SecurityHandbook.display();
            case 4 -> ExitSystem.display();
        }
    }
    
    /**
     * Clears the console screen.
     */
    static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}