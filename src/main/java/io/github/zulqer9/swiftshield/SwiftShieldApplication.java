package io.github.zulqer9.swiftshield;

import io.github.zulqer9.swiftshield.config.ConfigurationManager;
import io.github.zulqer9.swiftshield.service.ClipboardService;
import io.github.zulqer9.swiftshield.service.PasswordService;
import io.github.zulqer9.swiftshield.service.UIService;
import io.github.zulqer9.swiftshield.service.impl.ClipboardServiceImpl;
import io.github.zulqer9.swiftshield.service.impl.PasswordServiceImpl;
import io.github.zulqer9.swiftshield.service.impl.UIServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * Main application class for Swift Shield - Professional Password Toolkit.
 * This class serves as the entry point and orchestrates the application components.
 * 
 * @author zulqer9
 * @since 2.0.0
 */
@Command(
    name = "swift-shield",
    description = "Professional-grade terminal-based password toolkit",
    version = "Swift Shield 2.0.0",
    mixinStandardHelpOptions = true
)
public class SwiftShieldApplication implements Runnable {
    
    private static final Logger logger = LoggerFactory.getLogger(SwiftShieldApplication.class);
    
    @Option(names = {"-i", "--interactive"}, 
            description = "Run in interactive mode (default)")
    private boolean interactive = true;
    
    @Option(names = {"-g", "--generate"}, 
            description = "Generate a password and exit")
    private boolean generateOnly = false;
    
    @Option(names = {"-l", "--length"}, 
            description = "Password length (default: 16)")
    private int length = 16;
    
    @Option(names = {"-p", "--policy"}, 
            description = "Password policy (default, enterprise)")
    private String policy = "default";
    
    @Option(names = {"--no-clipboard"}, 
            description = "Disable clipboard integration")
    private boolean disableClipboard = false;
    
    private final ConfigurationManager config;
    private final PasswordService passwordService;
    private final UIService uiService;
    private final ClipboardService clipboardService;
    
    /**
     * Default constructor for dependency injection.
     */
    public SwiftShieldApplication() {
        this.config = ConfigurationManager.getInstance();
        this.passwordService = new PasswordServiceImpl();
        this.clipboardService = disableClipboard ? null : new ClipboardServiceImpl();
        this.uiService = new UIServiceImpl(config, clipboardService);
    }
    
    /**
     * Constructor for testing with injected dependencies.
     */
    public SwiftShieldApplication(PasswordService passwordService, 
                                 UIService uiService, 
                                 ClipboardService clipboardService) {
        this.config = ConfigurationManager.getInstance();
        this.passwordService = passwordService;
        this.uiService = uiService;
        this.clipboardService = clipboardService;
    }
    
    /**
     * Main entry point for the application.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Logger rootLogger = LoggerFactory.getLogger("io.github.zulqer9.swiftshield");
        rootLogger.info("Starting Swift Shield Application v2.0.0");
        
        try {
            SwiftShieldApplication app = new SwiftShieldApplication();
            int exitCode = new CommandLine(app).execute(args);
            System.exit(exitCode);
        } catch (Exception e) {
            rootLogger.error("Fatal error during application startup", e);
            System.err.println("Fatal error: " + e.getMessage());
            System.exit(1);
        }
    }
    
    @Override
    public void run() {
        logger.info("Swift Shield started with interactive={}, generateOnly={}, length={}, policy={}", 
                   interactive, generateOnly, length, policy);
        
        try {
            if (generateOnly) {
                runGenerateOnlyMode();
            } else {
                runInteractiveMode();
            }
        } catch (Exception e) {
            logger.error("Error during application execution", e);
            uiService.showMessage("An error occurred: " + e.getMessage(), UIService.MessageType.ERROR);
        }
    }
    
    /**
     * Runs the application in generate-only mode.
     */
    private void runGenerateOnlyMode() {
        logger.info("Running in generate-only mode");
        
        var password = length > 0 ? 
            passwordService.generatePassword(length) : 
            passwordService.generatePassword();
        
        System.out.println(password.getValue());
        
        if (clipboardService != null && clipboardService.isClipboardAvailable()) {
            boolean copied = clipboardService.copyToClipboardWithTimeout(
                password.getValue(), 
                config.getInt("security.clipboard.timeout", 30000)
            );
            if (copied) {
                System.err.println("Password copied to clipboard (will be cleared in 30 seconds)");
            }
        }
        
        logger.info("Password generated successfully: {}", password.getMaskedValue());
    }
    
    /**
     * Runs the application in interactive mode.
     */
    private void runInteractiveMode() {
        logger.info("Running in interactive mode");
        
        try {
            // Show welcome screen
            uiService.showWelcomeScreen();
            
            // Main application loop
            boolean running = true;
            while (running) {
                int choice = uiService.showMainMenu();
                running = handleMenuChoice(choice);
            }
            
        } catch (InterruptedException e) {
            logger.warn("Application interrupted by user", e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.error("Error in interactive mode", e);
            uiService.showMessage("An error occurred: " + e.getMessage(), UIService.MessageType.ERROR);
        }
    }
    
    /**
     * Handles menu choice selection and returns whether to continue running.
     * 
     * @param choice the menu choice
     * @return true to continue running, false to exit
     */
    private boolean handleMenuChoice(int choice) {
        try {
            switch (choice) {
                case 1 -> handlePasswordGeneration();
                case 2 -> handlePasswordOptimization();
                case 3 -> uiService.showSecurityHandbook();
                case 4 -> {
                    uiService.showExitScreen();
                    return false;
                }
                default -> {
                    uiService.showMessage("Invalid choice: " + choice, UIService.MessageType.ERROR);
                }
            }
        } catch (InterruptedException e) {
            logger.warn("Menu operation interrupted", e);
            Thread.currentThread().interrupt();
            return false;
        } catch (Exception e) {
            logger.error("Error handling menu choice: " + choice, e);
            uiService.showMessage("Error processing choice: " + e.getMessage(), UIService.MessageType.ERROR);
        }
        return true;
    }
    
    /**
     * Handles password generation workflow.
     */
    private void handlePasswordGeneration() {
        logger.info("Starting password generation workflow");
        
        var password = passwordService.generatePassword();
        String strengthBar = password.getStrength().getProgressBar(10);
        String strengthDisplay = String.format("[%s] %d%%", strengthBar, password.getStrengthPercentage());
        
        uiService.showPasswordDisplay(password.getValue(), strengthDisplay, password.getStrength().getDescription());
        
        logger.info("Password generated: {}", password);
    }
    
    /**
     * Handles password optimization workflow.
     */
    private void handlePasswordOptimization() {
        logger.info("Starting password optimization workflow");
        
        String userPassword = uiService.getUserInput("Enter password to optimize: ");
        if (userPassword != null && !userPassword.trim().isEmpty()) {
            var optimizedPassword = passwordService.optimizePassword(userPassword.trim());
            String strengthBar = optimizedPassword.getStrength().getProgressBar(10);
            String strengthDisplay = String.format("[%s] %d%%", strengthBar, optimizedPassword.getStrengthPercentage());
            
            uiService.showPasswordDisplay(optimizedPassword.getValue(), strengthDisplay, optimizedPassword.getStrength().getDescription());
            
            logger.info("Password optimized from length {} to {}", userPassword.length(), optimizedPassword.getLength());
        } else {
            uiService.showMessage("No password provided for optimization", UIService.MessageType.WARNING);
        }
    }
}