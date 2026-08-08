package com.showdown.wildfly.userservice.Service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;

import com.showdown.wildfly.userservice.Models.User;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@ApplicationScoped
public class KeycloakService {

    private static final String SERVER = "http://keycloak:8080";
    private static final String REALM = "jpetstore";

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    private static final String ADMIN_CLIENT = "admin-cli";

    private static final String FRONTEND_CLIENT = "jpetstore-frontend";

    /**
     * Existing authenticate() method.
     */
    public String authenticate(String username, String password) throws Exception {

        URL url = new URL(
                SERVER + "/realms/" + REALM + "/protocol/openid-connect/token");

        HttpURLConnection conn =
                (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        conn.setRequestProperty(
                "Content-Type",
                "application/x-www-form-urlencoded");

        String body =
                "grant_type=password" +
                "&client_id=" + URLEncoder.encode(FRONTEND_CLIENT, "UTF-8") +
                "&username=" + URLEncoder.encode(username, "UTF-8") +
                "&password=" + URLEncoder.encode(password, "UTF-8");

        try(OutputStream os = conn.getOutputStream()){
            os.write(body.getBytes());
        }

        if(conn.getResponseCode()!=200){
            throw new RuntimeException("Authentication failed");
        }

        try(JsonReader reader =
                    Json.createReader(conn.getInputStream())){

            JsonObject json = reader.readObject();
            return json.getString("access_token");
        }
    }

    /**
     * Gets an admin token.
     */
    public String getAdminToken() throws Exception {

        URL url = new URL(
                SERVER + "/realms/master/protocol/openid-connect/token");

        HttpURLConnection conn =
                (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        conn.setRequestProperty(
                "Content-Type",
                "application/x-www-form-urlencoded");

        String body =
                "grant_type=password" +
                "&client_id=" + ADMIN_CLIENT +
                "&username=" + URLEncoder.encode(ADMIN_USERNAME,"UTF-8") +
                "&password=" + URLEncoder.encode(ADMIN_PASSWORD,"UTF-8");

        try(OutputStream os = conn.getOutputStream()){
            os.write(body.getBytes());
        }

        if(conn.getResponseCode()!=200){
            throw new RuntimeException("Cannot obtain admin token");
        }

        try(JsonReader reader =
                    Json.createReader(conn.getInputStream())){

            JsonObject json = reader.readObject();
            return json.getString("access_token");
        }
    }

    /**
     * Creates a user in Keycloak.
     * Returns the generated userId.
     */
    public String createUser(User user) throws Exception {

        String token = getAdminToken();

        URL url = new URL(
                SERVER +
                        "/admin/realms/" +
                        REALM +
                        "/users");

        HttpURLConnection conn =
                (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        conn.setRequestProperty(
                "Authorization",
                "Bearer " + token);

        conn.setRequestProperty(
                "Content-Type",
                "application/json");

        JsonObjectBuilder builder = Json.createObjectBuilder();

        builder.add("username", user.getUsername());
        builder.add("enabled", true);
        builder.add("emailVerified", false);

        if(user.getEmail()!=null)
            builder.add("email", user.getEmail());

        if(user.getFirstName()!=null)
            builder.add("firstName", user.getFirstName());

        if(user.getLastName()!=null)
            builder.add("lastName", user.getLastName());

        JsonObject body = builder.build();

        try(OutputStream os = conn.getOutputStream()){
            os.write(body.toString().getBytes());
        }

        if(conn.getResponseCode()!=201){

            if(conn.getResponseCode() == 409) {
                throw new RuntimeException(
                        "Keycloak already has a user named '" + user.getUsername()
                                + "' (username is taken there, even though it isn't in the local DB)");
            }

            throw new RuntimeException(
                    "User creation failed. HTTP="
                            + conn.getResponseCode());
        }

        return getUserId(token, user.getUsername());
    }

    /**
     * Finds the Keycloak user id by username.
     */
    private String getUserId(String token,
                             String username) throws Exception {

        URL url = new URL(
                SERVER +
                        "/admin/realms/" +
                        REALM +
                        "/users?username=" +
                        URLEncoder.encode(username,"UTF-8"));

        HttpURLConnection conn =
                (HttpURLConnection) url.openConnection();

        conn.setRequestProperty(
                "Authorization",
                "Bearer " + token);

        try(JsonReader reader =
                    Json.createReader(conn.getInputStream())){

            JsonArray array = reader.readArray();

            if(array.isEmpty()){
                throw new RuntimeException("User not found");
            }

            return array.getJsonObject(0)
                    .getString("id");
        }
    }

    /**
     * Sets the password.
     */
    public void setPassword(String userId,
                            String password)
            throws Exception {

        String token = getAdminToken();

        URL url = new URL(
                SERVER +
                        "/admin/realms/" +
                        REALM +
                        "/users/" +
                        userId +
                        "/reset-password");

        HttpURLConnection conn =
                (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("PUT");
        conn.setDoOutput(true);

        conn.setRequestProperty(
                "Authorization",
                "Bearer " + token);

        conn.setRequestProperty(
                "Content-Type",
                "application/json");

        JsonObject json =
                Json.createObjectBuilder()
                        .add("temporary", false)
                        .add("type", "password")
                        .add("value", password)
                        .build();

        try(OutputStream os = conn.getOutputStream()){
            os.write(json.toString().getBytes());
        }

        if(conn.getResponseCode()!=204){

            throw new RuntimeException(
                    "Password set failed");
        }
    }

    /**
     * Assigns CUSTOMER realm role.
     */
    public void assignCustomerRole(String userId)
            throws Exception {

        String token = getAdminToken();

        URL roleUrl = new URL(
                SERVER +
                        "/admin/realms/" +
                        REALM +
                        "/roles/CUSTOMER");

        HttpURLConnection roleConn =
                (HttpURLConnection) roleUrl.openConnection();

        roleConn.setRequestProperty(
                "Authorization",
                "Bearer " + token);

        JsonObject role;

        try(JsonReader reader =
                    Json.createReader(roleConn.getInputStream())){

            role = reader.readObject();
        }

        URL assignUrl = new URL(
                SERVER +
                        "/admin/realms/" +
                        REALM +
                        "/users/" +
                        userId +
                        "/role-mappings/realm");

        HttpURLConnection assign =
                (HttpURLConnection) assignUrl.openConnection();

        assign.setRequestMethod("POST");
        assign.setDoOutput(true);

        assign.setRequestProperty(
                "Authorization",
                "Bearer " + token);

        assign.setRequestProperty(
                "Content-Type",
                "application/json");

        String body = "[" + role.toString() + "]";

        try(OutputStream os = assign.getOutputStream()){
            os.write(body.getBytes());
        }

        if(assign.getResponseCode()!=204){

            throw new RuntimeException(
                    "Role assignment failed");
        }
    }
}