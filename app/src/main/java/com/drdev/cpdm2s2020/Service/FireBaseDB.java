package com.drdev.cpdm2s2020.Service;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FireBaseDB {

    private DatabaseReference mDatabase;

    public FireBaseDB() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }
}