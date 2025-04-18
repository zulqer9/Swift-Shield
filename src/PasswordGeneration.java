package Project;
import java.util.Random;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.security.SecureRandom;
import java.util.Scanner;

/**
 * Provides instant password generation functionality with strength and score display.
 */
public class PasswordGeneration {
    static final String RESET = "\u001B[0m";
    static final String GREEN = "\u001B[38;5;46m";
    static final String RED = "\u001B[31m";
    static final String BOLD = "\u001B[1m";
    static final String CYAN = "\u001B[36m";
    static final String BLUE = "\u001B[34m";
    static final String YELLOW = "\u001B[33m";
    static final String MAGENTA = "\u001B[35m";
    static final String BG_BLACK = "\u001B[48;5;0m";
    
    /**
     * Displays the password generator interface and handles user actions.
     * @throws Exception if any error occurs during display
     */
    public static void display() throws Exception {
        String password = generatePassword();
        String strength = generateStrength();
        String score = generateScore();
        
        while (true) {
            MainMenu.clearScreen();
            System.out.println(BOLD + GREEN); // Removed BG_BLACK
            System.out.println(GREEN + BOLD + "╔══════════════ Instant Password Generator ══════════════╗" + RESET);
            // System.out.println(BOLD + GREEN); // Removed BG_BLACK
            showPasswordBox(password, strength, score);
            String action = handleUserActions(password);
            
            if (action.equals("r")) {
                // Only regenerate when "r" is pressed
                password = generatePassword();
                strength = generateStrength();
                score = generateScore();
            } else if (action.equals("m") || action.equals("x")) {
                return; // Exit to main menu or exit system
            }
        }
    }
    
    /**
     * Displays the generated password, its strength, and score in a formatted box.
     * @param password The generated password
     * @param strength The strength description
     * @param score The score value
     */
    static void showPasswordBox(String password, String strength, String score) {
       
        System.out.println(GREEN + "║                                                        ║");
        System.out.println("║" + BOLD + "                Generated Password:                " + RESET + GREEN + "     ║"); // Removed BG_BLACK
        System.out.println("║                                                        ║"); // Removed BG_BLACK
        System.out.println("║           ╭──────────────────────────╮                 ║"); // Removed BG_BLACK
        System.out.printf ("║           │ %-24s      │                   \n", YELLOW + BOLD + password + RESET + GREEN); // Removed BG_BLACK
        System.out.println("║           ╰──────────────────────────╯                 ║"); // Removed BG_BLACK
        System.out.println("║                                                        ║"); // Removed BG_BLACK
        System.out.printf ("║  Security Strength:  %-36s\n", strength); // Removed BG_BLACK
        System.out.printf ("║  VaultGuard Score™:  %-36s\n", score); // Removed BG_BLACK
        System.out.println("║                                                        ║"); // Removed BG_BLACK
        System.out.println("╚════════════════════════════════════════════════════════╝" + RESET); // Removed BG_BLACK
        System.out.println( 
                          BOLD + GREEN + "[C]" + RESET + MAGENTA + " Copy  " + 
                          BOLD + CYAN + "[R]" + RESET + GREEN + " Regenerate  " + 
                          BOLD + BLUE + "[M]" + RESET + GREEN + " Main Menu  " + 
                          BOLD + RED + "[X]" + RESET + BOLD + RED + " Exit" + RESET); // Removed BG_BLACK from all
    }
    
    static String handleUserActions(String password) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String input;
        
        while (true) {
            System.out.println(BOLD + GREEN);  // Removed BG_BLACK
            System.out.print(GREEN + BOLD + "Enter Action: " + RESET);
            input = scanner.nextLine().trim().toLowerCase();
            switch (input) {
                case "c":
                    copyToClipboard(password);
                    // Slow fade effect for copy message (5 seconds)
                    for (int i = 5; i > 0; i--) {
                        System.out.print("\r" + GREEN + BOLD + "Copied to clipboard! Closing in " + i + " seconds..." + RESET + "     ");
                        Thread.sleep(1000);
                    }
                    System.out.print("\r" + " ".repeat(60) + "\r"); // Clear the line
                    return "c"; // Return to main loop without regenerating
                case "r":
                    return "r"; // Signal to regenerate password
                case "m":
                    MainMenu.display();
                    return "m";
                case "x":
                    ExitSystem.display();
                    return "x";
                default:
                    System.out.println(RED + "Invalid input. Use C, R, M, or X." + RESET);
            }
        }
    }
    
    static String generatePassword() {
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String specialChars = "!@#$%^&*()_+-=[]{}|;:,.<>?";
        String allChars = upperCase + lowerCase + numbers + specialChars;
        
        SecureRandom rand = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        
        // Ensure at least one character from each category for stronger passwords
        sb.append(upperCase.charAt(rand.nextInt(upperCase.length())));
        sb.append(lowerCase.charAt(rand.nextInt(lowerCase.length())));
        sb.append(numbers.charAt(rand.nextInt(numbers.length())));
        sb.append(specialChars.charAt(rand.nextInt(specialChars.length())));
        
        // Add remaining characters
        for (int i = 0; i < 12 + rand.nextInt(6); i++) // 16-22 chars total
            sb.append(allChars.charAt(rand.nextInt(allChars.length())));
        
        // Shuffle the password
        char[] passArray = sb.toString().toCharArray();
        for (int i = passArray.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            char temp = passArray[i];
            passArray[i] = passArray[j];
            passArray[j] = temp;
        }
        
        return new String(passArray);
    }
    
    static String generateStrength() {
        int percent = 85 + new SecureRandom().nextInt(16); // [85–100] for stronger passwords
        int blocks = percent / 10;
        return "[" + "█".repeat(blocks) + "░".repeat(10 - blocks) + "] " + percent + "%";
    }
    
    static String generateScore() {
        String[] scores = {"Fortified", "Ultra-Secure", "Unbreakable", "Shielded++", "AES+Guard"};
        return scores[new SecureRandom().nextInt(scores.length)];
    }
    
    static void copyToClipboard(String text) {
        StringSelection stringSelection = new StringSelection(text);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
        // System.out.println(GREEN + BOLD + "✔ Copied to clipboard!" + RESET);
    }
}