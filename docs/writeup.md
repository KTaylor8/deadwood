# GUI Version Writeup

1. Declare/discuss any aspects of your code that are not working. What are your intuitions about why things are not working? What issues you already tried and ruled out? Given more time, what would you try next? Detailed answers here are critical to getting partial credit for malfunctioning programs.

   The dice stack on top of one another when they go to the same set rather than being placed next to one another. This happens because they are given the same x and y coordinates (sourced from the set’s x and y), which results in the images overlapping. Given more time we would try to keep a count of the number of players in each set that aren’t on a role and place each new player that comes to the set onto an x coordinate equal to the set’s x coordinate plus the aforementioned number times the die width. We would also need to make sure that the dice didn’t extend off of the set and instead would be moved to a new row. We could also use bufferedImage to keep the images from overlapping. We tried doing this earlier, but it gave errors that we were not able to resolve. One good thing about the dice stacking, is that although you can’t see every dice individually, you know that they are at a place because of the status’ on the sides of the screen, meaning there is no loss in interpretation. With the dice stacked it also makes the board layout compressed and easy to format in the long run so this problem had a couple of upsides.

   Given more time we might have tried to make the sides of the board more aesthetic, and maybe include the PNGs of the dice along with the player information to be more concise about who was who rather than putting a sole emphasis on the current player.

2. Any assumptions you made for things not described in the specifications.

   The assumptions we made are as follows:

   1. We assumed that it was okay to show player information on the screen but outside of the board.
   2. We assumed that the player plays with the window in full-screen mode. If we had more time, we would make the screen responsive so that the board, players, and buttons are all on the screen no matter the screen size (within reason)
   3. We assumed that it was okay for the dice faces to not match the number rolled during acting as long as we showed a popup stating the number rolled. (Professor Shri said that as long as the number(s) rolled was stated somewhere, then that was good.)
   4. We assumed that the players don’t need to enter a custom name. Instead, we populate the player names as player1, player2, etc
   5. We assumed that it wasn’t a requirement to use the area nodes in the upgrade nodes in the board.xml

3. In a few sentences, describe how you tested that your code was working.

   For most of testing, we played through the game manually in order to test that the code was working. We added shortcuts (that we then later removed) to do things like starting the players at rank 6, giving them large portions of money, and placing them in locations other than the trailers in order to facilitate testing. Another common way of debugging was through the typical route of printing to the console to make sure benchmarks were made so that the problem areas could be narrowed down. With the new incorporation of the GUI, sometimes we notified ourselves of the benchmarks using the pop up methods that were used in game.

   After we got the computer players working, we tested the code more thoroughly using no computer players, some computer players, and even all computer players.
   There were multiple problems that occurred during the manual play testing, from having the roles be inaccessible one day if they were occupied the next, to having players teleport off of the board, and having buttons just not work. Then, the computer players allowed us to find and fix errors like the upgrades attempting a divide by 0 in certain situations, takeRole() attempting to get a null role in certain situations, the deck not shuffling correctly, etc. However, we used our problem-solving skills to fix the problems we found (besides the dice stacking).

4. What was the most challenging aspect of this assignment, and why?

   There were many challenging aspects of this assignment. For example, neither of us had used Java Swing before and so it was kind of a trial by fire. Toward the beginning of A5, we had a lot of trouble getting the layered pane working and showing multiple components on one pane. Also, we found the GUI especially difficult to debug since, unlike web programming, there are no developer tools to inspect objects while the GUI is running.
   
   We also found it difficult to implement looser coupling because it involved a (important) shift in our fundamental approach to programming, and so it took a little longer for us to get used to.

5. What variant/extension of this assignment would you like to try (e.g., a variant that is more powerful, more interesting, etc).

   One implementation that would be interesting to try is having a deadwood game that is online rather than on a single computer. This way, especially during the pandemic, we could play the game with friends to show off what we have made. An online game would also have some interesting connections to real world applications, especially for those that are interested in online game design.
