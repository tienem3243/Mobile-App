package com.example.changemoneu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class NationHR {
    List<Nation> nationList;

    public NationHR() {
        nationList = new ArrayList<>();
    }

    public NationHR(List<Nation> nationList) {
        this.nationList = nationList;
    }

    public void add(Nation nation) {
        nationList.add(nation);
    }

    public void display() {
        for (Nation e : nationList) {
//            System.out.println(e.toString());
        }
    }

    public int  getLength() {
        return nationList.size();
    }

    public List<Nation> getNationList() {
        return nationList;
    }

    public void setNationList(List<Nation> nationList) {
        this.nationList = nationList;
    }

    public List<String> getStringName() {
        List<String> list = new ArrayList<>();

        for (Nation nation : nationList) {
            String name = nation.getStrName();
            list.add(name);
        }

        return  list;
    }

    public String getName(int i) {
        return nationList.get(i).getStrName();
    }

    public String getId(String name) {
        for (Nation nation : nationList) {
            if (nation.getStrName().equalsIgnoreCase(name))
                return nation.getStrID().toLowerCase();
        }
        return null;
    }

    public void sortbyName(){
        Collections.sort(nationList, Nation::nameInc);
    }

    public List<String> findNationName(String strName) {
        List<String> arr = new ArrayList<>();
        for (Nation nation : nationList) {
            if ( nation.getStrName().toLowerCase().indexOf(strName.toLowerCase()) != -1 ) {
                System.out.println("Tim thay " + nation.getStrName() );
                arr.add(nation.getStrName());
            }
        }

        sortbyName();

        return arr;
    }
}
