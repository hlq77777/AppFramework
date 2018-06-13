package com.android.kingwong.kingwongproject.bean;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String userName;
    private List<Address> addresses;

    public User(){
        addresses = new ArrayList<>();
    }

    public static class Address{
        private String street;
        private String city;

        public void setStreet(String street){
            this.street = street;
        }

        public String getStreet() {
            return street;
        }

        public void setCity(String city){
            this.city = city;
        }

        public String getCity() {
            return city;
        }
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public void setAddresses(String street, String city){
        Address address = new Address();
        address.setStreet(street);
        address.setCity(city);
        addresses.add(address);
    }

    public List<Address> getAddresses() {
        return addresses;
    }
}
