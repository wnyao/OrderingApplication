package com.example.kongwenyao.orderingapplication.cart_activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kongwenyao.orderingapplication.CartItem;
import com.example.kongwenyao.orderingapplication.R;

import java.util.LinkedList;
import java.util.List;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {

    private List<CartItem> cartItems;

    //Inner class
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView, amountTextView, priceTextView;
        public RelativeLayout relativeLayoutBack, relativeLayoutFore;

        public ViewHolder(View itemView) {
            super(itemView);

            //View assignments
            nameTextView = itemView.findViewById(R.id.name_textView);
            amountTextView = itemView.findViewById(R.id.amount_textView);
            priceTextView = itemView.findViewById(R.id.price_textView);
            relativeLayoutFore = itemView.findViewById(R.id.relative_foreground);
            relativeLayoutBack = itemView.findViewById(R.id.relative_background);
        }
    }

    //Constructor
    public CartListAdapter(List<String> cartItems) {
        this.cartItems = getCartItemObjects(cartItems);
    }

    //Turn array of string with cart item info into array of cartItem object
    private List<CartItem> getCartItemObjects(List<String> dataset) {
        List<CartItem> cartItems = new LinkedList<>();
        CartItem cartItem;

        for (String data: dataset) {
            String[] itemInfo = data.split(","); //Data of each dataset are ["tagNum, itemName, itemAmount, totalPrice"]

            cartItem = new CartItem(Integer.parseInt(itemInfo[0]), itemInfo[1], Integer.parseInt(itemInfo[2]),
                    Double.parseDouble(itemInfo[3]));

            cartItems.add(cartItem);
        }
        return cartItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CartItem item = cartItems.get(position);
        String itemPrice = "$" + String.format("%.2f", item.getTotalPrice());

        //Display cart item information
        holder.nameTextView.setText(item.getItemName());
        holder.amountTextView.setText(String.valueOf(item.getItemAmount()));
        holder.priceTextView.setText(itemPrice);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public void removeItem(int position) {
        cartItems.remove(position);
        notifyItemRemoved(position);
    }

    public int getRemoveItemTag(int position) {
        CartItem cartItem = cartItems.get(position);
        return cartItem.getTag();
    }

    public void clearRecyclerView() {
        notifyItemRangeRemoved(0, getItemCount());
        cartItems.clear();
    }

    private double calculateTotalPrice() {
        double totalPrice = 0;
        for (CartItem cartItem: cartItems) {
            totalPrice += cartItem.getTotalPrice();
        }
        return totalPrice;
    }

    public double getTotalPrice() {
        double totalPrice = calculateTotalPrice();
        return totalPrice;
    }


}
