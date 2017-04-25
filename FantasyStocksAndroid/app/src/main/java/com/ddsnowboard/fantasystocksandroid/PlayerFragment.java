package com.ddsnowboard.fantasystocksandroid;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.jameswk2.FantasyStocksAPI.AbbreviatedPlayer;
import com.jameswk2.FantasyStocksAPI.Player;

import java.util.ArrayList;

/**
 * Created by ddsnowboard on 4/24/17.
 */

public class PlayerFragment extends Fragment {
    public static final String TAG = "PlayerFragment";
    public static final String PLAYERS = "players";
    ArrayList<Player> players = new ArrayList<>();

    StockFragment.OnListFragmentInteractionListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Gson gson = new Gson();
            Log.e(TAG, getArguments().getString(PLAYERS));
            for(Player p : gson.fromJson(getArguments().getString(PLAYERS), AbbreviatedPlayer[].class))
                players.add(p);
        }
        else
            Log.d(TAG, "Empty arguments");
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
            PlayerRecyclerAdapter adapter = new PlayerRecyclerAdapter(players, listener);
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
        listener = null;
    }
}
