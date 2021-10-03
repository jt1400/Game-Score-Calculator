package ca.cmpt276.assignment2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import ca.cmpt276.assignment2.model.Game;
import ca.cmpt276.assignment2.model.GameManager;

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
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private GameManager gameManager;
    private List<Game> games = new ArrayList<Game>();
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
        refreshGameManager();
    }

    private void refreshGameManager(){
        for(Game game:games){
            gameManager.add(game);
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



    class LocalDateTimeSerializer implements JsonSerializer<LocalDateTime> {
        @Override
        public JsonElement serialize(LocalDateTime localDate, Type typeOfSrc, JsonSerializationContext context) {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("d::MMM::uuuu HH::mm::ss");
            return new JsonPrimitive(localDate.format(format));
        }
        
    }

    class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime>{
        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
                return LocalDateTime.parse(json.getAsString(),
                        DateTimeFormatter.ofPattern("d::MMM::uuuu HH::mm::ss"));
        }
    }
    private void storeGamesToSharedPreferences() {
        SharedPreferences mPrefs = getSharedPreferences("shared preferences", MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
                .create();
        String json = gson.toJson(games);
        prefsEditor.putString("myGames", json);
        prefsEditor.apply();

    }

    private void getLastGamesFromSharedPreference() {
        SharedPreferences mPrefs = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer()).create();
        String json = mPrefs.getString("myGames", "");
        Type type = new TypeToken<ArrayList<Game>>() {
        }.getType();
        games = gson.fromJson(json, type);

        if (games == null) {
            games = new ArrayList<>();
        }
    }

    private void setupEmptyState(){
        TextView noGames = (TextView) findViewById(R.id.noGames);
        TextView tips = (TextView) findViewById(R.id.tips);
        ImageView arrow = (ImageView) findViewById(R.id.arrow);

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

    private void setupNewGameFab(){
        FloatingActionButton newGameFab = findViewById(R.id.floatingActionButton);
        newGameFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = NewGameActivity.makeIntent(MainActivity.this, -1, -1);
                startActivity(intent);
            }
        });
    }

    private void populateGameList(){
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

    private void populateListView(){
        adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.gameListView);
        list.setAdapter(adapter);
    }

    private void setupListViewListener(){
        ListView gamesList = (ListView) findViewById(R.id.gameListView);
        gamesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Game clickedGame = games.get(i);
//
                Intent intent = NewGameActivity.makeIntent(MainActivity.this, 1, i);
                startActivity(intent);
            }
        });

    }

    private class MyListAdapter extends ArrayAdapter<Game>{
        public MyListAdapter(){
            super(MainActivity.this, R.layout.games_view, games);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//
            View gamesView = convertView;
            if (gamesView == null){
                gamesView = getLayoutInflater().inflate(R.layout.games_view, parent, false);
            }

            Game currentGame = games.get(position);

            TextView gameWinner = (TextView) gamesView.findViewById(R.id.gameWinner);
            gameWinner.setText(currentGame.getWinner());

            TextView playerScoreTV = (TextView) gamesView.findViewById(R.id.playerScores);
            playerScoreTV.setText("Player 1 vs Player 2 scores: " + currentGame.getScores());

            DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM d @ HH:mm a");
            TextView itemDateTV = (TextView) gamesView.findViewById(R.id.gameDate);
            itemDateTV.setText(currentGame.getLocalDateTime().format(format));

            ImageView icon = (ImageView) gamesView.findViewById(R.id.resultImageView);
            icon.setImageResource(currentGame.getIconID());

            return gamesView;
        }
    }

}