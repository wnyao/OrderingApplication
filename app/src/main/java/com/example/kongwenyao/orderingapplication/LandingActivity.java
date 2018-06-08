package com.example.kongwenyao.orderingapplication;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class LandingActivity extends AppCompatActivity implements View.OnClickListener {

    Map<String, Integer> foodItems;
    ItemInfoActivity itemInfoActivity;
    public static final String INTENT_FOODNAME = "CARD_NAME";
    public static final String INTENT_ID = "DRAWABLE_ID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        //Instance
        itemInfoActivity = new ItemInfoActivity();

        //Custom Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //Load initiate data
        try {
            loadData();
        } catch (IllegalAccessException | JSONException | IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        //Get intent message
        Intent intent = getIntent();
        String message = intent.getStringExtra(ItemInfoActivity.INTENT_MESSAGE);

        if (message != null) {
            //Notify user on added amount
            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent_linear_layout), message, Snackbar.LENGTH_LONG);
            snackbar.show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; adds items to the action bar
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemID = item.getItemId();

        //If cart menu item is clicked
        if (itemID == R.id.cart_menu) {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    //Load all menu data for display
    private void loadData() throws IllegalAccessException, IOException, JSONException {
        LinearLayout linearLayout = findViewById(R.id.linear_layout);
        CardView cardView;
        ImageView imageView;
        TextView textView;

        foodItems = getFoodsID();

        double foodPrice;
        for (String key: foodItems.keySet()) {
            //Get food Price
            foodPrice = itemInfoActivity.getFoodItemInfo(key, getResources());

            //Create Views
            cardView = createCardView();
            imageView = createImageView(foodItems.get(key));
            textView = createTextView(key, foodPrice); //key is the food name

            //ViewGroup add views
            cardView.addView(imageView);
            cardView.addView(textView);
            linearLayout.addView(cardView);

            cardView.setOnClickListener(this);
        }

    }

    //Get dictionary that contain all food name and its drawable reference id
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
        return foodItems;
    }

    //Get name capitalized. Name with prefix 'item'.
    private String getCapitalizedName(String name) {
        StringBuilder newName = new StringBuilder();

        String[] words = name.split("_");

        for (String word: words) {
            if (word.equals(words[words.length - 1])) { //if word is last word of the array list
                newName.append(word.toUpperCase().charAt(0)).append(word.substring(1));
            } else if (!word.equalsIgnoreCase("Item")) {
                newName.append(word.toUpperCase().charAt(0)).append(word.substring(1)).append(" ");
            }
        }
        return newName.toString();
    }

    //Create new CardView with specified layout parameters
    private CardView createCardView() {
        CardView cardView = new CardView(this);
        CardView.LayoutParams layoutParams = new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        //Layout Parameters
        layoutParams.setMargins(30, 50, 30, 0);
        cardView.setLayoutParams(layoutParams);
        cardView.setCardElevation(10);
        cardView.setRadius(10);

        return cardView;
    }

    //Create new ImageView with specified layout parameters
    private ImageView createImageView(int drawableID) {
        ImageView imageView = new ImageView(this);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500);

        //Layout Parameters
        imageView.setLayoutParams(layoutParams);
        imageView.setImageResource(drawableID);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setForeground(this.getDrawable(R.drawable.shape_gradient));

        return imageView;
    }

    //Create new TextView with specified layout parameters
    private TextView createTextView(String name, double price) {
        TextView textView = new TextView(this);

        CardView.LayoutParams layoutParams = new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        //Layout parameters
        layoutParams.setMargins(50, 0, 0, 0);
        textView.setLayoutParams(layoutParams);
        textView.setText(name + " | $" + String.valueOf(price));
        textView.setTextColor(this.getColor(R.color.colorWhite));
        textView.setTextSize(24);
        textView.setElevation(10);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);

        return textView;
    }

    @Override
    public void onClick(View v) {
        String foodName, string;
        String[] words;
        int drawableID;

        if (v instanceof CardView) {
            for (int i = 0; i < ((CardView) v).getChildCount(); i++) {
                if (((CardView) v).getChildAt(i) instanceof TextView) {
                    //Get food name
                    string = (String) ((TextView) ((CardView) v).getChildAt(i)).getText();
                    words = string.split("\\|");
                    foodName = words[0].trim(); //Trim away trailing whitespace

                    drawableID = foodItems.get(foodName);

                    //Launch food item info activity
                    Intent intent = new Intent(this, ItemInfoActivity.class);
                    intent.putExtra(INTENT_FOODNAME,foodName); //Pass food name to info activity
                    intent.putExtra(INTENT_ID, drawableID); //Pass drawable ID to info activity
                    startActivity(intent);
                }
            }
        }
    }
}
