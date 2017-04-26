package com.ddsnowboard.fantasystocksandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StockPicker extends AppCompatActivity {
    RecyclerView stockList;
    EditText searchBox;
    ArrayList<String> stocks;
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_picker);
        stocks = getIntent().getStringArrayListExtra(TradeActivity.STOCKS);
        stockList = (RecyclerView) findViewById(R.id.searchList);
        stockList.setLayoutManager(new LinearLayoutManager(this));
        searchBox = (EditText) findViewById(R.id.searchBox);
        adapter = new Adapter(stocks);
        stockList.setAdapter(adapter);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                adapter.setArray(stocks.stream().filter(s -> s.contains(editable.toString())).collect(Collectors.toList()));
            }
        });
    }

    class Adapter extends RecyclerView.Adapter<StockPicker.StockSearchHolder> {
        List<String> stocks;
        public Adapter(ArrayList<String> stocks) {
            this.stocks = stocks;
        }

        @Override
        public StockSearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.stock_search_holder, parent, false);
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

        public void setArray(List<String> newArray) {
            this.stocks = newArray;
            notifyDataSetChanged();
        }
    }

    class StockSearchHolder extends RecyclerView.ViewHolder {
        View parent;
        TextView nameView;

        public StockSearchHolder(View itemView) {
            super(itemView);
            parent = itemView;
            nameView = (TextView) itemView.findViewById(R.id.stockHolder);
        }

        public void bind(String s) {
            nameView.setText(s);
            parent.setOnClickListener(null);
            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent output = new Intent();
                    output.putExtra(TradeActivity.STOCK, nameView.getText().toString());
                    setResult(RESULT_OK, output);
                    finish();
                }
            });
        }
    }
}
