package com.example;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;

@RestController
public class CaptureController {

    @GetMapping("/capture")
    public String capture(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        String safeIp = ip.replace(".", "_"); // sanitize IP

        DatabaseReference ref = FirebaseDatabase.getInstance()
            .getReference("clickInfo")
            .child(safeIp);

        // Store IP and User-Agent
        ref.child("ip").setValueAsync(ip);
        ref.child("userAgent").setValueAsync(userAgent);

        // Store per-root usable space
        for (File root : File.listRoots()) {
            long usableSpaceGB = root.getUsableSpace() / (1024 * 1024 * 1024);

            // Print to console for debugging
            System.out.println("Root: " + root.getAbsolutePath() +
                               " Usable Space (GB): " + usableSpaceGB);

            // Save to Firebase under "storage/<root>"
            String rootKey = root.getAbsolutePath().replace(":", "").replace("\\", "");
            ref.child("storage").child(rootKey).child("usableSpaceGB")
               .setValueAsync(usableSpaceGB);
        }

        // Existing IP lookup for city/country
        IpLookup.captureIpInfo(ip);

        return "✅ Uploaded client + storage info to Firebase!";
    }
}

//package com.example;
//
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//@RestController
//public class CaptureController {
//
//	@GetMapping("/capture")
//	public String capture(HttpServletRequest request) {
//	    String ip = request.getRemoteAddr();
//	    String userAgent = request.getHeader("User-Agent");
//
//	    String safeIp = ip.replace(".", "_"); // sanitize IP
//
//	    DatabaseReference ref = FirebaseDatabase.getInstance()
//	        .getReference("clickInfo")
//	        .child(safeIp);
//
//	    ref.child("ip").setValueAsync(ip);
//	    ref.child("userAgent").setValueAsync(userAgent);
//
//	    IpLookup.captureIpInfo(ip); // writes city/country
//
//	    return "✅ Uploaded client info to Firebase!";
//	}
//
//
//}
