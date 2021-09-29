/*
  The Game class represent one game played by 1 to 4 players.
  This class use an ArrayList to store PlayerScores objects.
  This support setting the number of player, adding player, getting the winner of the game
  and reporting the summary of the game.
 */

package ca.cmpt276.assignment2.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Game{
    private static final int MIN_NUMBER_OF_PLAYERS = 1;
    private static final int MAX_NUMBER_OF_PLAYERS = 4;

    private int numberOfPLayers;
    private final LocalDateTime localDateTime;
    // Create an ArrayList to store PlayerScore objects
    public List<PlayerScore> playerScores = new ArrayList<>();


    public Game() {
        // Set localDateTime with the time the Game object is created
        this.localDateTime = LocalDateTime.now();
    }

    // This method set the numberOfPlayers
    public void setNumberOfPLayers(int numberOfPLayers) {
        // If numberOfPlayers is set with a number less than MIN_NUMBER_OF_PLAYERS or more than
        // MAX_NUMBER_OF_PLAYERS, an IllegalArgumentException will be thrown
        if(numberOfPLayers < MIN_NUMBER_OF_PLAYERS){
            throw new IllegalArgumentException("Please enter a value that is "
                    + MIN_NUMBER_OF_PLAYERS + " or greater.");
        }
        else if (numberOfPLayers > MAX_NUMBER_OF_PLAYERS){
            throw new IllegalArgumentException("Please enter a value that is "
                    + MAX_NUMBER_OF_PLAYERS + " or less.");
        }
        else {
            this.numberOfPLayers = numberOfPLayers;
        }
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    // This method return a string that shows the winner of the game
    public String getWinner() {
        // Set player 1 (the PlayerScore object with index 0 in playerScores ArrayList)
        // score as the temporary highest score
        int highestScore = playerScores.get(0).getScore();

        int j = 0;
        for (int i = 1; i < numberOfPLayers; i++){
            // If player with index i score is higher highestScore,
            // replace highestScore with player i+1 score
            if (playerScores.get(i).getScore() > highestScore){
                highestScore = playerScores.get(i).getScore();
                // Set j with the index of the player who has highest score
                j = i;
            }
        }

        // Winner is initiated with the player number who has the highest score
        StringBuilder winner = new StringBuilder(Integer.toString(j + 1));

        for (int i = 0; i < numberOfPLayers; i++){
            // If there are other player with the same score,
            // add the player number to the winner string
            if ((playerScores.get(i).getScore() == highestScore) && (i != j)){
                winner.append(", ").append(i + 1);
            }
        }

        return winner.toString();
    }

    // This method add a PlayerScore object to playerScore
    public void addPlayer(PlayerScore playerScore) {
        playerScores.add(playerScore);
    }

    // This method return the summary of a single Game
    @Override
    public String toString() {
        // Create a string to store the game summary
        StringBuilder gameResult = new StringBuilder();

        // A loop to add every player, excluding player 1 scores separated by vs to gameResult
        for (int i = 1; i < numberOfPLayers; i++){
            gameResult.append(" vs ");
            gameResult.append(playerScores.get(i).getScore());
        }

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Set the gameResultString with player 1's score and other players' scores
        String gameResultString = playerScores.get(0).getScore() + gameResult.toString();

        // Add the winner of the game and the date the game was created to gameResultString
        gameResultString += ", winner player(s): " + this.getWinner() + " (@"
                + this.getLocalDateTime().format(format) + ")";

        return gameResultString;
    }

}
