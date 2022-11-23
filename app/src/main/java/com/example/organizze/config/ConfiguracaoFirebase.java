package com.example.organizze.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFirebase {

    private static FirebaseAuth auth;
    private static DatabaseReference database;


    //Retorna a instância do FirebaseAuth
    public static FirebaseAuth getFirebaseAuth() {

        if (auth == null) auth = FirebaseAuth.getInstance();
        return auth;
    }

    //Retorna a instância do FirebaseDatabase
    public static DatabaseReference getFirebaseDatabase() {
        if (database == null) {
            database = FirebaseDatabase.getInstance().getReference();
        }
        return database;

    }
}
