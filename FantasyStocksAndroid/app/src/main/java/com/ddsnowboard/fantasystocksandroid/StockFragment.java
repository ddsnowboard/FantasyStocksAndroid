package com.ddsnowboard.fantasystocksandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ddsnowboard.fantasystocksandroid.AsyncTasks.GetStocksTask;
import com.jameswk2.FantasyStocksAPI.Stock;

import java.util.ArrayList;

/**
 * This is the stocks portion of the main activity
 */
public class StockFragment extends Fragment {
    public static final String TAG = "StockFragment";

    StockViewAdapter adapter;

    public ArrayList<Stock> stocks = new ArrayList<>();
    private BroadcastReceiver receiver;

    public StockFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new StockViewAdapter(stocks);

        IntentFilter filter = new IntentFilter(Utilities.LOAD_NEW_FLOOR);
        receiver = new FloorFragmentBroadcastReceiver<>(stocks -> {
            this.stocks.clear();
            for (Stock s : stocks)
                this.stocks.add(s);
            adapter.notifyDataSetChanged();
        }, GetStocksTask.class);

        getContext().registerReceiver(receiver, filter);

        if (getArguments() != null) {
            int playerId = getArguments().getInt(Utilities.PLAYER_ID);
            GetStocksTask task = new GetStocksTask(this.getContext(), stocks -> {
                for (Stock s : stocks)
                    this.stocks.add(s);
                adapter.notifyDataSetChanged();
            } );
            task.execute((() -> playerId));
        } else
            throw new RuntimeException("Empty arguments");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);

        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
        return view;
    }
}
