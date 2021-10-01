package ca.cmpt276.assignment2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import ca.cmpt276.assignment2.model.Game;
import ca.cmpt276.assignment2.model.GameManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class GamesPlayedActivity extends AppCompatActivity {
    private GameManager gameManager;
    private List<Game> games = new ArrayList<Game>();
    private ArrayAdapter<Game> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_played);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupNewGameFab();
        setupListViewListener();
        gameManager = GameManager.getInstance();



    }

    @Override
    protected void onStart() {
        super.onStart();
        setupListViewListener();

        gameManager = GameManager.getInstance();
        TextView feature = findViewById(R.id.textView);
        feature.setText("gak kosong");
        if (!gameManager.isEmpty()){
//            temp.setText("hah");
            populateGameList();
            populateListView();

        }
        if (gameManager.isEmpty()){
            feature.setText("kosong");
        }




    }

    private void setupNewGameFab(){
        FloatingActionButton newGameFab = findViewById(R.id.floatingActionButton);
        newGameFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(GamesPlayedActivity.this, NewGameActivity.class);
                Intent intent = NewGameActivity.makeIntent(GamesPlayedActivity.this, -1, -1);
                startActivity(intent);
            }
        });
    }


    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context, GamesPlayedActivity.class);
        return intent;
    }

    private void populateGameList(){
        if(games != null){
            games.clear();
        }
        for (Game game: gameManager) {
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
                Intent intent = NewGameActivity.makeIntent(GamesPlayedActivity.this, 1, i);
                startActivity(intent);
            }
        });

    }

    private class MyListAdapter extends ArrayAdapter<Game>{
        public MyListAdapter(){
            super(GamesPlayedActivity.this, R.layout.games, games);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//
            View gamesView = convertView;
            if (gamesView == null){
                gamesView = getLayoutInflater().inflate(R.layout.games, parent, false);
            }

            Game currentGame = games.get(position);

            TextView gameDescription = (TextView) gamesView.findViewById(R.id.gamedescription);
            gameDescription.setText(currentGame.toString());

            return gamesView;
//            return super.getView(position, convertView, parent);
        }
    }

}