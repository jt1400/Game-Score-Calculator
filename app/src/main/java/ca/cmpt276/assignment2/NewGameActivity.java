package ca.cmpt276.assignment2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import ca.cmpt276.assignment2.model.Game;
import ca.cmpt276.assignment2.model.GameManager;
import ca.cmpt276.assignment2.model.PlayerScore;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.format.DateTimeFormatter;

public class NewGameActivity extends AppCompatActivity {
    private static final String EXTRA_EDIT = "ca.cmpt276.assignment2 - edit";
    private static final String EXTRA_POSITION = "ca.cmpt276.assignment2 - position";

    // Data passed from MainActivity
    boolean edit;
    int position;

    private GameManager gameManager;
    private Game game;
    private PlayerScore playerOne;
    private PlayerScore playerTwo;
    private boolean dataChanged;

    // This method support creating intent and passing data from other activity
    public static Intent makeIntent(Context context, boolean edit, int position ){
        Intent intent = new Intent(context, NewGameActivity.class);
        intent.putExtra(EXTRA_EDIT, edit);
        intent.putExtra(EXTRA_POSITION, position);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        gameManager = GameManager.getInstance();

        extractDataFromIntent();

        dataChanged = false;

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Button deleteGame = findViewById(R.id.deleteBtn);
        setupDeleteGameBtn();

        if (!edit) {
            deleteGame.setVisibility(View.GONE);

            game = new Game(2);
            playerOne = new PlayerScore();
            playerTwo = new PlayerScore();
        }
        else {
            deleteGame.setVisibility(View.VISIBLE);

            actionBar.setTitle("Edit Game Score");
            setupEditGamePage();
        }

        setupDateGameCreated();
        setupAllTextChangedListener();
    }

    // This method set the date game created textView
    private void setupDateGameCreated(){
        TextView dateGameCreated = findViewById(R.id.dateGameCreatedTextView);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM d @ HH:mm a");
        dateGameCreated.setText(game.getLocalDateTime().format(format));
    }

    // This method setup text changed listener for all EditText in NewGameActivity
    private void setupAllTextChangedListener(){
        EditText playerOneNumOfCardsET = findViewById(R.id.playerOneNumOfCards);
        EditText playerOneSumOfCardsET = findViewById(R.id.playerOneSumOfCards);
        EditText playerOneNumOfWagerCardsET = findViewById(R.id.playerOneNumOfWagerCards);

        EditText playerTwoNumOfCardsET = findViewById(R.id.playerTwoNumOfCards);
        EditText playerTwoSumOfCardsET = findViewById(R.id.playerTwoSumOfCards);
        EditText playerTwoNumOfWagerCardsET = findViewById(R.id.playerTwoNumOfWagerCards);

        setupTextChangedListener(playerOneNumOfCardsET, 1);
        setupTextChangedListener(playerOneSumOfCardsET, 1);
        setupTextChangedListener(playerOneNumOfWagerCardsET, 1);
        setupTextChangedListener(playerTwoNumOfCardsET, 2);
        setupTextChangedListener(playerTwoSumOfCardsET, 2);
        setupTextChangedListener(playerTwoNumOfWagerCardsET, 2);
    }

