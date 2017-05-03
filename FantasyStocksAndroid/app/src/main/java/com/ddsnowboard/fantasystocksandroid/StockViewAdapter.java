package com.ddsnowboard.fantasystocksandroid;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jameswk2.FantasyStocksAPI.Stock;

import java.util.ArrayList;


/**
 * This is the adapter for the stock portion of the main activity
 */
public class StockViewAdapter extends RecyclerView.Adapter<StockViewAdapter.ViewHolder> {
    public static final String TAG = StockViewAdapter.class.getSimpleName();

    private ArrayList<Stock> stocks;

    public StockViewAdapter(ArrayList<Stock> stocks) {
        this.stocks = stocks;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ConstraintLayout view = (ConstraintLayout) LayoutInflater.from(parent.getContext())
            .inflate(R.layout.fragment_stock, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.stock = stocks.get(position);
        holder.bind(holder.stock);
    }

    @Override
    public int getItemCount() {
        return stocks.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout parent;
        TextView name;
        TextView price;
        Stock stock;

        public ViewHolder(ConstraintLayout v) {
            super(v);
            parent = v;
            name = (TextView) v.findViewById(R.id.left);
            price = (TextView) v.findViewById(R.id.right);
        }

        public void bind(Stock s) {
            stock = s;
            name.setText(s.getSymbol());
            price.setText(String.valueOf(s.getPrice()));
        }
    }
}
