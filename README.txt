GameLogic.java handles the game. It creates objects for BotPLayer, HumanPlayer and Map.
Since BotPlayer and HumanPlayer use some functions which are the same (pickup(), look(), quit(), returnGridLook()), which are similar, but are still unique for each player I used an interface in the file HumanBot.java which includes these functions. I decided not no add move() to the interface method because I discovered that it overcomplicates my program.
In order to access maps and other variables which exist only in the GameLogic.java, I used an object in the constructors of HumanPlayer and BotPlayer, through which I could access everything I needed.
To load a map, user needs to specify the location as a parameter in GameLogic object that is created in Main.java. On my PC it is for ex. Like that: “maps/some_map.txt”, where folder maps is inside the folder with java files. So a user can also just type  “some_map.txt” if it is in the same level as other java files.
In order to read text files I used a standardized function, which requires title of map to be on row 0 of the file, number of gold on row 1, and an array to start from row 2.

