import java.lang.Math;
import java.util.Scanner;

public class numberGuessGame {
    int randomInt;
    int attempts;
    int lowerBound;
    int upperBound;
    String gameState;

    public numberGuessGame(){
        randomInt = (int) (Math.floor(Math.random() * 100) + 1);
        attempts = 0;
        lowerBound = 0;
        upperBound = 100;
        gameState = "Enter a value between 1 and 100";
    }

    void guess(int guess){
        if (guess <= lowerBound || guess >= upperBound) {
            this.gameState = ("Your guess is not within the bounds. Try again!");
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
        }
        return;
    }
}