package com.ddsnowboard.fantasystocksandroid;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ddsnowboard.fantasystocksandroid.StockFragment.OnListFragmentInteractionListener;
import com.jameswk2.FantasyStocksAPI.Stock;

import java.util.ArrayList;


public class MyStockRecyclerViewAdapter extends RecyclerView.Adapter<MyStockRecyclerViewAdapter.ViewHolder> {
    public static final String TAG = MyStockRecyclerViewAdapter.class.getSimpleName();

    private ArrayList<Stock> stocks;
    private final OnListFragmentInteractionListener mListener;

    public MyStockRecyclerViewAdapter(ArrayList<Stock> stocks, OnListFragmentInteractionListener listener) {
        if (stocks != null)
            this.stocks = stocks;
        mListener = listener;
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

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.stock);
                }
            }
        });
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
