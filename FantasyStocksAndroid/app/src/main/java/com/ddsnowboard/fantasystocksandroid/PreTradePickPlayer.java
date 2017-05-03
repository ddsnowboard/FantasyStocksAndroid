package com.ddsnowboard.fantasystocksandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ddsnowboard.fantasystocksandroid.AsyncTasks.GetPlayersTask;
import com.jameswk2.FantasyStocksAPI.FantasyStocksAPI;
import com.jameswk2.FantasyStocksAPI.Player;

import java.util.ArrayList;
import java.util.Arrays;

import static com.ddsnowboard.fantasystocksandroid.Utilities.FLOOR_ID;
import static com.ddsnowboard.fantasystocksandroid.Utilities.UNKNOWN_ID;

/**
 * This chooses the other user to Trade with before the User can being actually making the Trade
 */
public class PreTradePickPlayer extends AppCompatActivity {
    public static final String TAG = "PreTradePickPlayer";

    EditText searchBox;
    RecyclerView list;
    private ArrayList<Player> allPlayers = new ArrayList<>();
    private ArrayList<Player> currentlyShownPlayers = new ArrayList<>();
    private int playerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_player);
        searchBox = (EditText) findViewById(R.id.searchBox);
        list = (RecyclerView) findViewById(R.id.recyclerView);

        Intent intent = getIntent();
        int floorId = intent.getIntExtra(FLOOR_ID, UNKNOWN_ID);
        FantasyStocksAPI api = FantasyStocksAPI.getInstance();
        Adapter adapter = new Adapter(currentlyShownPlayers);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));

        GetPlayersTask task = new GetPlayersTask(this,
                players -> {
                    // 2 = 1 floor + 1 user (the only one)
                    if (players.length == 2) {
                        Toast.makeText(PreTradePickPlayer.this, "There is no one to trade with!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    Arrays.stream(players)
                            .filter(p -> !p.getUser().equals(api.getUser()))
                            .peek(currentlyShownPlayers::add)
                            .forEach(allPlayers::add);
                    adapter.notifyDataSetChanged();
                });

        task.execute(() -> floorId);


        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

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

    @Override
    /* This calls the first level, which calls the second level, which returns a reult to the first level, which 
     * returns its result right here.  */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Utilities.MAKE_TRADE) {
            if(resultCode == RESULT_OK) {
                int[] senderStockIds = data.getIntArrayExtra(Utilities.TRADE_STOCKS_FROM_USER);
                int[] recipientStocks = data.getIntArrayExtra(Utilities.TRADE_STOCKS_TO_GIVE_USER);
                TradeActivity.sendTrade(senderStockIds, recipientStocks, playerId);
                finish();
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        View holder;


        public ViewHolder(View itemView) {
            super(itemView);
            holder = itemView;
            name = (TextView) itemView.findViewById(R.id.left);
            ((TextView) itemView.findViewById(R.id.right)).setText("");
        }

        public void bind(Player p) {
            name.setText(p.getUser().getUsername());
            holder.setOnClickListener(view -> {
                Intent intent = new Intent(PreTradePickPlayer.this, FirstLevelTrade.class);
                intent.putExtra(Utilities.PLAYER_ID, p.getId());
                playerId = p.getId();
                startActivityForResult(intent, Utilities.MAKE_TRADE);
            });
        }
    }
}
