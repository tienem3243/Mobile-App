package com.example.changemoneu;

public class Nation {
    String strID;
    String strName;

    public Nation() {
    }

    public Nation(String strID, String name) {
        this.strID = strID;
        strName = name;
    }

    public String getStrID() {
        return strID;
    }

    public void setStrID(String strID) {
        this.strID = strID;
    }

    public String getStrName() {
        return strName;
    }

    public void setName(String name) {
        strName = name;
    }

    @Override
    public String toString() {
        return strName;
    }

    public static int nameInc(Nation a, Nation b){
        return a.getStrName().compareTo(b.getStrName());
    }

    public static int nameDec(Nation a, Nation b){
        return a.getStrName().compareTo(b.getStrName());
    }
}
