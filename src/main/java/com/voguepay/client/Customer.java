/*
 * Decompiled with CFR 0.149.
 */
package com.voguepay.client;

public class Customer {
    public String Name;
    public String Email;
    public String Phone;
    public String Address;
    public String State;
    public String CountryISO3;
    public String City;
    public String ZipCode;

    @Override
    public String toString() {
        return "Customer{" + "Name=" + Name + ", Email=" + Email + ", Phone=" + Phone + ", Address=" + Address + ", State=" + State + ", CountryISO3=" + CountryISO3 + ", City=" + City + ", ZipCode=" + ZipCode + '}';
    }
}
