package com.ddsnowboard.fantasystocksandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ddsnowboard.fantasystocksandroid.AsyncTasks.AcceptTradeTask;
import com.ddsnowboard.fantasystocksandroid.AsyncTasks.GetFloorTask;
import com.ddsnowboard.fantasystocksandroid.AsyncTasks.GetTradeTask;
import com.ddsnowboard.fantasystocksandroid.AsyncTasks.GetUserTask;
import com.ddsnowboard.fantasystocksandroid.AsyncTasks.RejectTradeTask;
import com.jameswk2.FantasyStocksAPI.FantasyStocksAPI;
import com.jameswk2.FantasyStocksAPI.Stock;
import com.jameswk2.FantasyStocksAPI.Trade;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This is the activity that shows the individual trades to the user.
 * From here they can accept or deny them
 */
public class ViewTradeActivity extends AppCompatActivity {
    public static final String TAG = "ViewTradeActivity";
    final ArrayList<String> senderStockSymbols = new ArrayList<>();
    final ArrayList<String> recipientStockSymbols = new ArrayList<>();

    int tradeId;

    RecyclerView senderList;
    RecyclerView recipientList;
    TextView header;
    Button acceptButton;
    Button rejectButton;

    ProgressDialog bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bar = new ProgressDialog(this);
        bar.setMessage("Reasoning with server...");
        setContentView(R.layout.activity_view_trade);
        senderList = (RecyclerView) findViewById(R.id.leftList);
        recipientList = (RecyclerView) findViewById(R.id.rightList);
        header = (TextView) findViewById(R.id.tradeHeader);
        acceptButton = (Button) findViewById(R.id.acceptButton);
        rejectButton = (Button) findViewById(R.id.rejectButton);
        recipientStockSymbols.add("Your stocks: ");
        Adapter recipientAdapter = new Adapter(recipientStockSymbols);
        recipientList.setAdapter(recipientAdapter);
        recipientList.setLayoutManager(new LinearLayoutManager(this));

        senderStockSymbols.add("Their stocks: ");
        Adapter senderAdapter = new Adapter(senderStockSymbols);
        senderList.setAdapter(senderAdapter);
        senderList.setLayoutManager(new LinearLayoutManager(this));

        tradeId = getIntent().getIntExtra(Utilities.TRADE_ID, Utilities.UNKNOWN_ID);
        if (tradeId == Utilities.UNKNOWN_ID)
            throw new RuntimeException("You didn't pass in a trade!");
        bar.show();
        GetTradeTask task = new GetTradeTask(this, t -> {
            GetUserTask userTask = new GetUserTask(ViewTradeActivity.this, u -> {
                header.setText("Trade from " + u.getUsername());
            });
            // This is another instance where the way AsyncTasks are implemented means that you have to 
            // use this thread pool executor. Android isn't a really big fan of the "Yo Dawg" school of 
            // thread use.
            userTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, () -> t.getSenderPlayer().getUser().getId());

            Arrays.stream(t.getSenderStocks()).map(Stock::getSymbol).forEach(senderStockSymbols::add);
            senderAdapter.notifyDataSetChanged();

            Arrays.stream(t.getRecipientStocks()).map(Stock::getSymbol).forEach(recipientStockSymbols::add);
            recipientAdapter.notifyDataSetChanged();
            bar.hide();
        });
        task.execute(() -> tradeId);
    }

    class Adapter extends RecyclerView.Adapter<ViewTradeActivity.ViewHolder> {
        final ArrayList<String> stocks;

        public Adapter(ArrayList<String> stocks) {
            this.stocks = stocks;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_stock_view, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bind(stocks.get(position));
        }

        @Override
        public int getItemCount() {
            return stocks.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView parent;

        public ViewHolder(View itemView) {
            super(itemView);
            parent = (TextView) itemView;
        }

        public void bind(String s) {
            parent.setText(s);
        }
    }

    public void acceptTrade(View view) {
        AcceptTradeTask task = new AcceptTradeTask();
        task.execute(() -> tradeId);
        sendRefreshBroadcast();
        finish();
    }

    public void rejectTrade(View view) {
        RejectTradeTask task = new RejectTradeTask();
        task.execute(() -> tradeId);
        sendRefreshBroadcast();
        finish();
    }

    private void sendRefreshBroadcast() {
        GetFloorTask task = new GetFloorTask(this, f -> {
            Intent intent = new Intent(Utilities.LOAD_NEW_FLOOR);
            intent.putExtra(Utilities.FLOOR_ID, f.getId());
            sendBroadcast(intent);
        });
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, () -> FantasyStocksAPI.getInstance().getUser().getPlayers()[0].getFloor().getId());
    }
}
