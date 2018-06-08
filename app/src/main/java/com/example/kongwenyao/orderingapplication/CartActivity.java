package com.example.kongwenyao.orderingapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {


    private List<String> cartList;
    private CartListAdapter adapter;

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

        //RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        cartList = new LinkedList<>(Arrays.asList(getCartList())); //Note: Array.asList returning fixed-size list; can't flexibly remove
        adapter = new CartListAdapter(cartList);

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; adds items to the action bar
        getMenuInflater().inflate(R.menu.cart_activity_menu, menu);
        return true;
    }

    //Get cart list from sharedPreferences
    private String[] getCartList() {
        int totalItem = ItemInfoActivity.TOTAL_ITEM;
        List<String> cartItems = new ArrayList<>();
        String itemKey, stringExtra;
        SharedPreferences sharedPreferences = getSharedPreferences(CART_STORAGE_TAG, 0);

        for (int i = 1; i < totalItem + 1; i++) {
            itemKey = ITEM_PREFIX_TAG + "_" + String.valueOf(i);
            stringExtra = sharedPreferences.getString(itemKey, "");

            //If item being removed from pref list
            if (stringExtra != "") {
                cartItems.add(sharedPreferences.getString(itemKey, ""));
            }
        }

        return cartItems.toArray(new String[cartItems.size()]);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if (viewHolder instanceof CartListAdapter.ViewHolder) {
            //Get remove item name
            String foodName = cartList.get(viewHolder.getAdapterPosition()).split(",")[0];

            //Remove item from data set and shared preferences
            adapter.removeItem(position);
            removeItemFromPrefsFile(position);
        }

    }

    //Remove item from shared preferences file
    private void removeItemFromPrefsFile(int position) {
        String key = ITEM_PREFIX_TAG + "_" + String.valueOf(position);
        SharedPreferences sharedPreferences = getSharedPreferences(CART_STORAGE_TAG, 0);
        sharedPreferences.edit().remove(key).apply();

    }

}
