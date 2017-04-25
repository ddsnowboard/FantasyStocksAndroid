package com.ddsnowboard.fantasystocksandroid;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ddsnowboard.fantasystocksandroid.StockFragment.OnListFragmentInteractionListener;
import com.jameswk2.FantasyStocksAPI.Stock;


public class MyStockRecyclerViewAdapter extends RecyclerView.Adapter<MyStockRecyclerViewAdapter.ViewHolder> {
    public static final String TAG = MyStockRecyclerViewAdapter.class.getSimpleName();

    private Stock[] stocks = new Stock[0];
    private final OnListFragmentInteractionListener mListener;

    public MyStockRecyclerViewAdapter(Stock[] stocks, OnListFragmentInteractionListener listener) {
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
        holder.stock = stocks[position];
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

    public void setStocks(Stock[] stocks) {
        this.stocks = stocks;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return stocks.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout parent;
        TextView name;
        TextView price;
        Stock stock;

        public ViewHolder(ConstraintLayout v) {
            super(v);
            parent = v;
            name = (TextView) v.findViewById(R.id.name);
            price = (TextView) v.findViewById(R.id.price);
        }

        public void bind(Stock s) {
            stock = s;
            name.setText(s.getSymbol());
            price.setText(String.valueOf(s.getPrice()));
        }
    }
}
