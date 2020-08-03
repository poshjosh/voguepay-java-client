package com.voguepay.client;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author hp
 */
class DemoInstanceCreator {
    
    private static String ip() {
        try{
            return java.net.InetAddress.getLocalHost().getHostAddress();    
        }catch(UnknownHostException e) {
            return null;
        }
    }
    
    private static String host() {
        try{
            return InetAddress.getLocalHost().getHostName();
        }catch(UnknownHostException e) {
            return "http://looseboxes.com";
        }
    }
    
    public static Voguepay create() {
        Voguepay v = new Voguepay("DEMO", "looseBoxes", "looseboxes@gmail.com", Voguepay.Mode.DEMO);
        v.amount = 550;
        final String host = host();
        v.callbackUrl = host;
        v.currencyISO3 = "NGN";
        v.customerIP = ip();
        v.customerUrl = host;
        v.failUrl = host;
        v.memo = "Payment for drawer";
        v.redirectUrl = host;
        v.reference = Long.toHexString(System.currentTimeMillis());
//        v.storeID
        v.successUrl = host;
//        v.token
//        v.tokenize
  
        Customer customerObj = new Customer();
        customerObj.Name="John Lock";
        customerObj.Email="johnlock@gmail.com";
        customerObj.Address="1 tampa bay road";
        customerObj.CountryISO3="NGN";
        customerObj.Phone="08033000000";
        customerObj.State="Abuja";
        customerObj.City="Mabushi";
        customerObj.ZipCode="900108";
        v.setCustomer(customerObj);

        CreditCard creditcardObj= new CreditCard();
        creditcardObj.Name=customerObj.Name;
        creditcardObj.Pan="5061 0502 5442 1653 016";
        // No pin??
        // Zero or one based month?
        creditcardObj.ExpiryMonth="01";
        creditcardObj.ExpiryYear="22";
        creditcardObj.CVV="212";
        v.setCreditCard(creditcardObj);
        
        return v;
    }
}
