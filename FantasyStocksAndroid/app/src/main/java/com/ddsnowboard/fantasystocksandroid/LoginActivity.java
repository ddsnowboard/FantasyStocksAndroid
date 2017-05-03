package com.ddsnowboard.fantasystocksandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ddsnowboard.fantasystocksandroid.AsyncTasks.LoginTask;
import com.jameswk2.FantasyStocksAPI.FantasyStocksAPI;


/**
 * This is the activity that handles logging in
 */ 
public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "LoginActivity";

    EditText username;
    EditText password;
    ProgressDialog bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bar = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        bar.setTitle(R.string.loadingText);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        // This makes sure that when you click "next" on the keyboard, you go to the next text box
        username.setOnEditorActionListener((textView, i, keyEvent) -> {
            if(i == EditorInfo.IME_ACTION_NEXT) {
                username.clearFocus();
                password.requestFocus();
                return true;
            }
            return false;
        });
    }

    public void login(View v) {
        LoginTask task = new LoginTask(this);
        task.execute(username.getText().toString(), password.getText().toString());
        bar.show();
    }

    public void finishLogin(boolean result) {
        bar.hide();
        if(result) {
            SharedPreferences prefs = this.getSharedPreferences(getString(R.string.preferences), 0);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(getString(R.string.username), username.getText().toString());
            editor.putString(getString(R.string.password), password.getText().toString());
            editor.commit();
            Intent intent = new Intent(this, FloorActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Toast.makeText(this, R.string.badPassword, Toast.LENGTH_LONG).show();
        }
    }

    public void register(View v) {
        FantasyStocksAPI.getInstance().register(username.getText().toString(), password.getText().toString());
        FantasyStocksAPI.getInstance().login(username.getText().toString(), password.getText().toString());
    }
}
