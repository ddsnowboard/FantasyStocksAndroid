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

import com.ddsnowboard.fantasystocksandroid.AsyncTasks.GetPlayersTask;
import com.jameswk2.FantasyStocksAPI.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Consumer;

/**
 * Created by ddsnowboard on 4/24/17.
 */

public class PlayerFragment extends Fragment {
    public static final String TAG = "PlayerFragment";

    ArrayList<Player> players = new ArrayList<>();
    PlayerRecyclerAdapter adapter;

    private BroadcastReceiver receiver;

    StockFragment.OnListFragmentInteractionListener listener;

    private final Consumer<Player[]> callbackFunction = players -> {
        this.players.clear();
        Arrays.stream(players).sorted(Comparator.comparingInt(p -> -p.getPoints()))
                .filter(p -> !p.isFloor())
                .forEach(p -> this.players.add(p));
        adapter.notifyDataSetChanged();
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter filter = new IntentFilter(Utilities.LOAD_NEW_FLOOR);
        receiver = new FloorFragmentBroadcastReceiver<>(callbackFunction, GetPlayersTask.class);

        getContext().registerReceiver(receiver, filter);
        adapter = new PlayerRecyclerAdapter(players, listener);

        if (getArguments() != null) {
            int playerId = getArguments().getInt(Utilities.PLAYER_ID);
            GetPlayersTask task = new GetPlayersTask(getContext(), callbackFunction);
            if (playerId != Utilities.UNKNOWN_ID)
                task.execute(() -> Player.get(playerId).getFloor().getId());
            else
                task.execute(() -> (-1));
        } else
            throw new RuntimeException("Empty arguments");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StockFragment.OnListFragmentInteractionListener) {
            listener = (StockFragment.OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (receiver != null)
            getContext().unregisterReceiver(receiver);
        listener = null;
    }
}
