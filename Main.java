package Project;

// Main entry point for the Swift Shield application
public class Main {
    /**
     * The main method launches the application by displaying the welcome screen and then the main menu.
     * @param args Command-line arguments (not used)
     * @throws Exception if any error occurs during display
     */
    public static void main(String[] args) throws Exception {
        WelcomeScreen.display(); // Show animated welcome screen
        MainMenu.display();      // Launch the main menu for user interaction
    }
}