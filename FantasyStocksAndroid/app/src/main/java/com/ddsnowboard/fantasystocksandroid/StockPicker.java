package com.ddsnowboard.fantasystocksandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ddsnowboard.fantasystocksandroid.AsyncTasks.GetPlayersStocks;
import com.ddsnowboard.fantasystocksandroid.AsyncTasks.GetterTask;
import com.jameswk2.FantasyStocksAPI.Stock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is the activity that lets the user select an individual stock in the 
 * trading sequence
 */
public class StockPicker extends AppCompatActivity {
    public static final String TAG = StockPicker.class.getSimpleName();

    RecyclerView stockList;
    EditText searchBox;
    ArrayList<Stock> allStocks = new ArrayList<>();
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_picker);

        int playerId = getIntent().getIntExtra(Utilities.PLAYER_ID, Utilities.UNKNOWN_ID);
        if (playerId == Utilities.UNKNOWN_ID)
             throw new RuntimeException("You forgot to give me a playerId");

        GetterTask<Stock[]> task = new GetPlayersStocks(this,
                stocks -> {
                    Arrays.stream(stocks).forEach(allStocks::add);
                    adapter.notifyDataSetChanged();
                });
        task.execute(() -> playerId);

        stockList = (RecyclerView) findViewById(R.id.searchList);
        stockList.setLayoutManager(new LinearLayoutManager(this));
        searchBox = (EditText) findViewById(R.id.searchBox);
        adapter = new Adapter();
        adapter.setArray(allStocks);
        stockList.setAdapter(adapter);

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    ArrayList<Stock> currentStocks = new ArrayList<>(allStocks);
                    adapter.setArray(currentStocks);
                } else {
                    String searchText = editable.toString();
                    ArrayList<Stock> newList = new ArrayList<>();
                    for (Stock s : allStocks)
                        if (s.getCompanyName().toUpperCase().contains(searchText.toUpperCase()) || s.getSymbol().toUpperCase().contains(searchText.toUpperCase()))
                            newList.add(s);
                    adapter.setArray(newList);
                }
            }
        });
    }

    class Adapter extends RecyclerView.Adapter<StockPicker.StockSearchHolder> {
        List<Stock> stocks = new ArrayList<>();

        @Override
        public StockSearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.stock_viewer, parent, false);
            return new StockSearchHolder(view);
        }

        @Override
        public void onBindViewHolder(StockSearchHolder holder, int position) {
            holder.bind(stocks.get(position));
        }

        @Override
        public int getItemCount() {
            return stocks.size();
        }

        /**
         * Set the backing list of this adapter to be the given list
         * @param newArray a List of {@link Stock}s that will be set as the backing
         *                 list of this adapter
         */
        public void setArray(List<Stock> newArray) {
            this.stocks = newArray;
            notifyDataSetChanged();
        }
    }

    class StockSearchHolder extends RecyclerView.ViewHolder {
        View parent;
        TextView priceView;
        TextView nameView;

        public StockSearchHolder(View itemView) {
            super(itemView);
            parent = itemView;
            nameView = (TextView) itemView.findViewById(R.id.left);
            priceView = (TextView) itemView.findViewById(R.id.right);
        }

        public void bind(Stock s) {
            nameView.setText(s.getSymbol());
            priceView.setText(String.valueOf(s.getChange()));
            parent.setOnClickListener(view -> {
                Intent output = new Intent();
                output.putExtra(Utilities.STOCK_ID, s.getId());
                setResult(RESULT_OK, output);
                finish();
            });
        }
    }
}
