/*
  The PlayerScore class is used to represent the score of a single player during a game.
  This class keep the number of cards, sum of cards and the number of wager cards a player has.
  This class support getting the score of a player.
 */

package ca.cmpt276.assignment2.model;

public class PlayerScore {
    private int numberOfCards;
    private int sumOfCards;
    private int numberOfWagerCards;

    // This method set the numberOfCards
    public void setNumberOfCards(int numberOfCards) {
        // If numberOfCards is set with 0, set sumOfCards and set NumberOfWagerCards with 0 too.
        if (numberOfCards == 0){
            this.setSumOfCards(0);
            this.setNumberOfWagerCards(0);
        }
        // If numberOfCards is set with a number less than 0, an IllegalArgumentException will be thrown
        else if (numberOfCards < 0){
            throw new IllegalArgumentException("Please enter a value that is 0 or greater.");
        }

        this.numberOfCards = numberOfCards;
    }

    // This method set the sumOfCards
    public void setSumOfCards(int sumOfCards) {
        // If sumOfCards is set with a number less than 0, an IllegalArgumentException will be thrown
        if (sumOfCards < 0){
            throw new IllegalArgumentException("Please enter a value that is 0 or greater.");
        }

        this.sumOfCards = sumOfCards;
    }

    // This method set the NumberOfWagerCards
    public void setNumberOfWagerCards(int numberOfWagerCards) {
        // If NumberOfWagerCards is set with a number less than 0, an IllegalArgumentException will be thrown
        if (numberOfWagerCards < 0){
            throw new IllegalArgumentException("Please enter a value that is 0 or greater.");
        }

        this.numberOfWagerCards = numberOfWagerCards;
    }

    // This method calculate the score of a player
    public int getScore() {
        int score;

        // If numberOfCards is 0, set score to 0
        if (numberOfCards == 0){
            score = 0;
        }
        else {
            score = (sumOfCards - 20) * (numberOfWagerCards + 1);

            if (numberOfCards >= 8) {
                score += 20;
            }
        }
        return score;
    }

    public int getNumberOfCards() {
        return numberOfCards;
    }

    public int getSumOfCards() {
        return sumOfCards;
    }

    public int getNumberOfWagerCards() {
        return numberOfWagerCards;
    }
}
