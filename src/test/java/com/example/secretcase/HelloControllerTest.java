package com.example.secretcase;

import java.util.Base64;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest(properties = "ADMIN_PASSWORD=test_admin123")
@AutoConfigureMockMvc
public class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testWithValidCredentials() throws Exception {
        mockMvc.perform(get("/").header("Authorization", "Basic " + Base64.getEncoder().encodeToString("admin:test_admin123".getBytes())))
        .andExpect(status().isOk())
        .andExpect(content().string("Hello, welcome to the secret case!"));
    }

    @Test
    public void testWithInvalidCredentials() throws Exception {
        mockMvc.perform(get("/").header("Authorization", "Basic " + Base64.getEncoder().encodeToString("admin:invalid_password".getBytes())))
        .andExpect(status().isUnauthorized())
        .andExpect(content().string("Invalid credentials"));
    }   

    @Test
    public void testWithNoCredentials() throws Exception {
        mockMvc.perform(get("/"))
        .andExpect(status().isUnauthorized())
        .andExpect(content().string("Unauthorized"));
    }

    @Test
    public void testWithInvalidBase64() throws Exception {
        mockMvc.perform(get("/").header("Authorization", "Basic " + "invalid_base64"))
        .andExpect(status().isUnauthorized())
        .andExpect(content().string("Invalid credentials"));
    }
}