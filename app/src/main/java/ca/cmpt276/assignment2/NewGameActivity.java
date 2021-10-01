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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.time.format.DateTimeFormatter;

public class NewGameActivity extends AppCompatActivity {
    private static final String EXTRA_MODE = "ca.cmpt276.assignment2 - mode";
    private static final String EXTRA_POSITION = "ca.cmpt276.assignment2 - position";
    private GameManager gameManager;
    private Game game;
    private PlayerScore playerOne;
    private PlayerScore playerTwo;
    int mode;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        gameManager = GameManager.getInstance();


        TextView winner = findViewById(R.id.winner);

        TextView playerOneScoreTV = (TextView) findViewById(R.id.playerOneScore);
        TextView playerTwoScoreTV = (TextView) findViewById(R.id.playertwoScore);

        EditText playerOneNumOfCardsET = (EditText) findViewById(R.id.playerOneNumOfCards);
        EditText playerOneSumOfCardsET = (EditText) findViewById(R.id.playerOneSumOfCards);
        EditText playerOneNumOfWagerCardsET = (EditText) findViewById(R.id.playerOneNumOfWagerCards);

        EditText playerTwoNumOfCardsET = (EditText) findViewById(R.id.playerTwoNumOfCards);
        EditText playerTwoSumOfCardsET = (EditText) findViewById(R.id.playerTwoSumOfCards);
        EditText playerTwoNumOfWagerCardsET = (EditText) findViewById(R.id.playerTwoNumOfWagerCards);

        extractDataFromIntent();

        if (mode == -1) {
            game = new Game(2);
            playerOne = new PlayerScore();
            playerTwo = new PlayerScore();
            }
        else if (mode == 1) {
//            oldGame = new Game(gameManager.get(position));
            TextView score1 = (TextView) findViewById(R.id.playertwoScore);
            TextView score2 = (TextView) findViewById(R.id.playertwoScore);
            game = new Game(gameManager.get(position));
            playerOne = new PlayerScore(gameManager.get(position).getPlayer(0));
            playerTwo = new PlayerScore(gameManager.get(position).getPlayer(1));
//        else if (mode == 1){
//            game = gameManager.get(position);
//            playerOne = game.getPlayer(0);
//            playerTwo = game.getPlayer(1);
            playerOneNumOfCardsET.setText("" + playerOne.getNumberOfCards());
            playerOneSumOfCardsET.setText("" + playerOne.getSumOfCards());
            playerOneNumOfWagerCardsET.setText("" + playerOne.getNumberOfWagerCards());
            playerTwoNumOfCardsET.setText("" + playerTwo.getNumberOfCards());
            playerTwoSumOfCardsET.setText("" + playerTwo.getSumOfCards());
            playerTwoNumOfWagerCardsET.setText("" + playerTwo.getNumberOfWagerCards());
            score1.setText("" + playerOne.getScore());
            score2.setText("" + playerTwo.getScore());
            setWinner(winner);
        }


        TextView dateGameCreated = findViewById(R.id.dateGameCreated);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM d @ HH:mm a");
        dateGameCreated.setText(game.getLocalDateTime().format(format));


