Prompt 1: 
I'm building a Snake game in Java using Swing. Create a single file called SnakeGame.java. It should have a main method that opens a JFrame window that is 600 by 600 pixels and titled Snake. Inside the frame, add a JPanel subclass called GamePanel. Do not add any game logic yet. Just get the window to open correctly.
Prompt 2:
Now extend SnakeGame.java. Keep it as one file. Add a dark background grid and draw a starting snake that is three segments long near the center of the board, facing right. Each cell should be a 30x30 pixel square. Draw the snake in green and the background in dark gray. Do not add movement yet.
Prompt 3:
Make the snake move automatically using a Swing timer that ticks every 150 milliseconds. Add arrow key controls so the player can steer, but don't allow the snake to reverse direction. For now, have the snake wrap around the edges instead of dying. Make sure the panel can receive keyboard input.
Prompt 4:
Add a food pellet that spawns at a random empty cell. When the snake eats it, grow by one segment and spawn new food. Add collision detection: hitting a wall or the snake's own body should end the game, stop movement, and show a "Game Over" message with the final score. Display the current score in the top-left corner during play. When the game is over, let the player press R to reset everything and play again.
Prompt 5:
I want to add a main menu of the snake game that has a start button

Add a main menu with a "Start Game" button. When clicked, switch to the game panel and start the game. Add a separate Game Over screen with the final score and a "Play Again" button that restarts the game. Make sure the game panel requests focus correctly.
Prompt 6: 
At the 'gameover' section I would like to add a m for mainmenu that takes me back to the mainmenu of the game

This added a menu option to the gameover screen that takes you back to the main menu. 

Prompt 7: 
I want to add a highscore that persists across all games

This added a high score display to the game screen and on the game over screen. 

Prompt 8: 
I would like to add sound effects for the snake eating food, the snake dying, and the game starting. 

Surpsingly this prompt would not work with the agent. It kept timing out. So I decided to try doing one sound affect at a time. 


Prompt 9:
add sound effect to when the game starts

Doing this prompt actually initiated the agent to add all the sound effects, for the snake gme starting, snake eating and snake dying. 


Prompt 10: 
I want to make the game increase difficulty every level after score 5. I want it to put up random obstacles that if you hit it, it is game over. 

From doing this it created a random obstacle every 5 points. It also increase the speed of the snake every time I level up

I think what would make it better would be if more obstacles popped up rather than just one extra obstacle every 5 points. I will prompt that next...

Prompt 11: 
I want to make it more difficult by adding more obstacles to the game. I think that what would make it more fun would be if there were more obstacles than just one extra obstacle every 5 points. 

Doing this added 2 obstacles at 5 points, then 3 more obstacles at 10, and 4 more obstacles at 15, and so on which made the game more fun and difficult. 

Prompt 12: 
I want to have the option to switch between the classic snake game and the new snake game with the obstacles. 

    Doing this added to game modes, classic and obstacles mode. However doing this I noticed the highscore is the same for both game modes so I will prompt to have seperate highscores for each game mode.

    Prompt 13: 
    I want to have seperate highscores for each game mode. 

    
