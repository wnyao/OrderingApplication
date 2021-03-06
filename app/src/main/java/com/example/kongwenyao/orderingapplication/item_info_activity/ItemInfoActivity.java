package com.example.kongwenyao.orderingapplication.item_info_activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.kongwenyao.orderingapplication.cart_activity.CartActivity;
import com.example.kongwenyao.orderingapplication.landing_activity.LandingActivity;
import com.example.kongwenyao.orderingapplication.MenuItem;
import com.example.kongwenyao.orderingapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ItemInfoActivity extends AppCompatActivity implements View.OnClickListener, NumberPicker.OnValueChangeListener {

    private ImageView imageView;
    private TextView titleView, priceView, descriptionView, amountTextView;

    private String itemName;
    private int drawableID, itemAmount = 1;
    private MenuItem menuItem;

    public static int TOTAL_ITEM;
    public static final String INTENT_MESSAGE = "NOTICE"; //Intent key

    //Shared preferences Keys
    public static final String PREFS_FILE_KEY = "PREFS_FILE"; //Preferences file for sharing the total of cart items
    public static final String TOTAL_ITEM_KEY = "TOTAL_ITEM"; //Key for total item

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        //View reference assignment
        imageView = findViewById(R.id.image_view);
        priceView = findViewById(R.id.price_textView);
        titleView = findViewById(R.id.title_textView);
        descriptionView = findViewById(R.id.description_textView);
        amountTextView = findViewById(R.id.amount_textView);
        AppCompatButton addButton = findViewById(R.id.addToCart_button);

        //Set event Listener
        addButton.setOnClickListener(this);
        amountTextView.setOnClickListener(this);

        //Get intent extras
        getIntentExtras();

        //Get Food Price and Description
        try {
            getFoodItemInfo(itemName, getResources());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        //Set up information related to the item
        displaySetup();



    }

    //Get item name and drawable ID for item
    private void getIntentExtras() {
        Intent intent = getIntent();
        itemName = intent.getStringExtra(LandingActivity.INTENT_FOODNAME);
        drawableID = intent.getIntExtra(LandingActivity.INTENT_ID, 0);
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Store number of total item picked
        SharedPreferences.Editor sharedPreferences = getSharedPreferences(PREFS_FILE_KEY, 0).edit();
        sharedPreferences.putInt(TOTAL_ITEM_KEY, TOTAL_ITEM);
        sharedPreferences.apply();
    }

    private void displaySetup() {
        String price = "$" + String.format("%.2f", menuItem.getItemPrice());
        priceView.setText(price);
        titleView.setText(menuItem.getItemName());
        imageView.setImageResource(menuItem.getItemDrawableID());
        descriptionView.setText(menuItem.getItemDescription());
    }

    /**
     * Get food item information through JSON
     *
     * @Parameter itemName Eg. "Goku Ramen"
     * @Parameter resources Application resources
     */
    public void getFoodItemInfo(String itemName, Resources resources) throws IOException, JSONException {
        JSONArray itemsInfo = getJsonObject(R.raw.menu_items, resources).getJSONArray("foodItems");
        JSONObject itemObj;

        for (int i = 0; i < itemsInfo.length(); i++) {
            itemObj = itemsInfo.getJSONObject(i);

            if (itemObj.getString("name").equalsIgnoreCase(itemName)) {
                double price = itemObj.getDouble("price"); //Get item price
                String description = itemObj.getString("description"); //Get item description
                menuItem = new MenuItem(itemName, price, description, drawableID); //Turn to menuItem object
                break;
            }
        }
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

    //Getter for menuItem object
    public MenuItem getMenuItem() {
        return menuItem;
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
            double totalAmount = itemAmount * menuItem.getItemPrice(); //Calculate total price of selected item
            TOTAL_ITEM += 1;

            //Add to pref file of cart list
            String tagNum = String.valueOf(TOTAL_ITEM);
            String pref_key = CartActivity.ITEM_PREFIX_TAG + "_" + String.valueOf(tagNum);
            String cart_item = tagNum + "," + menuItem.getItemName() + "," + itemAmount + "," + totalAmount;
            SharedPreferences.Editor sharedPreferences = getSharedPreferences(CartActivity.CART_STORAGE_TAG, 0).edit();
            sharedPreferences.putString(pref_key, cart_item);
            sharedPreferences.apply();

            //Launch back to main menu
            message = itemAmount + " " + menuItem.getItemName() + " has added to cart.";
            Intent intent = new Intent(this, LandingActivity.class);
            intent.putExtra(INTENT_MESSAGE, message);
            startActivity(intent);
        }
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        itemAmount = newVal;
        amountTextView.setText(String.valueOf(itemAmount)); //Set display amount to chosen amount
    }

}
