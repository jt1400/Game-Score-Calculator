/*
  The GameManager class is used to store a collection of games.
  This class use an ArrayList to store Games object.
  This class support adding new games, retrieving a specific Game by its index and removing a
  game by its index.
 */

package ca.cmpt276.assignment2.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameManager implements Iterable<Game>{
    // Create an ArrayList to store Game objects
    private List<Game> games = new ArrayList<>();
    private static GameManager instance;

    // This method add Game object to games ArrayList
    public void add(Game game) {
        games.add(game);
    }

    // This method return the number of Games stored in games
    public int getNumberOfGames(){
        return games.size();
    }

    private GameManager() {
        // Private to prevent anyone else from instantiating
    }

    public static GameManager getInstance(){
        if (instance == null){
            instance = new GameManager();
        }
        return instance;
    }



    public void updateGameIndex (Game game, int i){
        games.get(i).updateGame(game);
    }

    @Override
    public Iterator<Game> iterator() {
        return games.iterator();
    }

    // This method delete Game with index n-1 in games ArrayList
    public void delete(int n){
        // If the index is less than 0 or more than the number of games stored,
        // an IllegalArgumentException will be thrown.
        if(n < 0){
            throw new IllegalArgumentException("Please enter a value that is 0 or greater.");
        }
        else if (n > games.size()){
            throw new IllegalArgumentException("Please enter a value that is " + games.size()
                    + " or less.");
        }
        else {
            games.remove(n - 1);
        }
    }

    public  boolean isEmpty() {
        return games.isEmpty();
    }

    public Game get(int i){
        return games.get(i);
    }

}
