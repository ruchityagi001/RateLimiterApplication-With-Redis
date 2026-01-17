package com.example.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RateLimitTest {
    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/hello"))
                .build();

        for (int i = 1; i <= 12; i++) {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Request " + i + " Status: " + response.statusCode());

            // Optional: Small delay so we don't overwhelm the local CPU
            Thread.sleep(100);
        }
    }
}
