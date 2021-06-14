/*
 * Decompiled with CFR 0.149.
 */
package com.voguepay.client;

public class CreditCard {
    public String Name;
    public String Pan;
    public String ExpiryMonth;
    public String ExpiryYear;
    public String CVV;

    @Override
    public String toString() {
        return "CreditCard{" + "Name=" + Name + ", Pan=" + Pan + ", ExpiryMonth=" + ExpiryMonth + ", ExpiryYear=" + ExpiryYear + ", CVV=" + CVV + '}';
    }
}
