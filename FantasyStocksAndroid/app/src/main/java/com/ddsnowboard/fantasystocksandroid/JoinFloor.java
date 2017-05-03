package com.ddsnowboard.fantasystocksandroid;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ddsnowboard.fantasystocksandroid.AsyncTasks.CreatePlayerTask;
import com.ddsnowboard.fantasystocksandroid.AsyncTasks.GetAllFloorsTask;
import com.jameswk2.FantasyStocksAPI.FantasyStocksAPI;
import com.jameswk2.FantasyStocksAPI.Floor;
import com.jameswk2.FantasyStocksAPI.Player;
import com.jameswk2.FantasyStocksAPI.User;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This is the activity for joining a Floor
 */
public class JoinFloor extends AppCompatActivity {
    public static final String TAG = "JoinFloor";
    ArrayList<Floor> floors = new ArrayList<>();
    Adapter adapter;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_floor);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(floors);
        recyclerView.setAdapter(adapter);

        GetAllFloorsTask task = new GetAllFloorsTask(this,
                floors -> {
                    Arrays.stream(floors).forEach(JoinFloor.this.floors::add);
                    adapter.notifyDataSetChanged();
                },
                floor -> {
                    // Only get floors that the user isn't a member of yet
                    User u = FantasyStocksAPI.getInstance().getUser();
                    Player[] usersPlayers = u.getPlayers();
                    return !Arrays.stream(usersPlayers).anyMatch(p -> p.getFloor().equals(floor));
                });
        task.execute();
    }

    class Adapter extends RecyclerView.Adapter<JoinFloor.ViewHolder> {

        ArrayList<Floor> floors;

        public Adapter(ArrayList<Floor> floors) {
            this.floors = floors;
        }

        @Override
        public JoinFloor.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.floor_list_container, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(JoinFloor.ViewHolder holder, int position) {
            holder.bind(floors.get(position));
        }

        @Override
        public int getItemCount() {
            return floors.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        View parent;

        public ViewHolder(View itemView) {
            super(itemView);
            parent = itemView;
            name = (TextView) itemView.findViewById(R.id.left);
        }

        public void bind(Floor floor) {
            name.setText(floor.getName());
            parent.setOnClickListener(view -> {
                CreatePlayerTask task = new CreatePlayerTask(p -> {
                    Intent broadcast = new Intent();
                    broadcast.setAction(Utilities.LOAD_NEW_FLOOR);
                    broadcast.putExtra(Utilities.FLOOR_ID, floor.getId());
                    sendBroadcast(broadcast);
                    finish();
                });

                // Fun fact: AsyncTasks don't actually run asynchronously of each other. Which 
                // means one can block all the others. Unless you do this.
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, () -> floor.getId());
            });
        }
    }
}
