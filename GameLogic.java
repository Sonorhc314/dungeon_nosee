/**
 * Contains the main logic part of the game, as it processes.
 *
 */
public class GameLogic {
	
	/* Reference to the map being used */
	private Map map;
	/*the user prompt analysing class */
	private HumanPlayer player;
	private BotPlayer bot;
	private final int required_gold; //all gold on map
	private int current_gold = 0; //increases as player picks up gold
	private char[][] change_map; //map where bot and player run and change position
	private char[][] original_map;
	String mapname_file="";//nothing -> default map
	String mapname_title;
	//---------------------
	//coordinates for a human player
	private int current_row;
	private int current_col;
	//coordinates for a bot player
	private int current_row_bot;
	private int current_col_bot;
	//---------------------


	public GameLogic() {//contstructor
		this("");//default noname constr. for map
	}

	public GameLogic(String mapname_file) {//contstructor
		if(mapname_file.length()==0)
		{
			map = new Map();
		}
		else
		{
			map = new Map(mapname_file);
			this.mapname_file = mapname_file;
		}
		mapname_title = map.getMapName();
		required_gold = map.getGold();

		original_map = map.returnMap();
		change_map = new char[original_map.length][original_map[0].length];
		for (int i = 0; i < original_map.length; i++) { //copies original_arrays
        	System.arraycopy(original_map[i], 0, change_map[i], 0, original_map[i].length);
    	}
		//all_gold = allGold();
		initializePlayerBot('P', false); //initialize player
		initializePlayerBot('B', true); //initialize bot
	}

	public void initializePlayerBot(char player_bot, Boolean isbot)//initializes a bot
	{
		while(true)
		{
			//randomly picks indexes for a player
			int rand_row = (int)(Math.random()*change_map.length);
			int rand_col = (int)(Math.random()*change_map[0].length);
			if((change_map[rand_row][rand_col]=='.' || change_map[rand_row][rand_col]=='E'))
			{
				change_map[rand_row][rand_col] = player_bot;//sets player('P'/'B') on a map
				if(!isbot)//checks who is a player
				{//assigns coordinates to a player
					current_row = rand_row;
					current_col = rand_col;
					break;
				}
				else//assigns coordinates to bot
				{
					current_row_bot = rand_row;
					current_col_bot = rand_col;
					break;
				}
			}
		}
	}

    /**
	 * Checks if the game is running
	 *
     * @return if the game is running.
     */
    public boolean gameRunning() {
        return false;
    }

	public void runGame()
	{
		player = new HumanPlayer(this); //constructor inside HumanPlayer that creates GameLogic object
		bot = new BotPlayer(this); 
		System.out.println("name is " + mapname_title);
		while(true)
		{
			player.humanplayer_start(); //will prompt with scanner and process commmand
			bot.botplayerStart();
		}
	}

	// final int allGold() //counts all occurances of G on the map
	// {
	// 	int all_gold = 0;
	// 	for (int i = 0; i<original_map.length; i++) {
	// 		for (int j = 0; j<original_map[0].length; j++){
	// 			if(original_map[i][j]=='G')
	// 			{all_gold++;}
	// 		}
	// 	}
	// 	return all_gold;
	// }
	public void returnChangeMap()
	{
		for (int i = 0; i<change_map.length; i++) {
			System.out.print(change_map[i]);
			System.out.println();
		}
	}

	public char[][] returnGridLook() //shows 5x5 grid with player in the center
	{
		int range_lookup = 5;
		char[][] gridArray = new char[range_lookup][range_lookup];
		for(int i=0, i_changeMap=-2; i<range_lookup; i++, i_changeMap++)
		{
			for(int j=0, j_changeMap=-2; j<range_lookup; j++, j_changeMap++)
			{
				boolean inBoundsRow = (i_changeMap+current_row >= 0) && (i_changeMap+current_row  < change_map.length);
				boolean inBoundsCol = (j_changeMap+current_col  >= 0) && (j_changeMap+current_col  < change_map[0].length);
				if(inBoundsCol && inBoundsRow)
				{
					gridArray[i][j] = change_map[current_row+i_changeMap][current_col+j_changeMap];
				}
				else
				{
					gridArray[i][j] = '#';
				}
				System.out.print(gridArray[i][j]);
			}
			System.out.println();
		} 
		return gridArray;
	}

	public void PrintMap()
	{
		map.readMap();
	}
    /**
	 * Returns the gold required to win.
	 *
     * @return : Gold required to win.
     */
    public String hello() {
		System.out.printf("You need %d of gold ingots\n",required_gold);
        return null;
    }
    public String gold() {
		System.out.printf("You have %d gold ingots\n", current_gold);
        return null;
    }
    public String pickup() {//picks gold if the is any
		if(original_map[current_row][current_col]=='G')
		{
			current_gold++;//increases gold owned by human player
			original_map[current_row][current_col] = '.';//if gold was picked up then leaves empty space
			//change_map[current_row][current_col] = '.';//on both maps
			System.out.println("Successful. Your number of gold is " + current_gold);
		}
		else
		{//gold not found on the spot
			System.out.println("Unsuccessful");
		}
        return null;
    }

    public String look() {
		returnGridLook();//displays 5x5 grid for player's orientation
		//returnChangeMap();
        return null;
    }

	public String quit() {
		if(current_gold == required_gold && original_map[current_row][current_col]=='E')
		{//player collected all gold and stands of E -> win
			System.out.println("WIN");
		}
		else//otherwise instant loose
		{
			System.out.println("LOOSE");
		}
		System.exit(0);//exits a game
        return null;
    }

	public void move_detailed(int row, int col, Boolean isbot) //changes how player is displayed on a map
	{//depends on the input from "move x" prompt
		char[] no_move = {'#'};//where players can not move
		boolean is_movable=true;
		if(!isbot)
		{
			for (char element : no_move) {
				if (element == change_map[current_row+row][current_col+col]) {
					is_movable=false;
				}
			}
			if(is_movable)
			{
				if(change_map[current_row+row][current_col+col]=='B')
				{
					System.out.println("LOOSE");
					System.exit(0);
				}
				change_map[current_row][current_col]=original_map[current_row][current_col];
				current_row = current_row+row;
				current_col = current_col+col;
				change_map[current_row][current_col]='P';
				System.out.println("Success");
			}
			else
			{
				System.out.println("Fail");
			}
		}
		else
		{
			for (char element : no_move) {
				if (element == change_map[current_row_bot+row][current_col_bot+col]) {
					is_movable=false;
				}
			}
			if(is_movable)
			{
				if(change_map[current_row_bot+row][current_col_bot+col]=='P')
				{
					System.out.println("LOOSE");
					System.exit(0);
				}
				change_map[current_row_bot][current_col_bot]=original_map[current_row_bot][current_col_bot];
				current_row_bot = current_row_bot+row;
				current_col_bot = current_col_bot+col;
				change_map[current_row_bot][current_col_bot]='B';
			}
		}
	}

    public void move(char direction, boolean isbot) { //moves player in 4 directions
        switch(direction){//only 4 cases present so I use switch
			case 'n':
				move_detailed(-1,0, isbot);//goes up by one
				break;
			case 's':
				move_detailed(1,0, isbot);//goes down by one
				break;
			case 'w':
				move_detailed(0,-1, isbot);//goes left by one
				break;
			case 'e':
				move_detailed(0,1, isbot);//goes right by one
				break;
		}
    }
}