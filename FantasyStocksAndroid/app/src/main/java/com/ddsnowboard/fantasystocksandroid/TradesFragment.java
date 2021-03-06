package com.ddsnowboard.fantasystocksandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ddsnowboard.fantasystocksandroid.AsyncTasks.GetPlayersTradesTask;
import com.ddsnowboard.fantasystocksandroid.AsyncTasks.GetUserTask;
import com.jameswk2.FantasyStocksAPI.Player;
import com.jameswk2.FantasyStocksAPI.Stock;
import com.jameswk2.FantasyStocksAPI.Trade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * This is the part of the main activity that shows the trades
 */

public class TradesFragment extends Fragment {
    BroadcastReceiver receiver;
    final ArrayList<Trade> trades = new ArrayList<>();
    Adapter adapter;

    // This is what we do when we get a call to reload / repopulate
    Consumer<Trade[]> receivedTradesCallback = trades -> {
        TradesFragment.this.trades.clear();
        Arrays.stream(trades).forEach(TradesFragment.this.trades::add);
        adapter.notifyDataSetChanged();
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new Adapter(trades);

        IntentFilter filter = new IntentFilter(Utilities.LOAD_NEW_FLOOR);
        receiver = new FloorFragmentBroadcastReceiver<>(receivedTradesCallback, GetPlayersTradesTask.class);

        getContext().registerReceiver(receiver, filter);

        if (getArguments() != null) {
            int playerId = getArguments().getInt(Utilities.PLAYER_ID);
            GetPlayersTradesTask task = new GetPlayersTradesTask(getContext(), receivedTradesCallback);
            if (playerId != Utilities.UNKNOWN_ID)
                task.execute(() -> Player.get(playerId).getFloor().getId());
            else
                task.execute(() -> (Utilities.UNKNOWN_ID));
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

    class Adapter extends RecyclerView.Adapter<TradesFragment.ViewHolder> {
        final ArrayList<Trade> trades;

        public Adapter(ArrayList<Trade> trades) {
            this.trades = trades;
        }

        @Override
        public TradesFragment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trade_list_view, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(TradesFragment.ViewHolder holder, int position) {
            holder.bind(trades.get(position));
        }

        @Override
        public int getItemCount() {
            return trades.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout parent;
        TextView titleView;
        TextView leftList;
        TextView rightList;
        ArrayList<String> leftListArray = new ArrayList<>();
        ArrayList<String> rightListArray = new ArrayList<>();

        public ViewHolder(View itemView) {
            super(itemView);
            parent = (LinearLayout) itemView;
            titleView = (TextView) itemView.findViewById(R.id.titleView);
            leftList = (TextView) itemView.findViewById(R.id.leftList);
            rightList = (TextView) itemView.findViewById(R.id.rightList);
        }

        public void bind(Trade t) {
            GetUserTask task = new GetUserTask(parent.getContext(), user -> titleView.setText(getString(R.string.trade_from_text, user.getUsername())));
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, () -> t.getSenderPlayer().getUser().getId());

            leftListArray.clear();
            leftListArray.add("Your stocks:");
            Arrays.stream(t.getRecipientStocks()).map(Stock::getSymbol).forEach(leftListArray::add);
            // Fun fact: if you tell a ListView to take as much vertical space as wants, it
            // will show exactly one item.
            leftList.setText(leftListArray.stream().collect(Collectors.joining("\n")));

            rightListArray.clear();
            rightListArray.add("Their stocks:");
            Arrays.stream(t.getSenderStocks()).map(Stock::getSymbol).forEach(rightListArray::add);
            rightList.setText(rightListArray.stream().collect(Collectors.joining("\n")));

            // If you click on a trade, it takes you to a trade activity for it
            parent.setOnClickListener(view -> {
                Intent intent = new Intent(TradesFragment.this.getContext(), ViewTradeActivity.class);
                intent.putExtra(Utilities.TRADE_ID, t.getId());
                startActivity(intent);
            });
        }
    }
}
