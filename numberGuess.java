import java.lang.Math;
import java.util.Scanner;

public class numberGuess {

    public static void main(String[] args) {
        int randomInt = (int) (Math.random() * 100);
        int attempts = 0;
        int lowerBound = 0;
        int upperBound = 100;
        Scanner inputScanner = new Scanner(System.in);
        System.out.println(randomInt);
        System.out.println("Enter a value between 0 and 100");

        while (true) {
            if (inputScanner.hasNextInt()) {
                int guess = inputScanner.nextInt();
                if (guess <= lowerBound || guess >= upperBound) {
                    System.out.println("Your guess is not within the bounds. Try again!");
                    continue;
                }
                attempts++;
                if (guess < randomInt) { /* Too low */
                    lowerBound = guess;
                    System.out.printf("Too low! Enter a value between %d and %d%n", lowerBound, upperBound);
                } else if (guess > randomInt) { /* Too high */
                    upperBound = guess;
                    System.out.printf("Too high! Enter a value between %d and %d%n", lowerBound, upperBound);

                } else { /* Right value */
                    if (attempts == 1) {
                        System.out.println("Amazing! You got it right in one attempt!");
                    } else {
                        System.out.printf("You got it right! Great work! You did it in %d attempts%n", attempts);
                    }
                    inputScanner.close();
                    return;
                }
            }
        }
    }
}