package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.FileInputStream;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        try {
            // Load service account credentials from environment variable
            String path = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
            FileInputStream serviceAccount = new FileInputStream(path);

            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://systeminfo-ed6ac-default-rtdb.firebaseio.com/")
                .build();

            FirebaseApp.initializeApp(options);
            System.out.println("✅ Firebase initialized successfully.");
        } catch (Exception e) {
            System.err.println("❌ Firebase initialization failed:");
            e.printStackTrace();
        }

        SpringApplication.run(Application.class, args);
    }
}
