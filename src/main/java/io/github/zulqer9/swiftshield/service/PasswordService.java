package io.github.zulqer9.swiftshield.service;

import io.github.zulqer9.swiftshield.core.Password;
import io.github.zulqer9.swiftshield.core.PasswordPolicy;

/**
 * Service interface for password generation operations.
 * Provides methods to generate secure passwords based on policies.
 * 
 * @author zulqer9
 * @since 2.0.0
 */
public interface PasswordService {
    
    /**
     * Generates a password using the default policy.
     * 
     * @return a generated password
     */
    Password generatePassword();
    
    /**
     * Generates a password using the specified policy.
     * 
     * @param policy the password generation policy
     * @return a generated password
     */
    Password generatePassword(PasswordPolicy policy);
    
    /**
     * Generates a password of the specified length using the default policy.
     * 
     * @param length the desired password length
     * @return a generated password
     */
    Password generatePassword(int length);
    
    /**
     * Optimizes an existing password by strengthening it.
     * 
     * @param existingPassword the password to optimize
     * @return an optimized password
     */
    Password optimizePassword(String existingPassword);
    
    /**
     * Optimizes an existing password using the specified policy.
     * 
     * @param existingPassword the password to optimize
     * @param policy the password policy to apply
     * @return an optimized password
     */
    Password optimizePassword(String existingPassword, PasswordPolicy policy);
    
    /**
     * Validates a password against the default policy.
     * 
     * @param password the password to validate
     * @return true if the password meets the policy requirements
     */
    boolean validatePassword(String password);
    
    /**
     * Validates a password against the specified policy.
     * 
     * @param password the password to validate
     * @param policy the policy to validate against
     * @return true if the password meets the policy requirements
     */
    boolean validatePassword(String password, PasswordPolicy policy);
    
    /**
     * Calculates the entropy of a password.
     * 
     * @param password the password to analyze
     * @return the calculated entropy value
     */
    double calculateEntropy(String password);
}