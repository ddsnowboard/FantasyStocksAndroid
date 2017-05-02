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
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class StockFragment extends Fragment {
    public static final String TAG = "StockFragment";

    MyStockRecyclerViewAdapter adapter;

    OnListFragmentInteractionListener listener;
    public ArrayList<Stock> stocks = new ArrayList<>();
    private BroadcastReceiver receiver;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StockFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new MyStockRecyclerViewAdapter(stocks, listener);

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
        if (context instanceof OnListFragmentInteractionListener) {
            listener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(receiver != null)
            getContext().unregisterReceiver(receiver);
        listener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Object o);
    }
}
