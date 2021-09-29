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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class GamesPlayedActivity extends AppCompatActivity {
    private GameManager gameManager;
    private List<Game> games = new ArrayList<Game>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_played);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        gameManager = GameManager.getInstance();
        setupNewGameFab();
//        TextView temp = findViewById(R.id.textView3);
        populateGameList();

        if (!gameManager.isEmpty()){
//            temp.setText("hah");
            populateListView();
        }

    }

    private void setupNewGameFab(){
        FloatingActionButton newGameFab = findViewById(R.id.floatingActionButton);
        newGameFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(GamesPlayedActivity.this, NewGameActivity.class);
                Intent intent = NewGameActivity.makeIntent(GamesPlayedActivity.this);
                startActivity(intent);
            }
        });
    }

    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context, GamesPlayedActivity.class);
        return intent;
    }

    private void populateGameList(){
        for (Game game: gameManager) {
            games.add(game);
        }
    }

    private void populateListView(){
        ArrayAdapter<Game> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.gameListView);
        list.setAdapter(adapter);
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