        playerOneNumOfCardsET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                createPlayer(playerOneNumOfCardsET, playerOneSumOfCardsET, playerOneNumOfWagerCardsET, playerOneScoreTV, playerOne);
                setWinner(winner);
            }
        });
        playerOneSumOfCardsET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                createPlayer(playerOneNumOfCardsET, playerOneSumOfCardsET, playerOneNumOfWagerCardsET, playerOneScoreTV, playerOne);
                setWinner(winner);
            }
        });
        playerOneNumOfWagerCardsET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                createPlayer(playerOneNumOfCardsET, playerOneSumOfCardsET, playerOneNumOfWagerCardsET, playerOneScoreTV, playerOne);
                setWinner(winner);
            }
        });

        playerTwoNumOfCardsET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                createPlayer(playerTwoNumOfCardsET, playerTwoSumOfCardsET, playerTwoNumOfWagerCardsET, playerTwoScoreTV, playerTwo);
                setWinner(winner);
            }
        });

        playerTwoSumOfCardsET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                createPlayer(playerTwoNumOfCardsET, playerTwoSumOfCardsET, playerTwoNumOfWagerCardsET, playerTwoScoreTV, playerTwo);
                setWinner(winner);
            }
        });

        playerTwoNumOfWagerCardsET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                createPlayer(playerTwoNumOfCardsET, playerTwoSumOfCardsET, playerTwoNumOfWagerCardsET, playerTwoScoreTV, playerTwo);
                setWinner(winner);
            }
        });


    }


    private void createPlayer(EditText num, EditText sum, EditText wager, TextView score, PlayerScore player){
        int numCards = extractIntFromEditText(num);
        int sumCards = extractIntFromEditText(sum);
        int numWager = extractIntFromEditText(wager);

        if (numCards == 0 && sumCards != -1 && numWager != -1){
            score.setText("");
        }
        else if (numCards == 0 && (sumCards <= 0 || numWager <= 0)){
            player.setNumberOfCards(numCards);
            score.setText("" + player.getScore());
        }
        else if (numCards != -1 && sumCards != -1 && numWager != -1){
            player.setNumberOfCards(numCards);
            player.setSumOfCards(sumCards);
            player.setNumberOfWagerCards(numWager);
            score.setText("" +player.getScore());
        }
        else {
            score.setText("");
        }
    }

    private void setWinner(TextView winner) {

        EditText playerOneNumOfCards = (EditText) findViewById(R.id.playerOneNumOfCards);
        EditText playerOneSumOfCards = (EditText) findViewById(R.id.playerOneSumOfCards);
        EditText playerOneNumOfWagerCards = (EditText) findViewById(R.id.playerOneNumOfWagerCards);

        EditText playerTwoNumOfCards = (EditText) findViewById(R.id.playerTwoNumOfCards);
        EditText playerTwoSumOfCards = (EditText) findViewById(R.id.playerTwoSumOfCards);
        EditText playerTwoNumOfWagerCards = (EditText) findViewById(R.id.playerTwoNumOfWagerCards);
        if(userFinishInput(playerOneNumOfCards, playerOneSumOfCards, playerOneNumOfWagerCards) && userFinishInput(playerTwoNumOfCards, playerTwoSumOfCards, playerTwoNumOfWagerCards)){
            if (playerOne.getScore() > playerTwo.getScore()) {
                winner.setText("Winner is Player One");
            }
            else if (playerOne.getScore() == playerTwo.getScore()){
                winner.setText("Tie");
            }
            else{
                winner.setText("Winner is Player Two");

            }
        }
    }

    private boolean userFinishInput(EditText a, EditText b, EditText c){
        int numCards = extractIntFromEditText(a);
        int sumCards = extractIntFromEditText(b);
        int numWager = extractIntFromEditText(c);

        if(numCards == 0 && (sumCards <= 0 || numWager <= 0))
            return true;
        else return numCards != -1 && sumCards != -1 && numWager != -1;
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
            case R.id.home:
                this.finish();
                break;
            case R.id.action_save:
                if(userFinishInput(playerOneNumOfCards, playerOneSumOfCards, playerOneNumOfWagerCards) && userFinishInput(playerTwoNumOfCards, playerTwoSumOfCards, playerTwoNumOfWagerCards)){
                    if(mode == 1){
                        gameManager.get(position);
                    }
                    else if (mode == -1) {
                        game.addPlayer(playerOne);
                        game.addPlayer(playerTwo);
                        gameManager.add(game);
                    }
                    this.finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please enter the correct information for the game", Toast.LENGTH_LONG).show();
                }
                break;

            default:
                return super.onOptionsItemSelected(item);

        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent makeIntent(Context context, int mode, int position ){
        Intent intent = new Intent(context, NewGameActivity.class);
        intent.putExtra(EXTRA_MODE, mode);
        intent.putExtra(EXTRA_POSITION, position);
        return intent;
    }

    private void extractDataFromIntent(){
        Intent intent = getIntent();

        mode = intent.getIntExtra(EXTRA_MODE, -1);
        position = intent.getIntExtra(EXTRA_POSITION, -1);
    }

    private int extractIntFromEditText(EditText e){
        if (e.getText().toString().isEmpty())
            return -1;
        return Integer.parseInt(e.getText().toString());
    }

}
