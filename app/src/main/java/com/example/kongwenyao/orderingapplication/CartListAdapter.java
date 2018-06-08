package com.example.kongwenyao.orderingapplication;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {

    private List<String> dataset;

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

    public CartListAdapter(List<String> dataset) {
        this.dataset = dataset;
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
        final String[] item = dataset.get(position).split(",");
        String totalPrice = "$" + String.format("%.2f", getTotalPrice(item[1], item[2]));

        //Display cart item information
        holder.nameTextView.setText(item[0]);
        holder.amountTextView.setText(item[1]);
        holder.priceTextView.setText(totalPrice);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    //Calculate total price
    private double getTotalPrice(String amount, String price) {
        int num = Integer.parseInt(amount);
        double cost = Double.parseDouble(price);

        return (num * cost);
    }

    public void removeItem(int position) {

        Log.e("remove position", String.valueOf(position)); //

        dataset.remove(position);
        notifyItemRemoved(position);
    }
}
