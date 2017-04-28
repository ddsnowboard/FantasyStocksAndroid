package com.ddsnowboard.fantasystocksandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jameswk2.FantasyStocksAPI.Floor;

import java.util.Arrays;

public class JoinFloor extends AppCompatActivity {
    public static final String TAG = "JoinFloor";
    Floor[] floors;

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_floor);
        Gson gson = new Gson();
        Intent intent = getIntent();
        floors = new Floor[0];
        Log.d(TAG, Arrays.toString(floors));
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new Adapter(floors));
    }

    class Adapter extends RecyclerView.Adapter<JoinFloor.ViewHolder> {

        Floor[] floors;
        public Adapter(Floor[] floors) {
            this.floors = floors;
        }

        @Override
        public JoinFloor.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.floor_list_container, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(JoinFloor.ViewHolder holder, int position) {
            holder.bind(floors[position]);
        }

        @Override
        public int getItemCount() {
            return floors.length;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        View parent;

        public ViewHolder(View itemView) {
            super(itemView);
            parent = itemView;
            name = (TextView) itemView.findViewById(R.id.name);
        }

        public void bind(Floor f) {
            name.setText(f.getName());
            parent.setOnClickListener(null);
            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent output = new Intent();
                    output.putExtra(FloorActivity.FLOOR, f.getId());
                    setResult(RESULT_OK, output);
                    finish();
                }
            });
        }
    }
}
