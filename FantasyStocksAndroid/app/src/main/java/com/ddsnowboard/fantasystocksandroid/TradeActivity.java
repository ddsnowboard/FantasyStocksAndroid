package com.ddsnowboard.fantasystocksandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ddsnowboard.fantasystocksandroid.AsyncTasks.GetStockTask;
import com.ddsnowboard.fantasystocksandroid.AsyncTasks.GetterTask;
import com.ddsnowboard.fantasystocksandroid.AsyncTasks.TradeUploader;
import com.jameswk2.FantasyStocksAPI.Player;
import com.jameswk2.FantasyStocksAPI.Stock;

import java.util.ArrayList;
import java.util.Arrays;

import static com.ddsnowboard.fantasystocksandroid.Utilities.GET_STOCK_FOR_TRADE;
import static com.ddsnowboard.fantasystocksandroid.Utilities.UNKNOWN_ID;


public class TradeActivity extends AppCompatActivity {
    public static final String TAG = "TradeActivity";

    private int playerId = Utilities.UNKNOWN_ID;

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
                Intent intent = new Intent(TradeActivity.this, StockPicker.class);
                if (playerId == UNKNOWN_ID) {
                    Toast.makeText(TradeActivity.this, "Please wait...", Toast.LENGTH_LONG).show();
                    return;
                }
                intent.putExtra(Utilities.PLAYER_ID, playerId);
                startActivityForResult(intent, GET_STOCK_FOR_TRADE);
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

    public void setText(@StringRes int res) {
        setText(getString(res));
    }

    public void addStock(Stock s) {
        adapter.add(s);
    }

    public ArrayList<Stock> getStocks() {
        return adapter.getObjects();
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

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if(view == null)
                view = LayoutInflater.from(getContext()).inflate(R.layout.stock_viewer,
                        parent, false);
            TextView nameView = (TextView) view.findViewById(R.id.left);
            TextView priceView = (TextView) view.findViewById(R.id.right);
            nameView.setText(getItem(position).getSymbol());
            priceView.setText(String.valueOf(getItem(position).getPrice()));
            return view;
        }
    }

    protected static void sendTrade(int[] stocksToReceive, int[] stocksToSend,
                                    int currentRecipientId) {
        // Send this trade...
        Utilities.TradeContainer tc = new Utilities.TradeContainer();
        tc.setRecipientPlayerId(currentRecipientId);
        tc.setRecipientStockIds(stocksToReceive);
        tc.setSenderStockIds(stocksToSend);
        TradeUploader tu = new TradeUploader();
        tu.execute(tc);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_STOCK_FOR_TRADE) {
            if (resultCode == RESULT_OK) {
                int stockId = data.getIntExtra(Utilities.STOCK_ID, UNKNOWN_ID);
                if(adapter.getObjects().stream().mapToInt(Stock::getId).anyMatch(i ->  i == stockId)) {
                    Toast.makeText(this, "You can't add the same stock twice!", Toast.LENGTH_LONG).show();
                }
                else {
                    GetterTask<Stock> task = new GetStockTask(this, adapter::add);
                    task.execute(() -> stockId);
                }
            }
        }
    }

    protected void setPlayer(Player p) {
        setPlayer(p.getId());
    }

    protected void setPlayer(int playerId) {
        this.playerId = playerId;
    }
}
