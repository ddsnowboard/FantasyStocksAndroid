package com.ddsnowboard.fantasystocksandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ddsnowboard.fantasystocksandroid.AsyncTasks.GetPlayersTask;
import com.jameswk2.FantasyStocksAPI.FantasyStocksAPI;
import com.jameswk2.FantasyStocksAPI.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class PreTradePickPlayer extends AppCompatActivity {
    public static final String FLOOR_ID = "floorID";
    public static final int IMPOSSIBLE_VALUE = -1;

    EditText searchBox;
    RecyclerView list;
    private ArrayList<Player> allPlayers;
    private ArrayList<Player> currentlyShownPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_player);
        searchBox = (EditText) findViewById(R.id.searchBox);
        list = (RecyclerView) findViewById(R.id.recyclerView);

        Intent intent = getIntent();
        int floorId = intent.getIntExtra(FLOOR_ID, IMPOSSIBLE_VALUE);
        FantasyStocksAPI api = FantasyStocksAPI.getInstance();
        GetPlayersTask task = new GetPlayersTask(this, players -> Arrays.stream(players)
                .filter(p -> !p.getUser().equals(api.getUser()))
                .peek(p -> currentlyShownPlayers.add(p))
                .forEach(p -> allPlayers.add(p)));

        task.execute(() -> floorId);

        Adapter adapter = new Adapter(currentlyShownPlayers);
        list.setAdapter(adapter);

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                currentlyShownPlayers.clear();
                String searchText = editable.toString();
                allPlayers.stream()
                        .filter(p -> p.getUser().getUsername().contains(searchText))
                        .forEach(currentlyShownPlayers::add);

                adapter.notifyDataSetChanged();
            }
        });
    }

    class Adapter extends RecyclerView.Adapter<ViewHolder> {
        private final ArrayList<Player> players;

        public Adapter(ArrayList<Player> players) {
            this.players = players;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_stock, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bind(currentlyShownPlayers.get(position));
        }

        @Override
        public int getItemCount() {
            return currentlyShownPlayers.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        View holder;


        public ViewHolder(View itemView) {
            super(itemView);
            holder = itemView;
            name = (TextView) itemView.findViewById(R.id.left);
        }

        public void bind(Player p) {
            name.setText(p.getUser().getUsername());
            holder.setOnClickListener(view -> {
                Intent intent = new Intent(PreTradePickPlayer.this, FirstLevelTrade.class);
                // Put the player's id in, and send it off.
            });
        }
    }
}
