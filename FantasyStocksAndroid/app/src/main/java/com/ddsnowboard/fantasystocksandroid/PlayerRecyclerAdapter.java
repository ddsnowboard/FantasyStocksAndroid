package com.ddsnowboard.fantasystocksandroid;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jameswk2.FantasyStocksAPI.Player;

import java.util.ArrayList;

/**
 * Created by ddsnowboard on 4/25/17.
 */

public class PlayerRecyclerAdapter extends RecyclerView.Adapter<PlayerRecyclerAdapter.ViewHolder> {
    public static final String TAG = PlayerRecyclerAdapter.class.getSimpleName();

    private ArrayList<Player> players;
    private final StockFragment.OnListFragmentInteractionListener mListener;

    public PlayerRecyclerAdapter(ArrayList<Player> player, StockFragment.OnListFragmentInteractionListener listener) {
        if (player != null)
            this.players = player;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ConstraintLayout view = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_stock, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.player = players.get(position);
        holder.bind(holder.player);

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.player);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout parent;
        TextView name;
        TextView points;
        Player player;

        public ViewHolder(ConstraintLayout v) {
            super(v);
            parent = v;
            name = (TextView) v.findViewById(R.id.name);
            points = (TextView) v.findViewById(R.id.price);
        }

        public void bind(Player p) {
            player = p;
            name.setText(p.getUser().getUsername());
            points.setText(String.valueOf(p.getPoints()));
        }
    }
}
