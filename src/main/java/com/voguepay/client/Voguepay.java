package com.voguepay.client;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;

public class Voguepay {
    private Boolean staging;
    private String MerchantID;
    private String Username;
    private String Email;
    private String CommandAPI;
    private byte[] PublicKey;
    private Customer customer;
    private CreditCard creditcard;
    public double amount;
    public String currencyISO3;
    public String memo;
    public String storeID;
    public String reference;
    public String callbackUrl;
    public String customerUrl;
    public String customerIP;
    public String redirectUrl;
    public String successUrl;
    public String failUrl;
    public Boolean tokenize = false;
    public String token = "";

    public Voguepay(String MerchantID, String Username, String Email) {
        this.MerchantID = MerchantID;
        this.Username = Username;
        this.Email = Email;
        this.staging = true;
    }

    public void setMode(String mode) {
        if (mode.toLowerCase() != "demo" || mode.toLowerCase() != "live") {
            new IllegalArgumentException("Parameter must be demo or live");
        }
        this.staging = mode.toLowerCase() == "demo";
    }

    public void setCommandAPIToken(String token) {
        this.CommandAPI = token;
    }

    private void setPublicKey(String key) {
        this.PublicKey = key.getBytes();
    }

    public void setPublicKeyPath(String filepath) {
        try {
            byte[] encoded = Files.readAllBytes(new File(filepath).toPath());
            String key = new String(encoded, StandardCharsets.US_ASCII);
            key = key.replace("-", "");
            key = key.replace("BEGIN", "");
            key = key.replace("END", "");
            key = key.replace("PUBLIC", "");
            key = key.replace("KEY", "");
            key = key.replace("\n", "");
            key = key.trim();
            this.PublicKey = Base64.getDecoder().decode(key);
        }
        catch (IOException ex) {
            Logger.getLogger(Voguepay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setCreditCard(CreditCard creditcard) {
        this.creditcard = creditcard;
    }

    public String GeneratePayLink() {
        Connection connectionObj = new Connection();
        String respData = null;
        String param = this.staging != false ? "v_merchant_id=demo" : "v_merchant_id=" + this.urlencode(this.MerchantID);
        param = param + "&cur=" + this.urlencode(this.currencyISO3);
        param = param + "&merchant_ref=" + this.urlencode(this.reference);
        param = param + "&memo=" + this.urlencode(this.memo);
        param = param + "&store_id=" + this.urlencode(this.storeID);
        param = param + "&total=" + this.urlencode(this.amount);
        param = param + "&notify_url=" + this.urlencode(this.callbackUrl);
        param = param + "&success_url=" + this.urlencode(this.successUrl);
        param = param + "&fail_url=" + this.urlencode(this.failUrl);
        param = param + "&developer_code=5cab1c85491ec";
        if (this.customer instanceof Customer) {
            param = param + "&email=" + this.urlencode(this.customer.Email);
            param = param + "&phone=" + this.urlencode(this.customer.Phone);
            param = param + "&name=" + this.urlencode(this.customer.Name);
            param = param + "&address=" + this.urlencode(this.customer.Address);
            param = param + "&city=" + this.urlencode(this.customer.City);
            param = param + "&state=" + this.urlencode(this.customer.State);
            param = param + "&zipcode=" + this.urlencode(this.customer.ZipCode);
            param = param + "&country=" + this.urlencode(this.customer.CountryISO3);
        }
        try {
            respData = connectionObj.executeGet(param);
        }
        catch (Exception ex) {
            Logger.getLogger(Voguepay.class.getName()).log(Level.SEVERE, null, ex);
        }
        respData = respData == "-1" ? "{\"status\": false, \"message\":\"Unable to process command\"}" : (respData == "-3" ? "{\"status\": false, \"message\":\"Empty Merchant ID\"}" : (respData == "-4" ? "{\"status\": false, \"message\":\"Memo is empty\"}" : (respData == "-14" ? "{\"status\": false, \"message\":\"Invalid merchant ID\"}" : (respData.contains("voguepay.com") ? "{\"status\": true, \"message\":\"Redirection required\",\"URL\":\"" + respData + "\"}" : "{\"status\": false, \"message\":\"No result\"}"))));
        return respData;
    }

    public String DirectPayment() {
        try {
            JSONObject jsonObj = new JSONObject();
            Random r = new Random();
            String ref = "javasdk-" + r.nextInt(1000000) + 1;
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.reset();
            digest.update((this.CommandAPI + "card" + this.Email + ref).getBytes("utf8"));
            String hash = String.format("%0128x", new BigInteger(1, digest.digest()));
            jsonObj.put((Object)"task", (Object)"card");
            jsonObj.put((Object)"merchant", (Object)this.MerchantID);
            jsonObj.put((Object)"ref", (Object)ref);
            jsonObj.put((Object)"hash", (Object)hash);
            jsonObj.put((Object)"version", (Object)"2");
            if (this.staging.booleanValue()) {
                jsonObj.put((Object)"demo", (Object)true);
            }
            jsonObj.put((Object)"currency", (Object)this.currencyISO3);
            jsonObj.put((Object)"merchant_ref", (Object)this.reference);
            jsonObj.put((Object)"memo", (Object)this.memo);
            jsonObj.put((Object)"store_id", (Object)this.storeID);
            jsonObj.put((Object)"total", (Object)this.amount);
            jsonObj.put((Object)"response_url", (Object)this.urlencode(this.callbackUrl));
            jsonObj.put((Object)"referral_url", (Object)this.customerUrl);
            jsonObj.put((Object)"redirect_url", (Object)this.urlencode(this.redirectUrl));
            if (this.tokenize.booleanValue()) {
                jsonObj.put((Object)"tokenize", (Object)true);
            }
            if (this.token.length() > 0) {
                jsonObj.put((Object)"token", (Object)this.token);
            }
            jsonObj.put((Object)"developer_code", (Object)"5cab1c85491ec");
            if (this.customer instanceof Customer) {
                jsonObj.put((Object)"email", (Object)this.customer.Email);
                jsonObj.put((Object)"phone", (Object)this.customer.Phone);
                jsonObj.put((Object)"customerName", (Object)this.customer.Name);
                jsonObj.put((Object)"customerAddress", (Object)this.customer.Address);
                jsonObj.put((Object)"customerCity", (Object)this.customer.City);
                jsonObj.put((Object)"customerState", (Object)this.customer.State);
                jsonObj.put((Object)"customerZip", (Object)this.customer.ZipCode);
                jsonObj.put((Object)"customerLocation", (Object)this.customer.CountryISO3);
            }
            if (this.creditcard instanceof CreditCard) {
                JSONObject creditcardObj = new JSONObject();
                creditcardObj.put((Object)"name", (Object)this.creditcard.Name);
                creditcardObj.put((Object)"pan", (Object)this.creditcard.Pan);
                creditcardObj.put((Object)"month", (Object)this.creditcard.ExpiryMonth);
                creditcardObj.put((Object)"year", (Object)this.creditcard.ExpiryYear);
                creditcardObj.put((Object)"cvv", (Object)this.creditcard.CVV);
                Encryption ac = new Encryption();
                PublicKey publicKey = ac.getPublic(this.PublicKey);
                String cardJson = "{\"card\":" + creditcardObj.toJSONString() + "}";
                jsonObj.put((Object)"params", (Object)ac.encryptText(cardJson, publicKey));
            }
            Connection connectionObj = new Connection();
            String respData = connectionObj.executePost("json=" + this.urlencode(jsonObj.toJSONString()));
            return respData;
        }
        catch (Exception ex) {
            Logger.getLogger(Voguepay.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    public String query(String TransactionID) {
        JSONObject jsonObj = new JSONObject();
        Random r = new Random();
        String ref = "javasdk-" + r.nextInt(1000000) + 1;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.reset();
            digest.update((this.CommandAPI + "query" + this.Email + ref).getBytes("utf8"));
            String hash = String.format("%0128x", new BigInteger(1, digest.digest()));
            jsonObj.put((Object)"task", (Object)"query");
            jsonObj.put((Object)"merchant", (Object)this.MerchantID);
            jsonObj.put((Object)"ref", (Object)ref);
            jsonObj.put((Object)"hash", (Object)hash);
            if (this.staging.booleanValue()) {
                jsonObj.put((Object)"demo", (Object)true);
            }
            jsonObj.put((Object)"transaction_id", (Object)TransactionID);
            Connection connectionObj = new Connection();
            String respData = connectionObj.executePost("json=" + this.urlencode(jsonObj.toJSONString()));
            return respData;
        }
        catch (Exception ex) {
            Logger.getLogger(Voguepay.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    private String urlencode(Object value) {
        if (value == null) {
            return "";
        }
        try {
            return URLEncoder.encode(value.toString(), StandardCharsets.UTF_8.toString());
        }
        catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Voguepay.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    @Override
    public String toString() {
        return "Voguepay{" + "staging=" + staging + ", MerchantID=" + MerchantID + 
                ", Username=" + Username + ", Email=" + Email + ", CommandAPI=" + CommandAPI + 
                ", PublicKey=" + (PublicKey!=null) + ", amount=" + amount + ", currencyISO3=" + currencyISO3 + 
                ", memo=" + memo + ", storeID=" + storeID + ", reference=" + reference + 
                ", callbackUrl=" + callbackUrl + ", customerUrl=" + customerUrl + 
                ", customerIP=" + customerIP + ", redirectUrl=" + redirectUrl + 
                ", successUrl=" + successUrl + ", failUrl=" + failUrl + ", tokenize=" + tokenize + 
                ", token=" + token + "\ncustomer=" + customer + "\ncreditcard=" + creditcard +  '}';
    }
}
