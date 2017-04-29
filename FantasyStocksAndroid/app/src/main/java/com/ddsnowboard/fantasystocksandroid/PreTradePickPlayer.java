package com.ddsnowboard.fantasystocksandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import static com.ddsnowboard.fantasystocksandroid.Utilities.FLOOR_ID;
import static com.ddsnowboard.fantasystocksandroid.Utilities.UNKNOWN_ID;

public class PreTradePickPlayer extends AppCompatActivity {
    public static final String TAG = "PreTradePickPlayer";

    public static PreTradePickPlayer currentPlayerPicker;

    EditText searchBox;
    RecyclerView list;
    private ArrayList<Player> allPlayers = new ArrayList<>();
    private ArrayList<Player> currentlyShownPlayers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ***********
        // for some reason there are no players showing up in the player picker. I don't know
        // why not. It might be because it doesn't show floors or something? I need more print statements.
        super.onCreate(savedInstanceState);
        currentPlayerPicker = this;
        setContentView(R.layout.activity_pick_player);
        searchBox = (EditText) findViewById(R.id.searchBox);
        list = (RecyclerView) findViewById(R.id.recyclerView);

        Intent intent = getIntent();
        int floorId = intent.getIntExtra(FLOOR_ID, UNKNOWN_ID);
        FantasyStocksAPI api = FantasyStocksAPI.getInstance();
        Adapter adapter = new Adapter(currentlyShownPlayers);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));
        Log.d(TAG, String.valueOf(floorId));
        GetPlayersTask task = new GetPlayersTask(this,
                players -> {
                    Arrays.stream(players)
                            .filter(p -> !p.getUser().equals(api.getUser()))
                            .peek(p -> currentlyShownPlayers.add(p))
                            .forEach(p -> allPlayers.add(p));
                    adapter.notifyDataSetChanged();
                });

        task.execute(() -> floorId);


        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                currentlyShownPlayers.clear();
                if (editable.length() == 0) {
                    currentlyShownPlayers.addAll(allPlayers);
                } else {
                    String searchText = editable.toString();
                    allPlayers.stream()
                            .filter(p -> p.getUser().getUsername().contains(searchText))
                            .forEach(currentlyShownPlayers::add);

                }
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
            Log.d(TAG, "Bound " + p.getUser().getUsername());
            name.setText(p.getUser().getUsername());
            holder.setOnClickListener(view -> {
                Intent intent = new Intent(PreTradePickPlayer.this, FirstLevelTrade.class);
                intent.putExtra(Utilities.PLAYER_ID, p.getId());
                startActivity(intent);
            });
        }
    }
}
