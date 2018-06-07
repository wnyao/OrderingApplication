package com.example.kongwenyao.orderingapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ItemInfoActivity extends AppCompatActivity {

    ImageView imageView;
    TextView titleView, priceView, descriptionView;

    String itemName, itemDescription;
    int drawableID, itemPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        //View assignment
        imageView = findViewById(R.id.image_view);
        priceView = findViewById(R.id.price_textView);
        titleView = findViewById(R.id.title_textView);
        descriptionView = findViewById(R.id.description_textView);

        //Get intent messages
        Intent intent = getIntent();
        itemName = intent.getStringExtra(LandingActivity.INTENT_FOODNAME);
        drawableID = intent.getIntExtra(LandingActivity.INTENT_ID, 0);

        //Get Food Price and Description
        try {
            getFoodItemInfo(itemName);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        //Set up all information
        displaySetup();
    }

    private void displaySetup() {
        String price = "$" + Integer.toString(itemPrice);
        priceView.setText(price);
        titleView.setText(itemName);
        imageView.setImageResource(drawableID);
        descriptionView.setText(itemDescription);
    }

    //Get JSON Object via reference ID of raw resources
    private JSONObject getJsonObject(int referenceID) throws IOException, JSONException {
        InputStream inputStream = getResources().openRawResource(referenceID);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        return (new JSONObject(stringBuilder.toString()));
    }

    //Get food item information through JSON
    private void getFoodItemInfo(String itemName) throws IOException, JSONException {
        JSONArray itemsInfo = getJsonObject(R.raw.menu_items).getJSONArray("foodItems");
        JSONObject infoObj;

        for (int i = 0; i < itemsInfo.length(); i++) {
            infoObj = itemsInfo.getJSONObject(i);

            if (infoObj.getString("name").equalsIgnoreCase(itemName)) {
                itemPrice = infoObj.getInt("price"); //get food price
                itemDescription = infoObj.getString("description"); //get description
            }
        }
    }
}
