package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    String fragment = "";
    TextView label;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        label = (TextView) findViewById(R.id.gameStatus);
        text = (TextView) findViewById(R.id.ghostText);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            dictionary = new SimpleDictionary(inputStream);
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        onStart(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.getUnicodeChar() < 'a' || event.getUnicodeChar() > 'z'){
            label.setText("Invalid key.");
            return super.onKeyUp(keyCode, event);
        } else{
            label.setText("Valid key.");
            fragment = (fragment+event.getDisplayLabel()).toLowerCase();
            text.setText(fragment);
            computerTurn();
            return true;
        }
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        fragment = "";
        Log.e("Reset","Called");
        userTurn = random.nextBoolean();
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            Log.e("User","Turn");
            label.setText(USER_TURN);
        } else {
            Log.e("PC","Turn");
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    private void computerTurn() {

        if(fragment.length() < 4){
            char c = (char)(random.nextInt(26)+97);
            fragment = fragment + c;
            text.setText(fragment);
            userTurn = true;
            label.setText(USER_TURN);
            return;
        }

        if(dictionary.isWord(fragment) && fragment.length() >= 4){
            label.setText("Computer Wins");
            return;
        }
        else {
            if(dictionary.getAnyWordStartingWith(fragment) == null){
                Log.e("Challenged User","True");
                text.setText("No more words can be formed");
                label.setText("Computer Wins");
                return;
            }
            else if(dictionary.getAnyWordStartingWith(fragment) != null){
                fragment = fragment + dictionary.getAnyWordStartingWith(fragment).substring(0,1);
                Log.e("Computer Appended","True");
            }
        }
        // Do computer turn stuff then make it the user's turn again
        userTurn = true;
        label.setText(USER_TURN);
    }

    public void challenge(View view) {
        TextView text =(TextView)findViewById(R.id.ghostText);
        Log.e("Challenge Called","true");
        Log.e("Fragment",fragment);
        String s = dictionary.getGoodWordStartingWith(fragment);
        try {
            Log.e("Good Word",s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(dictionary.isWord(fragment) && fragment.length() >= 4){
            label.setText("User Wins");
            return;
        }
        else if(s != null){
            label.setText("Computer Wins");
            text.setText(s);
            return;
        }
        else if (s == null){
            label.setText("User Wins");
            return;
        }
    }
}
