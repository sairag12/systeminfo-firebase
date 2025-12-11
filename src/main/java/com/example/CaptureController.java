package com.example;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

	    ref.child("ip").setValueAsync(ip);
	    ref.child("userAgent").setValueAsync(userAgent);

	    IpLookup.captureIpInfo(ip); // writes city/country

	    return "✅ Uploaded client info to Firebase!";
	}


}
