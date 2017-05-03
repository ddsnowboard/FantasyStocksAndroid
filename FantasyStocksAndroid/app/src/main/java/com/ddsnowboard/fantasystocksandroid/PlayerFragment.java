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
 * This takes care of the Player portion of the main activity
 */

public class PlayerFragment extends Fragment {
    public static final String TAG = "PlayerFragment";

    ArrayList<Player> players = new ArrayList<>();
    PlayerRecyclerAdapter adapter;

    private BroadcastReceiver receiver;

    // This is what happens when we get a broadcast to reload a floor
    private final Consumer<Player[]> receiverCallback = players -> {
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
        receiver = new FloorFragmentBroadcastReceiver<>(receiverCallback, GetPlayersTask.class);

        getContext().registerReceiver(receiver, filter);
        adapter = new PlayerRecyclerAdapter(players);

        if (getArguments() != null) {
            int playerId = getArguments().getInt(Utilities.PLAYER_ID);
            GetPlayersTask task = new GetPlayersTask(getContext(), receiverCallback);
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
}
