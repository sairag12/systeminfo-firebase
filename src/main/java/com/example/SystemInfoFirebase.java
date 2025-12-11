package com.example;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;

import java.io.FileInputStream;
import java.net.InetAddress;
import java.util.concurrent.ExecutionException;

public class SystemInfoFirebase {
	public static void main(String[] args) {
		try {
			// Initialize Firebase with your actual service account file
//        	FileInputStream serviceAccount = new FileInputStream(
//        		    "C:/SytemInfo/SystemInfoFirebase/systeminfo-ed6ac-firebase-adminsdk-fbsvc-26fa6715a2.json"
//        		);
			FileInputStream serviceAccount = new FileInputStream(System.getenv("GOOGLE_APPLICATION_CREDENTIALS"));

			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.setDatabaseUrl("https://systeminfo-ed6ac-default-rtdb.firebaseio.com/").build();

			FirebaseApp.initializeApp(options);

			// Collect system info
			String systemName = InetAddress.getLocalHost().getHostName();
			long totalSpace = StorageInfo.getTotalSpace();
			long freeSpace = StorageInfo.getFreeSpace();
			long usedSpace = StorageInfo.getUsedSpace();

			// Write system info under storageInfo/{systemName}
			DatabaseReference ref = FirebaseDatabase.getInstance().getReference("storageInfo").child(systemName);

			// Block until each write completes
			ref.child("systemName").setValueAsync(systemName).get();
			ref.child("totalSpace").setValueAsync(totalSpace).get();
			ref.child("freeSpace").setValueAsync(freeSpace).get();
			ref.child("usedSpace").setValueAsync(usedSpace).get();

			System.out.println("✅ System info uploaded to Firebase.");

			// Read back the data once
			ref.addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot snapshot) {
					if (snapshot.exists()) {
						String name = snapshot.child("systemName").getValue(String.class);
						Long total = snapshot.child("totalSpace").getValue(Long.class);
						Long free = snapshot.child("freeSpace").getValue(Long.class);
						Long used = snapshot.child("usedSpace").getValue(Long.class);

						System.out.println("📊 Storage Info from Firebase:");
						System.out.println("System Name: " + name);
						System.out.println("Total Space: " + total);
						System.out.println("Free Space: " + free);
						System.out.println("Used Space: " + used);
					} else {
						System.out.println("⚠️ No data found for this system.");
					}
				}

				@Override
				public void onCancelled(DatabaseError error) {
					System.err.println("❌ Error reading data: " + error.getMessage());
				}
			});

		} catch (ExecutionException | InterruptedException e) {
			System.err.println("❌ Write failed: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
