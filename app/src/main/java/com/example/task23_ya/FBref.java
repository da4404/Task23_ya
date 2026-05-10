package com.example.task23_ya;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * @author darya
 * @version 1.0
 * @since 10/05/2026
 * Helper class for Firebase references
 */
public class FBref {
    public static final FirebaseDatabase FBDB = FirebaseDatabase.getInstance();
    public static final DatabaseReference refExpenses = FBDB.getReference(Constants.EXPENSES_NODE);
}
