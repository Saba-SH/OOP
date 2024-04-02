import com.example.part1.AccountManager;
import junit.framework.TestCase;

public class AccountManagerTest extends TestCase {
    public void testcheckUser() {
        AccountManager am = new AccountManager();
        
        assertFalse(am.checkUser("dog", "bone"));
        assertFalse(am.checkUser("cat", "sausage"));
    }

    public void testCreate() {
        AccountManager am = new AccountManager();
        
        assertTrue(am.addUser("dog", "bone"));
        assertTrue(am.addUser("cat", "sausage"));

        assertFalse(am.addUser("dog", "bone"));
        assertFalse(am.addUser("dog", "sausage"));
        assertFalse(am.addUser("cat", "bone"));
    }
    
    public void testCreateAndcheckUser() {
        AccountManager am = new AccountManager();

        assertFalse(am.checkUser("dog", "bone"));
        assertFalse(am.checkUser("dog", "sausage"));
        assertTrue(am.addUser("dog", "bone"));
        assertTrue(am.checkUser("dog", "bone"));
        assertFalse(am.checkUser("dog", "sausage"));

        assertFalse(am.checkUser("cat", "sausage"));
        assertTrue(am.addUser("cat", "sausage"));
        assertTrue(am.checkUser("cat", "sausage"));
        assertFalse(am.checkUser("cat", "bone"));

        assertTrue(am.checkUser("dog", "bone"));
        assertFalse(am.checkUser("dog", "sausage"));
        assertFalse(am.addUser("dog", "bone"));
        assertTrue(am.checkUser("dog", "bone"));
        assertFalse(am.checkUser("dog", "sausage"));

        assertTrue(am.checkUser("cat", "sausage"));
        assertFalse(am.checkUser("cat", "1234"));
        assertFalse(am.checkUser("dog", "4321"));
    }
}
