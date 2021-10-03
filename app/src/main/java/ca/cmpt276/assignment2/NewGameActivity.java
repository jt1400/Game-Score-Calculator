package ca.cmpt276.assignment2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import ca.cmpt276.assignment2.model.Game;
import ca.cmpt276.assignment2.model.GameManager;
import ca.cmpt276.assignment2.model.PlayerScore;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    private boolean dataChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        gameManager = GameManager.getInstance();

        extractDataFromIntent();

        dataChanged = false;

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Button deleteGame = (Button) findViewById(R.id.deleteBtn);
        setupDeleteGameBtn();

        if (mode == -1) {
            deleteGame.setVisibility(View.GONE);
            game = new Game(2);
            playerOne = new PlayerScore();
            playerTwo = new PlayerScore();

        }
        else if (mode == 1) {
            deleteGame.setVisibility(View.VISIBLE);

            ab.setTitle("Edit Game Score");
            setupEditGamePage();
        }


        setupDateGameCreated();
        setupAllTextChangedListener();

    }

    private void setupDateGameCreated(){
        TextView dateGameCreated = findViewById(R.id.dateGameCreated);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM d @ HH:mm a");
        dateGameCreated.setText(game.getLocalDateTime().format(format));
    }
    private void setupAllTextChangedListener(){
        EditText playerOneNumOfCardsET = (EditText) findViewById(R.id.playerOneNumOfCards);
        EditText playerOneSumOfCardsET = (EditText) findViewById(R.id.playerOneSumOfCards);
        EditText playerOneNumOfWagerCardsET = (EditText) findViewById(R.id.playerOneNumOfWagerCards);

        EditText playerTwoNumOfCardsET = (EditText) findViewById(R.id.playerTwoNumOfCards);
        EditText playerTwoSumOfCardsET = (EditText) findViewById(R.id.playerTwoSumOfCards);
        EditText playerTwoNumOfWagerCardsET = (EditText) findViewById(R.id.playerTwoNumOfWagerCards);

        setupTextChangedListener(playerOneNumOfCardsET, 1);
        setupTextChangedListener(playerOneSumOfCardsET, 1);
        setupTextChangedListener(playerOneNumOfWagerCardsET, 1);
        setupTextChangedListener(playerTwoNumOfCardsET, 2);
        setupTextChangedListener(playerTwoSumOfCardsET, 2);
        setupTextChangedListener(playerTwoNumOfWagerCardsET, 2);
    }

    private void setupEditGamePage(){
        TextView playerOneScoreTV = (TextView) findViewById(R.id.playerOneScore);
        TextView playerTwoScoreTV = (TextView) findViewById(R.id.playertwoScore);

        EditText playerOneNumOfCardsET = (EditText) findViewById(R.id.playerOneNumOfCards);
        EditText playerOneSumOfCardsET = (EditText) findViewById(R.id.playerOneSumOfCards);
        EditText playerOneNumOfWagerCardsET = (EditText) findViewById(R.id.playerOneNumOfWagerCards);

        EditText playerTwoNumOfCardsET = (EditText) findViewById(R.id.playerTwoNumOfCards);
        EditText playerTwoSumOfCardsET = (EditText) findViewById(R.id.playerTwoSumOfCards);
        EditText playerTwoNumOfWagerCardsET = (EditText) findViewById(R.id.playerTwoNumOfWagerCards);

        game = new Game(gameManager.get(position));
        playerOne = new PlayerScore(gameManager.get(position).getPlayer(0));
        playerTwo = new PlayerScore(gameManager.get(position).getPlayer(1));

        playerOneNumOfCardsET.setText("" + playerOne.getNumberOfCards());
        playerOneSumOfCardsET.setText("" + playerOne.getSumOfCards());
        playerOneNumOfWagerCardsET.setText("" + playerOne.getNumberOfWagerCards());
        playerTwoNumOfCardsET.setText("" + playerTwo.getNumberOfCards());
        playerTwoSumOfCardsET.setText("" + playerTwo.getSumOfCards());
        playerTwoNumOfWagerCardsET.setText("" + playerTwo.getNumberOfWagerCards());
        playerOneScoreTV.setText("" + playerOne.getScore());
        playerTwoScoreTV.setText("" + playerTwo.getScore());
        setWinner();
    }

    private void setupDeleteGameBtn(){
        Button deleteGame = (Button) findViewById(R.id.deleteBtn);
        deleteGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getSupportFragmentManager();
                CancelDeleteFragment dialog = new CancelDeleteFragment();
                dialog.show(manager, "MessageDialog");

            }
        });
    }

    public void deleteGame(){
        gameManager.delete(position + 1);
        finish();
    }

    private void setupTextChangedListener(EditText editText, int playerNumber){
        TextView playerOneScoreTV = (TextView) findViewById(R.id.playerOneScore);
        TextView playerTwoScoreTV = (TextView) findViewById(R.id.playertwoScore);

        EditText playerOneNumOfCardsET = (EditText) findViewById(R.id.playerOneNumOfCards);
        EditText playerOneSumOfCardsET = (EditText) findViewById(R.id.playerOneSumOfCards);
        EditText playerOneNumOfWagerCardsET = (EditText) findViewById(R.id.playerOneNumOfWagerCards);

        EditText playerTwoNumOfCardsET = (EditText) findViewById(R.id.playerTwoNumOfCards);
        EditText playerTwoSumOfCardsET = (EditText) findViewById(R.id.playerTwoSumOfCards);
        EditText playerTwoNumOfWagerCardsET = (EditText) findViewById(R.id.playerTwoNumOfWagerCards);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                if(playerNumber == 1) {
                    createPlayer(playerOneNumOfCardsET, playerOneSumOfCardsET, playerOneNumOfWagerCardsET, playerOneScoreTV, playerOne);
                }
                else if(playerNumber == 2){
                    createPlayer(playerTwoNumOfCardsET, playerTwoSumOfCardsET, playerTwoNumOfWagerCardsET, playerTwoScoreTV, playerTwo);
                }
                setWinner();
                dataChanged = true;
            }
        });
    }



    private void createPlayer(EditText NumOfCardsET, EditText SumOfCardsET, EditText NumOfWagerCardsET, TextView playerScoreTV, PlayerScore player){

        int numCards = extractIntFromEditText(NumOfCardsET);
        int sumCards = extractIntFromEditText(SumOfCardsET);
        int numWager = extractIntFromEditText(NumOfWagerCardsET);

        if (numCards == 0 && sumCards != -1 && numWager != -1){
            playerScoreTV.setText("");
        }
        else if (numCards == 0 && sumCards <= 0 && numWager <= 0){
            player.setNumberOfCards(numCards);
            playerScoreTV.setText("" + player.getScore());
        }
        else if (numCards != -1 && sumCards != -1 && numWager != -1){
            player.setNumberOfCards(numCards);
            player.setSumOfCards(sumCards);
            player.setNumberOfWagerCards(numWager);
            playerScoreTV.setText("" +player.getScore());
        }
        else {
            playerScoreTV.setText("");
        }
    }

    private void setWinner() {
        TextView winner = findViewById(R.id.winner);

        EditText playerOneNumOfCards = (EditText) findViewById(R.id.playerOneNumOfCards);
        EditText playerOneSumOfCards = (EditText) findViewById(R.id.playerOneSumOfCards);
        EditText playerOneNumOfWagerCards = (EditText) findViewById(R.id.playerOneNumOfWagerCards);

        EditText playerTwoNumOfCards = (EditText) findViewById(R.id.playerTwoNumOfCards);
        EditText playerTwoSumOfCards = (EditText) findViewById(R.id.playerTwoSumOfCards);
        EditText playerTwoNumOfWagerCards = (EditText) findViewById(R.id.playerTwoNumOfWagerCards);
        if(userFinishInput(playerOneNumOfCards, playerOneSumOfCards, playerOneNumOfWagerCards) && userFinishInput(playerTwoNumOfCards, playerTwoSumOfCards, playerTwoNumOfWagerCards)){
            if (playerOne.getScore() > playerTwo.getScore()) {
                winner.setText(R.string.player_one_win);
            }
            else if (playerOne.getScore() == playerTwo.getScore()){
                winner.setText(R.string.tie_game);
            }
            else{
                winner.setText(R.string.player_two_win);
            }
        }
    }

    private boolean userFinishInput(EditText a, EditText b, EditText c){
        int numCards = extractIntFromEditText(a);
        int sumCards = extractIntFromEditText(b);
        int numWager = extractIntFromEditText(c);

        if(numCards == 0 && sumCards <= 0 && numWager <= 0) {
            return true;
        }
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


        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_save:
                saveGame();
                break;

            default:
                return super.onOptionsItemSelected(item);

        }
        return super.onOptionsItemSelected(item);
    }

    public void saveGame(){
        EditText playerOneNumOfCards = (EditText) findViewById(R.id.playerOneNumOfCards);
        EditText playerOneSumOfCards = (EditText) findViewById(R.id.playerOneSumOfCards);
        EditText playerOneNumOfWagerCards = (EditText) findViewById(R.id.playerOneNumOfWagerCards);

        EditText playerTwoNumOfCards = (EditText) findViewById(R.id.playerTwoNumOfCards);
        EditText playerTwoSumOfCards = (EditText) findViewById(R.id.playerTwoSumOfCards);
        EditText playerTwoNumOfWagerCards = (EditText) findViewById(R.id.playerTwoNumOfWagerCards);
        if (userFinishInput(playerOneNumOfCards, playerOneSumOfCards, playerOneNumOfWagerCards) && userFinishInput(playerTwoNumOfCards, playerTwoSumOfCards, playerTwoNumOfWagerCards)) {
            game.addPlayer(playerOne);
            game.addPlayer(playerTwo);
            if (mode == 1) {
                gameManager.updateGameIndex(game, position);
            } else if (mode == -1) {
                gameManager.add(game);
            }
            this.finish();
        } else {
            Toast.makeText(getApplicationContext(), "Please enter the correct information for the game", Toast.LENGTH_LONG).show();
        }
    }



    @Override
    public void onBackPressed() {
        if(dataChanged){
            FragmentManager manager = getSupportFragmentManager();
            CancelEditFragment dialog = new CancelEditFragment();
            dialog.show(manager, "MessageDialog");
        }
        else {
            this.finish();
        }
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
