package com.example.part1;

import java.util.HashMap;
import java.util.Map;

public class AccountManager {

    private Map<String, String> users;

    public AccountManager() {
        users = new HashMap<>();
        users.put("Patrick", "1234");
        users.put("Molly", "FloPup");
    }

    /**
     * Puts a new user with the given username and password into the database.
     * @return true if it's a new user, returns false if the user was already registered.
     * */
    public boolean addUser(String username, String password) {
        boolean retval = true;
        if(users.containsKey(username))
            retval = false;
        users.put(username, password);

        return retval;
    }

    /**
     * Attempts to log in to the username's account with the given password.
     * @return true if the login is successful, false if it's not
     * */
    public boolean checkUser(String username, String password) {
        if(!users.containsKey(username) || !users.get(username).equals(password))
            return false;
        return true;
    }

    /**
     * Checks if a user with the given username exists.
     * @return true if the username is in use, false otherwise
     * */
    public boolean checkUserName(String username) {
        return users.containsKey(username);
    }
}
