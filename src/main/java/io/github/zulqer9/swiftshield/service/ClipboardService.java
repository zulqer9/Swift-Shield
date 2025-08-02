package io.github.zulqer9.swiftshield.service;

/**
 * Service interface for clipboard operations.
 * Provides secure clipboard management functionality.
 * 
 * @author zulqer9
 * @since 2.0.0
 */
public interface ClipboardService {
    
    /**
     * Copies text to the system clipboard.
     * 
     * @param text the text to copy
     * @return true if the operation was successful
     */
    boolean copyToClipboard(String text);
    
    /**
     * Copies text to the clipboard with automatic clearing after timeout.
     * 
     * @param text the text to copy
     * @param timeoutMs the timeout in milliseconds after which to clear the clipboard
     * @return true if the operation was successful
     */
    boolean copyToClipboardWithTimeout(String text, long timeoutMs);
    
    /**
     * Clears the system clipboard.
     * 
     * @return true if the operation was successful
     */
    boolean clearClipboard();
    
    /**
     * Checks if clipboard operations are available on this system.
     * 
     * @return true if clipboard is available
     */
    boolean isClipboardAvailable();
}