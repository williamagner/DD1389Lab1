def main():
    from random import random
    # generate some random numbers
    randomInt = int(random()*100)
    attempts = 0
    lowerBound = 1
    upperBound = 100
    guess = input ("Guess a number between 1 and 100\n") 
    while 1==1:
        try:
            guess = int(guess)
            if guess < lowerBound or guess > upperBound:
                guess = input("That guess is outside the bounds. Guess a number between " + str(lowerBound) + " and " + str(upperBound) + "\n")
                continue
            attempts += 1
            if guess < randomInt:   #Guess too low
                lowerBound = guess
                guess = input("Too low! Guess a number between " + str(lowerBound) + " and " + str(upperBound) + "\n")
            elif guess > randomInt: #Guess too low
                upperBound = guess
                guess = input("Too high! Guess a number between " + str(lowerBound) + " and " + str(upperBound) + "\n")
            else:                   #Guess is correct
                guess = input("That's the right number! Great work! It took you " + str(attempts) + " attempt(s) to guess it.\n")
                break

        except ValueError:
            guess = input("Please guess a number between " + str(lowerBound) + " and " + str(upperBound) + "\n")

if __name__ == '__main__':
    main()