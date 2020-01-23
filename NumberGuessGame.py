from random import random

class NumberGuessGame:
    def __init__(self):
        self.randomInt = int(random()*100)
        self.attempts = 0
        self.lowerBound = 1
        self.upperBound = 100
        self.gameState = "Enter a value between 1 and 100"
        self.gameOver = False

    def guess(self,val):
        # generate some random numbers 
            try:
                guess = int(val)
                if guess < self.lowerBound or guess > self.upperBound:
                    self.gameState = "That guess is outside the bounds. Guess a number between " + str(self.lowerBound) + " and " + str(self.upperBound)
                    return
                self.attempts += 1
                if guess < self.randomInt:   #Guess too low
                    self.lowerBound = guess
                    self.gameState = "Too low! Guess a number between " + str(self.lowerBound) + " and " + str(self.upperBound)
                elif guess > self.randomInt: #Guess too low
                    self.upperBound = guess
                    self.gameState = "Too high! Guess a number between " + str(self.lowerBound) + " and " + str(self.upperBound)
                else:                   #Guess is correct
                    self.gameState = "That's the right number! Great work! It took you " + str(self.attempts) + " attempt(s) to guess it."
                    self.gameOver = True
            except ValueError:
                self.gameState = "Invalid input. Please guess a number between " + str(self.lowerBound) + " and " + str(self.upperBound)

    def getState(self):
        return self.gameState

    def getAttempts(self):
        return "You have made " + str(self.attempts) + " guesses."

    def resetGame(self):
        self.randomInt = int(random()*100)
        self.attempts = 0
        self.lowerBound = 1
        self.upperBound = 100
        self.gameState = "Enter a value between 1 and 100"
        self.gameOver = False
