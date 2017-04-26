package com.ddsnowboard.fantasystocksandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jameswk2.FantasyStocksAPI.AbbreviatedStock;
import com.jameswk2.FantasyStocksAPI.Player;
import com.jameswk2.FantasyStocksAPI.Stock;

import java.lang.reflect.Field;
import java.util.ArrayList;


public class TradeActivity extends AppCompatActivity {
    public static final String USER = "user";
    public static final int GET_STOCK = 2;
    public static final String STOCK = "stock";
    public static final String STOCKS = "stocks";
    protected static ArrayList<Stock> stocksToSend = new ArrayList<>();
    protected static ArrayList<Stock> stocksToReceive = new ArrayList<>();
    protected static Player currentRecipient;
    protected static FirstLevelTrade currentFirstLevel;

    TextView text;
    ListView list;
    Adapter adapter;
    TextView addButton;

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade);
        text = (TextView) findViewById(R.id.tradeTextView);
        list = (ListView) findViewById(R.id.stockList);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        adapter = new Adapter(getApplicationContext());
        addButton = (TextView) findViewById(R.id.stockAdder);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // You're going to have to show this dynamically too...
                Stock[] dummies = getDummyStocks();
                Intent intent = new Intent(TradeActivity.this, StockPicker.class);
                ArrayList<String> names = new ArrayList<>();
                for (Stock s : dummies) {
                    names.add(s.getSymbol());
                }
                intent.putStringArrayListExtra(STOCKS, names);
                startActivityForResult(intent, GET_STOCK);
            }
        });
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.remove(adapter.getItem(i));
            }
        });
    }

    public void setText(CharSequence s) {
        text.setText(s);
    }

    public void addStock(Stock s) {
        adapter.add(s);
    }

    class Adapter extends ArrayAdapter<Stock> {

        @LayoutRes
        private static final int LAYOUT = R.layout.floor_list_container;

        public Adapter(@NonNull Context context) {
            super(context, LAYOUT);
        }

        public ArrayList<Stock> getObjects() {
            ArrayList<Stock> retval = new ArrayList<>();
            for (int i = 0; i < this.getCount(); i++) {
                retval.add(getItem(i));
            }
            return retval;
        }
    }

    protected static void sendTrade(ArrayList<Stock> stocksToReceive, ArrayList<Stock> stocksToSend,
                                    Player currentRecipient) {
        // Send this trade...
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_STOCK) {
            if (resultCode == RESULT_OK) {
                String stockName = data.getStringExtra(STOCK);
                Toast.makeText(this, String.format("Imagine that %s showed up the list instead of Apple again", stockName),
                        Toast.LENGTH_LONG).show();
                Stock stock = getDummyStocks()[0];
                adapter.add(stock);
            }
        }
    }

    private static Stock[] getDummyStocks() {

        Stock[] dummyStocks = new Stock[]{new AbbreviatedStock(),
                new AbbreviatedStock(),
                new AbbreviatedStock()};
        try {
            Field nameField = AbbreviatedStock.class.getDeclaredField("symbol");
            nameField.setAccessible(true);
            Field priceField = AbbreviatedStock.class.getDeclaredField("price");
            priceField.setAccessible(true);


            nameField.set(dummyStocks[0], "AAPL");
            priceField.set(dummyStocks[0], 1.23);

            nameField.set(dummyStocks[1], "VZW");
            priceField.set(dummyStocks[1], 2.23);

            nameField.set(dummyStocks[2], "GOOG");
            priceField.set(dummyStocks[2], 3.23);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e.toString());
        }
        return dummyStocks;
    }
}
