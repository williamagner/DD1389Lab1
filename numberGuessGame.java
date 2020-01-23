import java.lang.Math;
import java.util.Scanner;

public class NumberGuessGame {
    int randomInt;
    int attempts;
    int lowerBound;
    int upperBound;
    String gameState;
    boolean gameOver;

    public NumberGuessGame(){
        resetGame();
    }

    void guess(String strGuess){
        try{
            int guess = Integer.parseInt(strGuess);
            if (guess <= lowerBound || guess >= upperBound) {
                this.gameState = String.format("Your guess is out of bounds. Enter a value between %d and %d%n", lowerBound, upperBound);
                return;
            }
            this.attempts++;
            if (guess < randomInt) { /* Too low */
                this.lowerBound = guess;
                this.gameState = String.format("Too low! Enter a value between %d and %d%n", lowerBound, upperBound);
            } else if (guess > randomInt) { /* Too high */
                this.upperBound = guess;
                this.gameState = String.format("Too high! Enter a value between %d and %d%n", lowerBound, upperBound);
            } else { /* Right value */
                if (this.attempts == 1) {
                    this.gameState = ("Amazing! You got it right in one attempt!");
                } else {
                    this.gameState = String.format("You got it right! Great work! You did it in %d attempts%n", attempts);
                }
                gameOver = true;
            }
            return;
        }
        catch(NumberFormatException e){
            this.gameState = String.format("Invalid input! Enter a value between %d and %d%n", lowerBound, upperBound);
        }
    }

    public String getState(){
        return gameState;
    }

    public String getAttempts(){
        return "You have made " + Integer.toString(attempts) + " guesses.";
    }

    public void resetGame(){
        randomInt = (int) (Math.floor(Math.random() * 100) + 1);
        attempts = 0;
        lowerBound = 1;
        upperBound = 100;
        gameState = "Enter a value between 1 and 100";
        gameOver = false;
    }
}