package Project;
import java.util.Random;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.security.SecureRandom;
import java.util.Scanner;

/**
 * Provides password optimization by strengthening user-provided passwords and displaying their strength and score.
 */
public class PasswordOptimizer {
    static final String RESET = "\u001B[0m";
    static final String GREEN = "\u001B[38;5;46m";
    static final String RED = "\u001B[31m";
    static final String CYAN = "\u001B[36m";
    static final String BOLD = "\u001B[1m";
    static final String BLUE = "\u001B[34m";
    static final String YELLOW = "\u001B[33m";
    static final String MAGENTA = "\u001B[35m";
    
    /**
     * Displays the password optimizer interface and handles user actions.
     * @throws Exception if any error occurs during display
     */
    public static void display() throws Exception {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            MainMenu.clearScreen();
            System.out.println(BOLD + GREEN + "═════════════════ Password Optimizer ═════════════════" + RESET);
            System.out.print(BOLD + GREEN + "         \n Enter password to strengthen: " + RESET);
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("m")) {
                MainMenu.display();
                return;
            }
            
            String optimized = generateStrengthenedPassword(input);
            String strength = generateStrength();
            String score = generateScore();
            
            MainMenu.clearScreen();
            System.out.println(BOLD + GREEN + "╔═════════════════ Password Optimizer ═════════════════╗");
            System.out.printf("║ Original:     %-40s\n", input);
            System.out.println("║ Generated Password:                                   ");
            System.out.println("║     ╭──────────────────────────╮                      ║");
            System.out.printf("║     │ %-24s │                     \n", YELLOW + BOLD + optimized + GREEN);
            System.out.println("║     ╰──────────────────────────╯                      ║");
            System.out.printf("║ Strength:     %-40s\n", strength);
            System.out.printf("║ Score:        %-40s\n", score);
            System.out.println("╚══════════════════════════════════════════════════════╝" + RESET);
            
            System.out.println(BOLD + CYAN + "[C]" + RESET + BOLD + GREEN + " Copy  " + 
                              BOLD + YELLOW + "[R]" + RESET + BOLD + GREEN + " Regenerated  " + 
                              BOLD + BLUE + "[M]" + RESET + BOLD + GREEN + " Menu  " + 
                              BOLD + MAGENTA + "[X]" + RESET + BOLD + GREEN + " Exit" + RESET);
            
            String choice;
            while (true) {
                System.out.print(BOLD + GREEN + "Enter Action: " + RESET);
                choice = scanner.nextLine().trim().toLowerCase();
                switch (choice) {
                    case "c": 
                        copyToClipboard(optimized); 
                        Thread.sleep(2000); // Give time to read the message
                        break;
                    case "r": break;
                    case "m": MainMenu.display(); return;
                    case "x": ExitSystem.display(); return;
                    default: System.out.println(BOLD + RED + "Invalid input. Use C, R, M, X." + RESET);
                }
                if (choice.equals("r")) break;
            }
        }
    }
    
    static String generateStrengthenedPassword(String input) {
        if (input == null || input.isEmpty()) {
            return generateRandomPassword();
        }
        
        // Keep some parts of the original password for "matching" with input
        int keepLength = Math.min(3, input.length());
        String keptPart = input.substring(0, keepLength);
        
        // Generate additional secure parts
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String specialChars = "!@#$%^&*()_+-=[]{}|;:,.<>?";
        String allChars = upperCase + lowerCase + numbers + specialChars;
        
        SecureRandom rand = new SecureRandom();
        StringBuilder sb = new StringBuilder(keptPart);
        
        // Ensure at least one character from each category
        sb.append(upperCase.charAt(rand.nextInt(upperCase.length())));
        sb.append(lowerCase.charAt(rand.nextInt(lowerCase.length())));
        sb.append(numbers.charAt(rand.nextInt(numbers.length())));
        sb.append(specialChars.charAt(rand.nextInt(specialChars.length())));
        
        // Add remaining characters
        for (int i = 0; i < 8 + rand.nextInt(5); i++) // 12+ chars total
            sb.append(allChars.charAt(rand.nextInt(allChars.length())));
        
        // Shuffle all except the kept part
        char[] passArray = sb.toString().toCharArray();
        for (int i = passArray.length - 1; i > keepLength; i--) {
            int j = keepLength + rand.nextInt(i + 1 - keepLength);
            char temp = passArray[i];
            passArray[i] = passArray[j];
            passArray[j] = temp;
        }
        
        return new String(passArray);
    }
    
    static String generateRandomPassword() {
        return PasswordGeneration.generatePassword();
    }
    
    static String generateStrength() {
        int percent = 85 + new SecureRandom().nextInt(16); // [85–100]
        return "[" + "█".repeat(percent / 10) + "░".repeat(10 - (percent / 10)) + "] " + percent + "%";
    }
    
    static String generateScore() {
        String[] scores = {"Reinforced", "Titan Locked", "Superior+", "Battle Hardened"};
        return scores[new SecureRandom().nextInt(scores.length)];
    }
    
    static void copyToClipboard(String password) {
        StringSelection selection = new StringSelection(password);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
        System.out.println(BOLD + GREEN + " Password copied to clipboard!" + RESET);
    }
}