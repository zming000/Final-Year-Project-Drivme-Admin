package com.example.finalyeardrivmeadmin;

import java.util.ArrayList;
import java.util.Arrays;

public class ModelDriverDetails {
    private static ArrayList<ModelDriverDetails> detailsArrayList;
    private final String detailsOption;

    public ModelDriverDetails(String detailsOption) {
        this.detailsOption = detailsOption;
    }

    //Initialize state array list
    public static void initState(){
        detailsArrayList = new ArrayList<>();
        String[] stateItems = new String[]{"Penang (Island)", "Penang (Mainland)", "Perak", "Selangor", "Johor"};
        Arrays.sort(stateItems);

        for (String stateItem : stateItems) {
            ModelDriverDetails state = new ModelDriverDetails(stateItem);
            detailsArrayList.add(state);
        }
    }

    public static ArrayList<ModelDriverDetails> getDetailsArrayList() {
        return detailsArrayList;
    }

    //return the name
    public static String[] detailsName(){
        String[] genderOpt = new String[detailsArrayList.size()];
        for(int i = 0; i < detailsArrayList.size(); i++){
            genderOpt[i] = detailsArrayList.get(i).detailsOption;
        }

        return genderOpt;
    }

    public String getDetailsOption() {
        return detailsOption;
    }
}
