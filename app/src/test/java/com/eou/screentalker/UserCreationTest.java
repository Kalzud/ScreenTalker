package com.eou.screentalker;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eou.screentalker.Activities.RegisterActivity;
import com.eou.screentalker.UserCredentials;
import com.eou.screentalker.UserManager;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.User;

public class UserCreationTest {

    @Mock
    private FirebaseAuth mockFirebaseAuth;

    private UserCredentials credentials;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        credentials = new UserCredentials("ben@example.com", "examplePass");
    }

    @Test
    public void testEmailValidation_validEmail_returnsTrue() {
        String validEmail = "user@example.com";
        boolean isValid = RegisterActivity.isValidEmail(validEmail);
        assertTrue(isValid);
    }

    @Test
    public void testEmailValidation_invalidEmail_returnsFalse() {
        String invalidEmail = "invalidemail";
        boolean isValid = RegisterActivity.isValidEmail(invalidEmail);
        assertFalse(isValid);
    }

    @Test
    public void testPasswordValidation_emptyPassword_returnsFalse() {
        String emptyPassword = "";
        boolean isValid = RegisterActivity.isValidPassword(emptyPassword);
        assertFalse(isValid);
    }

    @Test
    public void testPasswordValidation_shortPassword_returnsFalse() {
        String shortPassword = "short";
        boolean isValid = RegisterActivity.isValidPassword(shortPassword);
        assertFalse(isValid);
    }

    @Test
    public void testPasswordValidation_validPassword_returnsTrue() {
        String validPassword = "longEnoughPassword";
        boolean isValid = RegisterActivity.isValidPassword(validPassword);
        assertTrue(isValid);
    }

    @Test
    public void testPasswordMatching_matchingPasswords_returnsTrue() {
        String password = "validPassword";
        String confirmPassword = password;
        boolean isMatching = RegisterActivity.isPasswordMatching(password, confirmPassword);
        assertTrue(isMatching);
    }

    @Test
    public void testPasswordMatching_notMatchingPasswords_returnsFalse() {
        String password = "password1";
        String confirmPassword = "password2";
        boolean isMatching = RegisterActivity.isPasswordMatching(password, confirmPassword);
        assertFalse(isMatching);
    }

    @Test
    public void testCreateUser() throws Exception {

        // Act
        UserManager userManager = new UserManager(mockFirebaseAuth);
        userManager.createUser(credentials);

        // Assert
        verify(mockFirebaseAuth).createUserWithEmailAndPassword(credentials.getEmail(), credentials.getPassword());

    }



    @Test
    public void testSignInWithEmailAndPassword_validCredentials_succeeds() throws Exception {
        // Arrange
        UserManager userManager = new UserManager(mockFirebaseAuth);
        String validEmail = "test@example.com";
        String validPassword = "password";

        try {
            userManager.signInWithEmailAndPassword(validEmail, validPassword);
//            fail("Expected an exception but none was thrown"); // If no exception is thrown, fail the test
        } catch (Exception e) { // Catch any Exception (modify if needed)
            // Assert - No need to check specific exception type anymore
            assertFalse(e.getMessage().contains("Invalid email or password")); // Assert the message contains the expected text
        }

        // You can add optional assertions to verify the Task's behavior (if applicable)
        // For example, if your UserManager handles the Task result, you could assert on its success:
        // assertTrue(signInTask.isSuccessful());
    }


    @Test
    public void testSignInWithEmailAndPassword_invalidEmail_throwsException() throws Exception {
      // Arrange
        UserManager userManager = new UserManager(mockFirebaseAuth);
        String validEmail = "test@.com";
        String invalidPassword = "password";

        try {
            userManager.signInWithEmailAndPassword(validEmail, invalidPassword);
            fail("Expected an exception but none was thrown"); // If no exception is thrown, fail the test
        } catch (Exception e) { // Catch any Exception (modify if needed)
            // Assert - No need to check specific exception type anymore
            assertTrue(e.getMessage().contains("Invalid email or password")); // Assert the message contains the expected text
        }
    }





    @Test
    public void testSignInWithEmailAndPassword_invalidPassword_throwsException() throws Exception {
        // Arrange
        UserManager userManager = new UserManager(mockFirebaseAuth);
        String validEmail = "test@example.com";
        String invalidPassword = "short";

        try {
            userManager.signInWithEmailAndPassword(validEmail, invalidPassword);
            fail("Expected an exception but none was thrown"); // If no exception is thrown, fail the test
        } catch (Exception e) { // Catch any Exception (modify if needed)
            // Assert - No need to check specific exception type anymore
            assertTrue(e.getMessage().contains("Invalid email or password")); // Assert the message contains the expected text
        }

//        // Act
//        userManager.signInWithEmailAndPassword(validEmail, invalidPassword);
//        fail("Expected an exception but none was thrown"); // If no exception is thrown, fail the test
    }



}



// Class representing TaskResult (modify as needed)
class TaskResult {
    private User user;

    public TaskResult(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
