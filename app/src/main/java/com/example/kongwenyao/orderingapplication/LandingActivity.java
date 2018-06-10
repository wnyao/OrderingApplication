package com.example.kongwenyao.orderingapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import java.net.Inet4Address;
import java.util.HashMap;
import java.util.Map;

public class LandingActivity extends AppCompatActivity implements View.OnClickListener, OnDataSendToActivity {

    private Map<String, Integer> foodItems;
    private ItemInfoActivity itemInfoActivity;

    //Intent keys
    public static final String INTENT_FOODNAME = "CARD_NAME";
    public static final String INTENT_ID = "DRAWABLE_ID";

    LinearLayout linearLayout;

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
        new DataRetrievalTask(this).execute();
        linearLayout = findViewById(R.id.linear_layout);
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

            //Remove extra
            intent.removeExtra(ItemInfoActivity.INTENT_MESSAGE);
        }

        //Get total number of item picked
        SharedPreferences sharedPreferences = getSharedPreferences(ItemInfoActivity.PREFS_FILE_KEY, 0);
        ItemInfoActivity.TOTAL_ITEM = sharedPreferences.getInt(ItemInfoActivity.TOTAL_ITEM_KEY, 0);

        //Load initiate data
        try {
            loadData();
        } catch (IllegalAccessException | JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; adds items to the action bar
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

    //Get name capitalized. Drawable name with prefix of 'item'.
    private String getProcessedName(String name) {
        StringBuilder newName = new StringBuilder();
        String[] words = name.split("_");

        for (String word: words) {
            if (word.equals(words[words.length - 1])) { //If word is last word of the array list
                newName.append(word.toUpperCase().charAt(0)).append(word.substring(1));
            } else if (!word.equalsIgnoreCase("Item")) {
                newName.append(word.toUpperCase().charAt(0)).append(word.substring(1)).append(" ");
            }
        }
        return newName.toString();
    }

    //Function for creating new CardView with specified layout parameters
    private CardView createCardView() {
        CardView cardView = new CardView(this);
        CardView.LayoutParams layoutParams = new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        //Layout Parameters
        layoutParams.setMargins(30, 50, 30, 0);
        cardView.setLayoutParams(layoutParams);
        cardView.setCardElevation(10);
        cardView.setRadius(10);

        //Set event listener
        cardView.setOnClickListener(this);

        return cardView;
    }

    //Create new ImageView with specified layout parameters
    private ImageView createImageView(int drawableID) {
        ImageView imageView = new ImageView(this);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                500);

        //Layout Parameters
        imageView.setTag(drawableID); //Set drawable ID to tag
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
        textView.setText(name + " | $" + String.format("%.2f", price));
        textView.setTextColor(this.getColor(R.color.colorWhite));
        textView.setTextSize(24);
        textView.setElevation(10);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);

        return textView;
    }

    @Override
    public void onClick(View view) {
        String vText;
        String[] words;
        String foodName = "";
        int drawableID = 0;

        if (view instanceof CardView) {
            for (int i = 0; i < ((CardView) view).getChildCount(); i++) {
                if (((CardView) view).getChildAt(i) instanceof TextView) {
                    //Get food name from text view
                    vText = (String) ((TextView) ((CardView) view).getChildAt(i)).getText();
                    words = vText.split("\\|");
                    foodName = words[0].trim(); //Trim away the trailing whitespace

                } else if (((CardView) view).getChildAt(i) instanceof ImageView) {
                    //Get drawable id from tag
                    drawableID = (int) ((CardView) view).getChildAt(i).getTag();
                }
            }

            //Launch itemInfoActivity.class with passing information
            launchInfoItemActivity(foodName, drawableID);
        }
    }

    //Function for launching InfoItemActivity.class
    private void launchInfoItemActivity(String foodName, int drawableID) {
        Intent intent = new Intent(this, ItemInfoActivity.class);
        intent.putExtra(INTENT_FOODNAME,foodName); //Pass food name to info activity
        intent.putExtra(INTENT_ID, drawableID); //Pass drawable ID to info activity
        startActivity(intent);
    }

    CardView cardView;
    ImageView imageView;
    TextView textView;
    double foodPrice = 0;
    String processedName;
    int drawableFileID; //Access by runnable

    @Override
    public void sendData(String key, int drawableID) {
        processedName = getProcessedName(key);
        drawableFileID = drawableID;

        //Get food Price
        try {
            itemInfoActivity.getFoodItemInfo(processedName, getResources());
            foodPrice = itemInfoActivity.getItemPrice();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        //Create card views
        cardView = createCardView();
        imageView = createImageView(drawableFileID);
        textView = createTextView(processedName, foodPrice); //key is the food name

        //ViewGroup add views
        cardView.addView(imageView);
        cardView.addView(textView);
        linearLayout.addView(cardView);

    }
}
