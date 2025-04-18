package Project;
import java.util.Random;

/**
 * Displays the animated welcome screen and handles the initial user prompt for the Swift Shield application.
 */
public class WelcomeScreen {
    static final String RESET = "\u001B[0m";
    static final String BOLD = "\u001B[1m";
    static final String GREEN = "\u001B[38;5;46m";

    /**
     * Shows the animated welcome art, typing effect, waits for user input, and clears the screen.
     * @throws Exception if interrupted during animation
     */
    public static void display() throws Exception {
        showAnimatedArt();
        showTypingEffect();
        System.in.read();
        smoothClearScreen();
    }

    /**
     * Displays a typing effect for the welcome message.
     * @throws Exception if interrupted during animation
     */
    static void showTypingEffect() throws Exception {
        String message = "Press ENTER to continue [⚡]";
        System.out.println();
        for (char c : message.toCharArray()) {
            System.out.print(BOLD + GREEN + c + RESET);
            Thread.sleep(35);
            System.out.flush();
        }
        for (int i = 0; i < 2; i++) {
            Thread.sleep(200); 
            System.out.print("\r" + " ".repeat(message.length())); 
            Thread.sleep(200);
            System.out.print("\r" + BOLD + GREEN + message + RESET); 
        }
        System.out.println();
    }

    /**
     * Clears the console screen smoothly.
     * @throws Exception if interrupted during clearing
     */
    static void smoothClearScreen() throws Exception {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Displays animated ASCII art for the welcome screen.
     * @throws Exception if interrupted during animation
     */
    static void showAnimatedArt() throws Exception {
        String[] art = {
            "┌─────────────────────────────────────────────────────────────────┐",
            "│    ███████╗    ██╗    ██╗    ██╗    ███████╗    ████████╗       │",
            "│    ██╔════╝    ██║    ██║    ██║    ██╔════╝    ╚══██╔══╝       │",
            "│    ███████╗    ██║ █╗ ██║    ██║    █████╗         ██║          │",
            "│    ╚════██║    ██║███╗██║    ██║    ██╔══╝         ██║          │",
            "│    ███████║    ╚███╔███╔╝    ██║    ██║            ██║          │",
            "│    ╚══════╝     ╚══╝╚══╝     ╚═╝    ╚═╝            ╚═╝          │",
            "│                                                                 │",
            "│███████╗    ██╗  ██╗    ██╗    ███████╗    ██╗         ██████╗   │",
            "│██╔════╝    ██║  ██║    ██║    ██╔════╝    ██║         ██╔══██╗  │",
            "│███████╗    ███████║    ██║    █████╗      ██║         ██║  ██║  │",
            "│╚════██║    ██╔══██║    ██║    ██╔══╝      ██║         ██║  ██║  │",
            "│███████║    ██║  ██║    ██║    ███████╗    ███████╗    ██████╔╝  │",
            "│╚══════╝    ╚═╝  ╚═╝    ╚═╝    ╚══════╝    ╚══════╝    ╚═════╝   │",
            "│                      (C) 2025 SWIFT SHIELD                      │",
            "└─────────────────────────────────────────────────────────────────┘"
        };
        Random rand = new Random();
        String[] colors = { "\u001B[38;5;93m", "\u001B[38;5;129m", "\u001B[38;5;165m",
                            "\u001B[38;5;171m", "\u001B[38;5;177m", "\u001B[38;5;183m",
                            "\u001B[38;5;189m", "\u001B[38;5;201m" };

        for(String line : art) {
            StringBuilder currentLine = new StringBuilder();
            for(char c : line.toCharArray()) {
                String color = colors[rand.nextInt(colors.length)];
                currentLine.append(color).append(c);
                System.out.print(BOLD + currentLine + RESET);
                Thread.sleep(1);
                System.out.print("\r");
            }
            System.out.println();
        }
        Thread.sleep(1000);
    }
}