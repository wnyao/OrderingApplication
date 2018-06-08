package com.example.kongwenyao.orderingapplication;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ItemInfoActivity extends AppCompatActivity implements View.OnClickListener, NumberPicker.OnValueChangeListener {

    ImageView imageView;
    TextView titleView, priceView, descriptionView, amountTextView;
    AppCompatButton addButton;

    String itemName, itemDescription;
    int drawableID, itemAmount;
    Double itemPrice;

    public static final String INTENT_MESSAGE = "NOTICE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        //View assignment
        imageView = findViewById(R.id.image_view);
        priceView = findViewById(R.id.price_textView);
        titleView = findViewById(R.id.title_textView);
        descriptionView = findViewById(R.id.description_textView);
        amountTextView = findViewById(R.id.amount_textView);
        addButton = findViewById(R.id.addToCart_button);

        //Event Listener
        addButton.setOnClickListener(this);
        amountTextView.setOnClickListener(this);

        //Get intent messages
        Intent intent = getIntent();
        itemName = intent.getStringExtra(LandingActivity.INTENT_FOODNAME);
        drawableID = intent.getIntExtra(LandingActivity.INTENT_ID, 0);

        //Get Food Price and Description
        try {
            getFoodItemInfo(itemName, getResources());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        //Set up all information
        displaySetup();
    }

    private void displaySetup() {
        String price = "$" + Double.toString(itemPrice);
        priceView.setText(price);
        titleView.setText(itemName);
        imageView.setImageResource(drawableID);
        descriptionView.setText(itemDescription);
    }

    //Get JSON Object via reference ID of raw resources
    private JSONObject getJsonObject(int referenceID, Resources resources) throws IOException, JSONException {
        InputStream inputStream = resources.openRawResource(referenceID);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        return (new JSONObject(stringBuilder.toString()));
    }

    //Get food item information through JSON
    public double getFoodItemInfo(String itemName, Resources resources) throws IOException, JSONException {
        JSONArray itemsInfo = getJsonObject(R.raw.menu_items, resources).getJSONArray("foodItems");
        JSONObject infoObj;

        for (int i = 0; i < itemsInfo.length(); i++) {
            infoObj = itemsInfo.getJSONObject(i);

            if (infoObj.getString("name").equalsIgnoreCase(itemName)) {
                itemPrice = infoObj.getDouble("price"); //get food price
                itemDescription = infoObj.getString("description"); //get description
            }
        }

        return itemPrice;
    }

    @Override
    public void onClick(View v) {
        int viewID = v.getId();
        String message;

        if (viewID == R.id.amount_textView) {
            //Activate number picker dialog
            NumberPickerDialog numberPickerDialog = new NumberPickerDialog();
            numberPickerDialog.setValueChangeListener(this);
            numberPickerDialog.show(getFragmentManager(), "NUMBER_PICKER");

        } else if (viewID == R.id.addToCart_button) {
            message = itemAmount + " " + itemName + "has added to cart.";

            //Launch back to main menu
            Intent intent = new Intent(this, LandingActivity.class);
            intent.putExtra(INTENT_MESSAGE, message);
            startActivity(intent);

            //TODO: add to cart list
        }
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        itemAmount = newVal;
        amountTextView.setText(String.valueOf(itemAmount)); //Set display amount to choosen amount
    }
}
