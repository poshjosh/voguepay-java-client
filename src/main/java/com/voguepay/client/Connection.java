package com.voguepay.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;

public class Connection {
    private final String BaseURL = "https://voguepay.com/";
    private final String USER_AGENT = "Mozilla/5.0";

    public String executeGet(String params) throws IOException {
        String inputLine;
        URL obj = null;
        HttpURLConnection con = null;
        try {
            obj = new URL(this.BaseURL + "?p=linkToken&" + params);
            con = (HttpURLConnection)obj.openConnection();
        }
        catch (Exception ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = con.getResponseCode();
        if (responseCode != 200) {
            return new String();
        }
        try(BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            final StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString().trim();
        }
    }

    public String executePost(String jsonparam) {
        URL obj = null;
        final StringBuilder response = new StringBuilder();
        try {
            String inputLine;
            obj = new URL(this.BaseURL + "api/");
            HttpsURLConnection con = null;
            con = (HttpsURLConnection)obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setDoOutput(true);
            DataOutputStream wr = null;
            wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(jsonparam);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            if (responseCode != 200) {
                return new String();
            }
            BufferedReader in = null;
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        }
        catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response.toString().trim();
    }
}
