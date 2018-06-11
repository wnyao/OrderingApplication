package com.example.kongwenyao.orderingapplication.CartActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.kongwenyao.orderingapplication.ItemInfoActivity.ItemInfoActivity;
import com.example.kongwenyao.orderingapplication.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, View.OnClickListener {

    private List<String> cartList;
    private CartListAdapter adapter;
    private Button resetBtn, submitBtn;

    public static final String CART_STORAGE_TAG = "CART_LIST";
    public static final String ITEM_PREFIX_TAG = "ITEM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //Custom Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //View assignment
        resetBtn = findViewById(R.id.reset_button);
        submitBtn = findViewById(R.id.submit_button);

        //RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        cartList = new LinkedList<>(Arrays.asList(getCartList())); //Note: Array.asList returning fixed-size list; can't flexibly remove data
        adapter = new CartListAdapter(cartList);

        if (cartList.size() != 0) {
            //Recycler view setup
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            recyclerView.setAdapter(adapter);

            //Attach item touch helpers
            ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0,
                    ItemTouchHelper.LEFT, this);
            new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

            //Set button state and set event listener
            resetBtn.setVisibility(View.VISIBLE);
            resetBtn.setOnClickListener(this);
            submitBtn.setVisibility(View.VISIBLE);
            submitBtn.setOnClickListener(this);

        } else {
            //Inform user for no item in cart
            notifyForNoItem();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; adds items to the action bar
        getMenuInflater().inflate(R.menu.cart_activity_menu, menu);
        return true;
    }

    //Get cart list from sharedPreferences
    private String[] getCartList() {
        List<String> cartItems = new ArrayList<>();
        String itemKey, stringExtra;

        int totalItem = ItemInfoActivity.TOTAL_ITEM;
        SharedPreferences sharedPreferences = getSharedPreferences(CART_STORAGE_TAG, 0);

        for (int i = 1; i < totalItem + 1; i++) {
            itemKey = ITEM_PREFIX_TAG + "_" + String.valueOf(i);
            stringExtra = sharedPreferences.getString(itemKey, "");

            //If item being removed from pref list
            if (!stringExtra.equals("")) {
                cartItems.add(sharedPreferences.getString(itemKey, ""));
            }
        }
        return cartItems.toArray(new String[cartItems.size()]);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if (viewHolder instanceof CartListAdapter.ViewHolder) {
            //TODO: notify remove item name
            String foodName = cartList.get(viewHolder.getAdapterPosition()).split(",")[0];
            notifyThroughSnackBar(foodName + " has been removed from you cart");

            //Remove item from data set and shared preferences
            adapter.removeItem(position);
            removeItemFromPrefsFile(position);

            //Hide buttons if no item in cart list
            if (adapter.getItemCount() == 0) {
                resetBtn.setVisibility(View.INVISIBLE);
                submitBtn.setVisibility(View.INVISIBLE);
                notifyForNoItem();
            }
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.reset_button) {
            resetAll();
            notifyThroughSnackBar("Your cart list has been reset");

        } else if (id == R.id.submit_button) {
            resetAll();
            notifyThroughSnackBar("Your order has been submitted");
        }
    }

    //Reset to default condition
    private void resetAll() {
        resetCartList();
        adapter.resetRecyclerView();
        resetBtn.setVisibility(View.INVISIBLE);
        submitBtn.setVisibility(View.INVISIBLE);
        notifyForNoItem();
    }

    //Remove item from shared preferences file
    private void removeItemFromPrefsFile(int position) {
        String key = ITEM_PREFIX_TAG + "_" + String.valueOf(position);
        SharedPreferences sharedPreferences = getSharedPreferences(CART_STORAGE_TAG, 0);
        sharedPreferences.edit().remove(key).apply();
    }

    //Notify user for no item in cart
    private void notifyForNoItem() {
        ConstraintLayout constraintLayout = findViewById(R.id.constraitLayout);
        TextView textView = new TextView(this);

        //Layout parameters
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(layoutParams);
        textView.setTextSize(20);
        textView.setText(R.string.notify_string_no_item);
        textView.setBackgroundColor(this.getColor(R.color.colorWhite));
        textView.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);

        constraintLayout.addView(textView);
    }

    private void notifyThroughSnackBar(String msg) {
        Snackbar snackbar = Snackbar.make(this.findViewById(R.id.coordinatorLayout), msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    //Reset cart list
    public void resetCartList() {
        //Clear total Number of cart list
        SharedPreferences sharedPreferences = getSharedPreferences(ItemInfoActivity.PREFS_FILE_KEY, 0);
        sharedPreferences.edit().clear().apply();

        //Clear items in cart storage
        SharedPreferences sharedPreferences1 = getSharedPreferences(CART_STORAGE_TAG, 0);
        sharedPreferences1.edit().clear().apply();

        //Reset total item;
        ItemInfoActivity.TOTAL_ITEM = 0;
    }

}
