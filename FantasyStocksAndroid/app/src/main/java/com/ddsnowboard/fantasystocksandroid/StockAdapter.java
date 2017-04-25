package com.ddsnowboard.fantasystocksandroid;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jameswk2.FantasyStocksAPI.Stock;

/**
 * Created by ddsnowboard on 4/24/17.
 */

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.ViewHolder> {
    Stock[] stocks;
    public StockAdapter(Stock[] stocks) {
        super();
        this.stocks = stocks;
    }
    @Override
    public StockAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ConstraintLayout view = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_stock_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StockAdapter.ViewHolder holder, int position) {
        holder.bind(stocks[position]);
    }

    @Override
    public int getItemCount() {
        return stocks.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout parent;
        TextView name;
        TextView price;

        public ViewHolder(ConstraintLayout v) {
            super(v);
            parent = v;
            name = (TextView) v.findViewById(R.id.name);
            price = (TextView) v.findViewById(R.id.price);
        }

        public void bind(Stock s) {
            name.setText(s.getSymbol());
            price.setText(String.valueOf(s.getPrice()));
        }
    }
}
