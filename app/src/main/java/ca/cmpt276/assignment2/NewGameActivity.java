package ca.cmpt276.assignment2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import ca.cmpt276.assignment2.model.Game;
import ca.cmpt276.assignment2.model.GameManager;
import ca.cmpt276.assignment2.model.PlayerScore;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.format.DateTimeFormatter;

public class NewGameActivity extends AppCompatActivity {
    private GameManager gameManager;
    private Game game;
    private int a;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        gameManager = GameManager.getInstance();
        game = new Game();

        TextView dateGameCreated = findViewById(R.id.dateGameCreated);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM d @ HH:mm a");
        dateGameCreated.setText(game.getLocalDateTime().format(format));

//        TextView playerOneScore = (TextView) findViewById(R.id.playerOneScore);
//
//        EditText playerOneNumOfCards = (EditText) findViewById(R.id.playerOneNumOfCards);
//        EditText playerOneSumOfCards = (EditText) findViewById(R.id.playerOneSumOfCards);
//        EditText playerOneNumOfWagerCards = (EditText) findViewById(R.id.playerOneNumOfWagerCards);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu
        getMenuInflater().inflate(R.menu.menu_new_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        EditText playerOneNumOfCards = (EditText) findViewById(R.id.playerOneNumOfCards);
        EditText playerOneSumOfCards = (EditText) findViewById(R.id.playerOneSumOfCards);
        EditText playerOneNumOfWagerCards = (EditText) findViewById(R.id.playerOneNumOfWagerCards);

        EditText playerTwoNumOfCards = (EditText) findViewById(R.id.playerTwoNumOfCards);
        EditText playerTwoSumOfCards = (EditText) findViewById(R.id.playerTwoSumOfCards);
        EditText playerTwoNumOfWagerCards = (EditText) findViewById(R.id.playerTwoNumOfWagerCards);

        switch (item.getItemId()) {
            case R.id.action_save:
                if(createPlayer(playerOneNumOfCards, playerOneSumOfCards, playerOneNumOfWagerCards)) {
                    if(createPlayer(playerTwoNumOfCards, playerTwoSumOfCards, playerTwoNumOfWagerCards)){
                        gameManager.add(game);
                        Intent intent = new Intent(NewGameActivity.this, GamesPlayedActivity.class);
                        startActivity(intent);

                    }
                }
                break;

            default:
                return super.onOptionsItemSelected(item);

        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context, NewGameActivity.class);
        return intent;
    }

    private void extractDataFromIntent(){
        Intent intent = getIntent();
        //String personName = intent.getStringExtra(String name)
    }

    private int extractIntFromEditText(EditText e){
        if (e.getText().toString().isEmpty())
            return -1;
        return Integer.parseInt(e.getText().toString());
    }

    private boolean createPlayer(EditText numCardsET, EditText sumCardsET, EditText numWagerET){
        PlayerScore player = new PlayerScore();
        int numCards = extractIntFromEditText(numCardsET);
        int sumCards = extractIntFromEditText(sumCardsET);
        int numWager = extractIntFromEditText(numWagerET);

        if (numCards == 0 && (sumCards == -1 && numWager == -1)){
            player.setNumberOfCards(numCards);
            game.addPlayer(player);
            return true;
        }
        else if (numCards == -1 || sumCards == -1|| numWager == -1){
            Toast.makeText(getApplicationContext(), "Enter the correct game information", Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            player.setNumberOfCards(numCards);
            player.setSumOfCards(sumCards);
            player.setNumberOfWagerCards(numWager);
            game.addPlayer(player);
            return true;
        }


    }

}