    // This method setup the new game activity so that user can edit game
    @SuppressLint("SetTextI18n")
    private void setupEditGamePage(){
        TextView playerOneScoreTV = findViewById(R.id.playerOneScore);
        TextView playerTwoScoreTV = findViewById(R.id.playertwoScore);

        EditText playerOneNumOfCardsET = findViewById(R.id.playerOneNumOfCards);
        EditText playerOneSumOfCardsET = findViewById(R.id.playerOneSumOfCards);
        EditText playerOneNumOfWagerCardsET = findViewById(R.id.playerOneNumOfWagerCards);

        EditText playerTwoNumOfCardsET = findViewById(R.id.playerTwoNumOfCards);
        EditText playerTwoSumOfCardsET = findViewById(R.id.playerTwoSumOfCards);
        EditText playerTwoNumOfWagerCardsET = findViewById(R.id.playerTwoNumOfWagerCards);

        game = new Game(gameManager.get(position));
        playerOne = new PlayerScore(gameManager.get(position).getPlayer(0));
        playerTwo = new PlayerScore(gameManager.get(position).getPlayer(1));

        // Set the text in EditText to match the game that user want to edit
        playerOneNumOfCardsET.setText(Integer.toString(playerOne.getNumberOfCards()));
        playerOneSumOfCardsET.setText(Integer.toString(playerOne.getSumOfCards()));
        playerOneNumOfWagerCardsET.setText(Integer.toString(playerOne.getNumberOfWagerCards()));

        playerTwoNumOfCardsET.setText(Integer.toString(playerTwo.getNumberOfCards()));
        playerTwoSumOfCardsET.setText(Integer.toString(playerTwo.getSumOfCards()));
        playerTwoNumOfWagerCardsET.setText(Integer.toString(playerTwo.getNumberOfWagerCards()));

        playerOneScoreTV.setText(Integer.toString(playerOne.getScore()));
        playerTwoScoreTV.setText(Integer.toString(playerTwo.getScore()));

        setWinner();
    }

