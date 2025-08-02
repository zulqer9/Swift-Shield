package io.github.zulqer9.swiftshield.service;

/**
 * Service interface for UI rendering operations.
 * Handles terminal-based user interface components.
 * 
 * @author zulqer9
 * @since 2.0.0
 */
public interface UIService {
    
    /**
     * Displays the animated welcome screen.
     * 
     * @throws InterruptedException if the animation is interrupted
     */
    void showWelcomeScreen() throws InterruptedException;
    
    /**
     * Displays the main menu and returns the user's choice.
     * 
     * @return the selected menu option
     */
    int showMainMenu();
    
    /**
     * Displays a password with its metadata in a formatted box.
     * 
     * @param password the password value
     * @param strength the strength description
     * @param score the score description
     */
    void showPasswordDisplay(String password, String strength, String score);
    
    /**
     * Displays the security handbook.
     */
    void showSecurityHandbook();
    
    /**
     * Displays the exit screen and terminates the application.
     * 
     * @throws InterruptedException if the exit animation is interrupted
     */
    void showExitScreen() throws InterruptedException;
    
    /**
     * Clears the terminal screen.
     */
    void clearScreen();
    
    /**
     * Displays a message with the specified color.
     * 
     * @param message the message to display
     * @param colorType the color type (success, error, warning, info)
     */
    void showMessage(String message, MessageType colorType);
    
    /**
     * Gets user input with a prompt.
     * 
     * @param prompt the input prompt
     * @return the user input
     */
    String getUserInput(String prompt);
    
    /**
     * Gets a menu choice from the user with validation.
     * 
     * @param prompt the choice prompt
     * @param minOption the minimum valid option
     * @param maxOption the maximum valid option
     * @return the selected option
     */
    int getUserChoice(String prompt, int minOption, int maxOption);
    
    /**
     * Displays a progress bar.
     * 
     * @param percentage the completion percentage (0-100)
     * @param length the bar length in characters
     * @return the progress bar string
     */
    String createProgressBar(int percentage, int length);
    
    /**
     * Message types for colored output.
     */
    enum MessageType {
        SUCCESS, ERROR, WARNING, INFO, PRIMARY, SECONDARY
    }
}