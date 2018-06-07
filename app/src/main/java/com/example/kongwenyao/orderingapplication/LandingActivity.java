package com.example.kongwenyao.orderingapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridLayout;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class LandingActivity extends AppCompatActivity {

    GridLayout gridLayout;
    Map<String, Integer> foodItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        //Variable Assignment
        gridLayout = findViewById(R.id.gridLayout);


        ImageView imageView;
        for (int i = 0; i < foodItems.size(); i++) {

        }


        try {
            foodItems = getFoodsID();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    private Map<String, Integer> getFoodsID() throws IllegalAccessException {
        Field[] fieldIDs = R.drawable.class.getFields(); //get all id from image files within drawable
        Map<String, Integer> foodItems = new HashMap<>();
        String fileName;
        String foodName;
        int referenceID;

        for (Field id:fieldIDs) {
            fileName = id.getName(); //get image filename
            referenceID = id.getInt(null);

            if (fileName.split("_")[0].equals("item")) {
                foodName = getCapitalizedName(fileName);
                foodItems.put(foodName, referenceID);
            }

        }
        return foodItems; //return all food name and drawable reference id
    }

    private String getCapitalizedName(String name) {
        StringBuilder newName = new StringBuilder();

        String[] words = name.split("_");

        for (String word: words) {
            if (word.equals(words[words.length - 1])) { //if word equals last word
                newName.append(word.toUpperCase().charAt(0) + word.substring(1));
            } else if (!word.equalsIgnoreCase("Item")) {
                newName.append(word.toUpperCase().charAt(0) + word.substring(1) + " ");
            }
        }
        return newName.toString();
    }




}
