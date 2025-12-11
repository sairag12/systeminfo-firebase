package com.example;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;

import java.io.FileInputStream;

public class ReadStorageInfo {
    public static void main(String[] args) {
        try {
            // Initialize Firebase
            FileInputStream serviceAccount = new FileInputStream("serviceAccountKey.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://systeminfo-ed6ac-default-rtdb.firebaseio.com/")
                    .build();

            FirebaseApp.initializeApp(options);

            // Reference to deviceStorage node
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("deviceStorage");

            // Read data once
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Read systemName if present
                        String systemName = snapshot.child("systemName").getValue(String.class);

                        Long total = snapshot.child("totalSpace").getValue(Long.class);
                        Long free = snapshot.child("freeSpace").getValue(Long.class);
                        Long used = snapshot.child("usedSpace").getValue(Long.class);

                        System.out.println("📊 Storage Info from Firebase:");
                        if (systemName != null) {
                            System.out.println("System Name: " + systemName);
                        }
                        System.out.println("Total Space: " + total);
                        System.out.println("Free Space: " + free);
                        System.out.println("Used Space: " + used);
                    } else {
                        System.out.println("⚠️ No data found at deviceStorage node.");
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    System.err.println("❌ Error reading data: " + error.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