    // This method setup the delete game button
    private void setupDeleteGameBtn(){
        Button deleteGame = findViewById(R.id.deleteBtn);
        deleteGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show dialog to confirm user want to delete game
                FragmentManager manager = getSupportFragmentManager();
                CancelDeleteFragment dialog = new CancelDeleteFragment();
                dialog.show(manager, "MessageDialog");
            }
        });
    }

    // This method delete the game chosen by user
    public void deleteGame(){
        gameManager.delete(position + 1);
        finish();
    }

    // This method setup text changed listener for EditText
    private void setupTextChangedListener(EditText editText, int playerNumber){
        TextView playerOneScoreTV = findViewById(R.id.playerOneScore);
        TextView playerTwoScoreTV = findViewById(R.id.playertwoScore);

        EditText playerOneNumOfCardsET = findViewById(R.id.playerOneNumOfCards);
        EditText playerOneSumOfCardsET = findViewById(R.id.playerOneSumOfCards);
        EditText playerOneNumOfWagerCardsET = findViewById(R.id.playerOneNumOfWagerCards);

        EditText playerTwoNumOfCardsET = findViewById(R.id.playerTwoNumOfCards);
        EditText playerTwoSumOfCardsET = findViewById(R.id.playerTwoSumOfCards);
        EditText playerTwoNumOfWagerCardsET = findViewById(R.id.playerTwoNumOfWagerCards);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                if(playerNumber == 1) {
                    createPlayer(playerOneNumOfCardsET, playerOneSumOfCardsET,
                            playerOneNumOfWagerCardsET, playerOneScoreTV, playerOne);
                }
                else if(playerNumber == 2){
                    createPlayer(playerTwoNumOfCardsET, playerTwoSumOfCardsET,
                            playerTwoNumOfWagerCardsET, playerTwoScoreTV, playerTwo);
                }
                setWinner();
                dataChanged = true;
            }
        });
    }

    // This method create a PlayerScore object
    @SuppressLint("SetTextI18n")
    private void createPlayer(EditText NumOfCardsET, EditText SumOfCardsET,
                              EditText NumOfWagerCardsET, TextView playerScoreTV, PlayerScore player){
        int numCards = extractIntFromEditText(NumOfCardsET);
        int sumCards = extractIntFromEditText(SumOfCardsET);
        int numWager = extractIntFromEditText(NumOfWagerCardsET);


        if (numCards == 0 && sumCards <= 0 && numWager <= 0) {
            player.setNumberOfCards(numCards);
            playerScoreTV.setText(Integer.toString(player.getScore()));
        }
        else if (numCards == 0 && sumCards != -1 && numWager != -1){
            playerScoreTV.setText("");
        }
        else if (numCards != -1 && sumCards != -1 && numWager != -1){
            player.setNumberOfCards(numCards);
            player.setSumOfCards(sumCards);
            player.setNumberOfWagerCards(numWager);
            playerScoreTV.setText(Integer.toString(player.getScore()));
        }
        else {
            playerScoreTV.setText("");
        }
    }

    // This method set the winner TextView with the winner of the game
    private void setWinner() {
        TextView winner = findViewById(R.id.winner);

        if(userFinishInput()){
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

    // This method save the game
    public void saveGame(){
        if (userFinishInput()) {
            game.addPlayer(playerOne);
            game.addPlayer(playerTwo);

            if (edit) {
                gameManager.updateGameIndex(game, position);
            } else {
                gameManager.add(game);
            }

            this.finish();
        } else {
            Toast.makeText(getApplicationContext(), "Please enter the correct information for the game", Toast.LENGTH_LONG).show();
        }
    }

    // This method check if the user has finish entering data for the game
    private boolean userFinishInput() {
        EditText playerOneNumOfCardsET = findViewById(R.id.playerOneNumOfCards);
        EditText playerOneSumOfCardsET = findViewById(R.id.playerOneSumOfCards);
        EditText playerOneNumOfWagerCardsET = findViewById(R.id.playerOneNumOfWagerCards);

        EditText playerTwoNumOfCardsET = findViewById(R.id.playerTwoNumOfCards);
        EditText playerTwoSumOfCardsET = findViewById(R.id.playerTwoSumOfCards);
        EditText playerTwoNumOfWagerCardsET = findViewById(R.id.playerTwoNumOfWagerCards);

        int playerOneNumberOfCards = extractIntFromEditText(playerOneNumOfCardsET);
        int playerOneSumOfCards = extractIntFromEditText(playerOneSumOfCardsET);
        int playerOneNumberOfWagerCards = extractIntFromEditText(playerOneNumOfWagerCardsET);

        int playerTwoNumberOfCards = extractIntFromEditText(playerTwoNumOfCardsET);
        int playerTwoSumOfCards = extractIntFromEditText(playerTwoSumOfCardsET);
        int playerTwoNumberOfWagerCards = extractIntFromEditText(playerTwoNumOfWagerCardsET);

        boolean playerOneDone;
        boolean playerTwoDone;

        if (playerOneNumberOfCards == 0 && playerOneSumOfCards <= 0 && playerOneNumberOfWagerCards <= 0) {
            playerOneDone = true;
        }
        else if (playerOneNumberOfCards == 0 && playerOneSumOfCards != -1 && playerOneNumberOfWagerCards != -1){
            playerOneDone = false;
        }
        else{
            playerOneDone = (playerOneNumberOfCards != -1 && playerOneSumOfCards != -1 && playerOneNumberOfWagerCards != -1);
        }

        if (playerTwoNumberOfCards == 0 && playerTwoSumOfCards <= 0 && playerTwoNumberOfWagerCards <= 0) {
            playerTwoDone = true;
        }
        else if (playerTwoNumberOfCards == 0 && playerTwoSumOfCards != -1 && playerTwoNumberOfWagerCards != -1){
            playerTwoDone = false;
        }
        else{
            playerTwoDone = (playerTwoNumberOfCards != -1 && playerTwoSumOfCards != -1 && playerTwoNumberOfWagerCards != -1);
        }

        return playerOneDone && playerTwoDone;
    }

    // This method extract number entered by user from EitText
    private int extractIntFromEditText(EditText e){
        if (e.getText().toString().isEmpty())
            return -1;
        return Integer.parseInt(e.getText().toString());
    }

    // This method get data passed from other activity
    private void extractDataFromIntent(){
        Intent intent = getIntent();

        edit = intent.getBooleanExtra(EXTRA_EDIT, false);
        position = intent.getIntExtra(EXTRA_POSITION, -1);
    }
}
