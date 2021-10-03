package ca.cmpt276.assignment2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import ca.cmpt276.assignment2.model.Game;
import ca.cmpt276.assignment2.model.GameManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private GameManager gameManager;
    private List<Game> games = new ArrayList<>();
    private ArrayAdapter<Game> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupNewGameFab();

        gameManager = GameManager.getInstance();

        getLastGamesFromSharedPreference();
    }

    // This method setup the new game floating action button to open the new game activity
    private void setupNewGameFab(){
        FloatingActionButton newGameFab = findViewById(R.id.NewGameFloatingActionButton);
        newGameFab.setOnClickListener(view -> {
            Intent intent = NewGameActivity.makeIntent(MainActivity.this, false, -1);
            startActivity(intent);
        });
    }

    // Code taken from https://www.javaguides.net/2019/11/gson-localdatetime-localdate.html
    // This method get the games created by user during their previous use of the app
    private void getLastGamesFromSharedPreference() {
        SharedPreferences mPrefs = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
                new LocalDateTimeDeserializer()).create();
        String json = mPrefs.getString("myGames", "");
        Type type = new TypeToken<ArrayList<Game>>() {}.getType();
        if (games == null) {
            games = new ArrayList<>();
        }

        games = gson.fromJson(json, type);

        if(gameManager.isEmpty()) {
            for (Game game : games) {
                gameManager.add(game);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        gameManager = GameManager.getInstance();

        populateGameList();
        populateListView();
        setupListViewListener();

        setupEmptyState();

        storeGamesToSharedPreferences();
    }

    // This method populate the games list with user created game from gameManager
    // This method also assign an icon for each game
    private void populateGameList(){
        // If there are games in the games list, remove all games before adding new games
        if(games != null){
            games.clear();
        }

        for (Game game: gameManager) {
            if(game.getWinner().equals("Player 1 won")){
                game.setIconID(R.drawable.ic_baseline_looks_one_24);
            }
            else if(game.getWinner().equals("Player 2 won")){
                game.setIconID(R.drawable.ic_baseline_looks_two_24);
            }
            else {
                game.setIconID(R.drawable.ic_round_drag_handle_24);
            }
            games.add(game);
        }
    }

    // This method populate the listview with the games in game list
    private void populateListView(){
        adapter = new MyListAdapter();
        ListView list = findViewById(R.id.gameListView);
        list.setAdapter(adapter);
    }

    // This method setup each item in listview to respond to user's click
    private void setupListViewListener(){
        ListView gamesListView = findViewById(R.id.gameListView);
        gamesListView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = NewGameActivity.makeIntent(MainActivity.this, true, i);
            startActivity(intent);
        });
    }

    // This method set up the empty state
    private void setupEmptyState(){
        TextView noGames = findViewById(R.id.noGamesTextView);
        TextView tips = findViewById(R.id.tipsTextView);
        ImageView arrow = findViewById(R.id.arrowImageView);

        if(gameManager.isEmpty()){
            noGames.setVisibility(View.VISIBLE);
            tips.setVisibility(View.VISIBLE);
            arrow.setVisibility(View.VISIBLE);
        }
        else {
            noGames.setVisibility(View.GONE);
            tips.setVisibility(View.GONE);
            arrow.setVisibility(View.GONE);
        }
    }

    // Code taken from https://www.javaguides.net/2019/11/gson-localdatetime-localdate.html
    // This method save the created games in games list
    private void storeGamesToSharedPreferences() {
        SharedPreferences mPrefs = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();

        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
                new LocalDateTimeSerializer()).create();
        String json = gson.toJson(games);

        prefsEditor.putString("myGames", json);
        prefsEditor.apply();
    }

    private class MyListAdapter extends ArrayAdapter<Game>{

        public MyListAdapter(){
            super(MainActivity.this, R.layout.games_view, games);
        }
        @SuppressLint("SetTextI18n")
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View gamesView = convertView;
            if (gamesView == null){
                gamesView = getLayoutInflater().inflate(R.layout.games_view, parent, false);
            }

            Game currentGame = games.get(position);

            // set up game ListView item
            TextView gameWinnerTV = gamesView.findViewById(R.id.gameWinnerTextView);
            gameWinnerTV.setText(currentGame.getWinner());

            TextView playerScoreTV = gamesView.findViewById(R.id.playerScoresTextView);
            playerScoreTV.setText("Player 1 vs Player 2 scores: " + currentGame.getScores());

            DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM d @ HH:mm a");
            TextView itemDateTV = gamesView.findViewById(R.id.gameDateTextView);
            itemDateTV.setText(currentGame.getLocalDateTime().format(format));

            ImageView iconIV = gamesView.findViewById(R.id.gameIconImageView);
            iconIV.setImageResource(currentGame.getIconID());

            return gamesView;
        }

    }

    // Code taken from https://www.javaguides.net/2019/11/gson-localdatetime-localdate.html
    // This method implement a Json serializer for LocalDateTime object
    class LocalDateTimeSerializer implements JsonSerializer<LocalDateTime>{
        @Override
        public JsonElement serialize(LocalDateTime localDate, Type typeOfSrc,
                                     JsonSerializationContext context){
            DateTimeFormatter format = DateTimeFormatter.ofPattern("d::MMM::uuuu HH::mm::ss");
            return new JsonPrimitive(localDate.format(format));
        }
    }

    // Code taken from https://www.javaguides.net/2019/11/gson-localdatetime-localdate.html
    // This method implement a Json deserializer for LocalDateTime object
    class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime>{
        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext
                context) throws JsonParseException {
            return LocalDateTime.parse(json.getAsString(),
                    DateTimeFormatter.ofPattern("d::MMM::uuuu HH::mm::ss"));
        }
    }


}