package com.voguepay.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.json.simple.JSONObject;

/**
 * @author chinomso bassey ikwuagwu
 * @see https://voguepay.com/documentation
 * @see https://voguepay.com/integration
 * @see https://github.com/kunlexzybitty/Java-VoguePay-SDK
 */
public class VoguepayTest {
    public static final String MERCHANT_ID = "v_merchant_id";
    public static final String TOTAL_AMOUNT = "total";
    public static final String CURRENCY = "cur";
    public static final String MERCHANT_REF = "merchant_ref";
    public static final String TRANSACTION_MEMO = "memo";
    public static final String CUSTOMER_NAME = "name";
    public static final String CUSTOMER_ADDRESS = "address";
    public static final String CUSTOMER_CITY = "city";
    public static final String CUSTOMER_ZIPCODE = "zipcode";
    public static final String CUSTOMER_EMAIL = "email";
    public static final String CUSTOMER_PHONE = "phone";
    public static final String CARD_NAME = "name";
    public static final String CARD_PAN = "pan";
    public static final String CARD_EXPIRY_MONTH = "expiry_month";
    public static final String CARD_EXPIRY_YEAR = "expiry_year";
    public static final String CARD_CVV = "cvv";
    public static final String CARD_PIN = "pin";
    
    public static void main(String [] args) {
        new VoguepayTest().send();
    }
    
    public void send() {
        Voguepay v = Voguepay.demoInstance();
        VoguepayProperties props = new VoguepayProperties("META-INF/git-ignore.properties");
        System.out.println("Properties: " + props);
        v.setCommandAPIToken(props.getCommandApiToken());
        v.setPublicKeyPath(props.getPublicKeyPath());
        System.out.println("Sending request to Voguepay: " + v);
        final String response = v.GeneratePayLink();
//        final String response = v.DirectPayment();
        System.out.println("Received response from Voguepay: " + response);
    }
    
    public void send_old() {

        final Map<String, Object> body = new HashMap<>();
        body.put(MERCHANT_ID, "DEMO");
        body.put(TOTAL_AMOUNT, "550");
        body.put(CURRENCY, "NGN");
        body.put(MERCHANT_REF, Long.toHexString(System.currentTimeMillis()));
        body.put(TRANSACTION_MEMO, "Payment for drawer");
        
        final String name = "Chinomso Ikwuagwu";
        final Map<String, String> cust = new HashMap<>();
        cust.put(CUSTOMER_NAME, name);
//        cust.put(CUSTOMER_ADDRESS, "");
//        cust.put(CUSTOMER_CITY, "");
//        cust.put(CUSTOMER_ZIPCODE, "");
        cust.put(CUSTOMER_EMAIL, "johnlock@gmail.com");
        cust.put(CUSTOMER_PHONE, "08033000000");
        body.put("customer", cust);
        
        final Map<String, String> card = new HashMap<>();
        card.put(CARD_NAME, name);
        card.put(CARD_PAN, "5061 0502 5442 1653 016");
        card.put(CARD_EXPIRY_MONTH, "01");
        card.put(CARD_EXPIRY_YEAR, "22");
        card.put(CARD_CVV, "212");
        card.put(CARD_PIN, "1234");
        body.put("creditcard", card);
  
        final String jsonStr = JSONObject.toJSONString(body);
        
        System.out.println("Request.body " + jsonStr);
        
        final okhttp3.RequestBody jsonBody = okhttp3.RequestBody
                .create(MediaType.parse("application/json; charset=utf-8"), jsonStr);
        
        final String url = "https://voguepay.com/api";
        
        final Request request = new Request.Builder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")  
//            .post(formBody)         // for form data
            .post(jsonBody)         // for json data
//            .post(multipartBody)    // for multipart data
//            .url(new URL(null, url, new HttpStreamHandlerForBadStatusLine()))
            .url(url)
            .build();
        
        final OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        
        final Map<String, List<Cookie>> all = new HashMap<>();
        
        final OkHttpClient client = clientBuilder
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(45, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .cookieJar(new CookieJar(){
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        final Collection<Cookie> sessionCookies = this.getCookies(url, true);
                        sessionCookies.addAll(cookies);
                    }
                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        final List<Cookie> sessionCookies = this.getCookies(url, false);
                        return sessionCookies == null ? Collections.EMPTY_LIST : sessionCookies;
                    }
                    public List<Cookie> getCookies(HttpUrl url, boolean createIfNone) {
                        final String key = url.topPrivateDomain() == null ? url.host() : url.topPrivateDomain();
                        List<Cookie> sessionCookies = all.get(key);
                        if(sessionCookies == null && createIfNone) {
                            sessionCookies = new ArrayList<>();
                            all.put(key, sessionCookies);
                        }
                        return sessionCookies;
                    }
                })
                .build();

        try(final okhttp3.Response response = client.newCall(request).execute()) {
        
            final String message = response.message();
            System.out.println("Response.message: "+message);
            
            final String result = response.body() == null ? "null" : response.body().string();
            System.out.println("Response.body: "+result);
            
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
