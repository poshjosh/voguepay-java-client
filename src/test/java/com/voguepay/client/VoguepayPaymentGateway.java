package com.voguepay.client;

import com.voguepay.client.Voguepay;
import com.voguepay.client.CreditCard;
import com.voguepay.client.Customer;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

/**
 * The HTTP 409 Conflict response status code indicates a request conflict with current state of the server.
 * @author hp
 * @see https://github.com/kunlexzybitty/Java-VoguePay-SDK
 * @see https://voguepay.com/integration
 */
public class VoguepayPaymentGateway{

    private final Map<String, Object> properties;
    
    public VoguepayPaymentGateway(Map<String, Object> properties) {
        this.properties = Objects.requireNonNull(properties);
    }
    
    public Voguepay createNew() {
        //Create voguepay instance with voguepay details
        Voguepay voguepayObj=new Voguepay("demo", "username", "email");
        voguepayObj.setCommandAPIToken("commandapitoken");
        final String publicKeyPath = "";
        voguepayObj.setPublicKeyPath(publicKeyPath);
        voguepayObj.setMode("demo");
        return voguepayObj;
    }
    
    public void processPayment(Map<String, Object> payment) {
        
        //Create voguepay instance with voguepay details
        Voguepay voguepayObj = this.createNew();
        
        Customer customerObj = new Customer();
        customerObj.Name=null;
        customerObj.Email=null;
        
//            customerObj.Address=null;
//            customerObj.CountryISO3=null;
//            customerObj.Phone=
//            customerObj.State=
//            customerObj.City=
//            customerObj.ZipCode=
            
        voguepayObj.setCustomer(customerObj);

        voguepayObj.memo=null;
        voguepayObj.amount=0d;
//        voguepayObj.storeID= //Optional
        voguepayObj.currencyISO3=null;
        
        voguepayObj.reference=null;

        CreditCard creditcardObj= new CreditCard();
        creditcardObj.Name=null;
        creditcardObj.Pan=null;
        LocalDate expiryDate = null;
        // Zero or one based month?
        creditcardObj.ExpiryMonth=String.valueOf(expiryDate.getMonthValue() - 1);
        creditcardObj.ExpiryYear=String.valueOf(expiryDate.getYear());
        creditcardObj.CVV=null;
        voguepayObj.setCreditCard(creditcardObj);

//        voguepayObj.callbackUrl="https://domain.com/callback";
//        voguepayObj.customerUrl="https://domain.com/customer";
//        voguepayObj.redirectUrl="https://domain.com/redirect";


        final String jsonresponse = this.directPayment(voguepayObj);
    }
    
    /**
     * 
        Sample Responses
        <code><pre>
        {
          "merchant_ref":"MY_UNIQUE_REF",
          "field":"Card Year",
          "message":"Card provided has expired",
          "status":"ERROR",
          "response":"WL002",
          "salt":"5d06a1811caec",
          "hash":"421caed76f402bf27656297a1f03e62303d1e55d478e9bf5882b77561db47808ad765fc91cd1ed576524adb0ec091f7a07d349a70adce342efd6a72f34e55b10",
          "username":"username"
        }

        {
        "merchant_ref":"MY_UNIQUE_REF",
        "status":"OK",
        "response":"WL3D",
        "message":"3D Authorization required",
        "reference":"5d069f1d3ed98",
        "redirect_url":"https://voguepay.com/?p=vpgate&ref=czoxMzoiNWQwNjlmMWQzZWQ5OCI7",
        "salt":"5d069f1d399e5",
        "hash":"7d3d2e64dcba9f95bfc705721f9367923139ab752d01a453456ee0bf5a15f9bd069f14c8c9faac018f4cd1dc57760d37b17820f13586da95070ec1dd5268b8b1",
        username":"username"
        }
        </pre></code>
     * @return 
     */
    private String directPayment(Voguepay voguepayObj) {
        System.out.println(this.getClass().getSimpleName() + 
                ". Sending request to Voguepay payment gateway");
        final String jsonresponse = voguepayObj.DirectPayment();
//        LOG.debug("Response from Voguepay payment gateway: {}", jsonresponse);
        return jsonresponse;
    }
    
    private String requireValue(String s) {
        if(s == null || s.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return s;
    }
    
    private String require3Chars(String s) {
        if(s.length() != 3) {
            throw new IllegalArgumentException();
        }
        return s;
    }
}
