package com.example;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class IpLookup {

    public static void captureIpInfo(String ip) {
        try {
            String token = System.getenv("IPINFO_TOKEN"); // set this in Render env vars
            if (token == null) {
                System.err.println("❌ IPINFO_TOKEN not set in environment variables.");
                return;
            }

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://ipinfo.io/" + ip + "?token=" + token))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parse JSON
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response.body());

            String city = json.has("city") ? json.get("city").asText() : "unknown";
            String country = json.has("country") ? json.get("country").asText() : "unknown";

            // Upload to Firebase
            DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("clickInfo")
                .child(ip);

            ref.child("city").setValueAsync(city);
            ref.child("country").setValueAsync(country);

            System.out.println("📍 Captured IP info: " + city + ", " + country);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
