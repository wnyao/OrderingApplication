package com.example.kongwenyao.orderingapplication;

import android.app.Activity;
import android.os.AsyncTask;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class DataRetrievalTask extends AsyncTask<String, Void, Map<String, Integer>> {

    private OnDataSendToActivity onDataSendToActivity;

    DataRetrievalTask(Activity activity) {
        onDataSendToActivity = (OnDataSendToActivity) activity;
    }

    @Override
    protected Map<String, Integer> doInBackground(String... strings) {
        Field[] fieldIDs = R.drawable.class.getFields(); //Get all id from image files within drawable
        Map<String, Integer> foodItems = new HashMap<>();
        String fileName;
        int referenceID = 0;

        for (Field id:fieldIDs) {
            fileName = id.getName(); //Get image filename Eg. item_tamakoyaki

            try {
                referenceID = id.getInt(null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            if (fileName.split("_")[0].equals("item")) {
                foodItems.put(fileName, referenceID);
            }

        }
        return foodItems;
    }

    @Override
    protected void onPostExecute(Map<String, Integer> foodItems) {
        super.onPostExecute(foodItems);

        for (String key: foodItems.keySet()) {
            onDataSendToActivity.sendData(key, foodItems.get(key));
        }
    }

}
