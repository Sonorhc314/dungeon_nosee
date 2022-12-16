/**
 * Contains the main logic part of the game, as it processes.
 *
 */
public class GameLogic {
	
	/* Reference to the map being used */
	/*the user prompt analysing class */
	private HumanPlayer player;
	private BotPlayer bot;
	//---------------------
	//gold
	private final int required_gold; //all gold on map
	private int current_gold = 0; //increases as player picks up gold
	private int current_gold_bot = 0; //increases as bot picks up gold
	//---------------------
	//maps
	private Map map;
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
		mapname_title = map.getMapName(); //get name of the map
		required_gold = map.getGold(); //get required gold

		original_map = map.returnMap(); //map with no moving
		change_map = new char[original_map.length][original_map[0].length]; //map where bot and player move
		for (int i = 0; i < original_map.length; i++) { //copies original_arrays
        	System.arraycopy(original_map[i], 0, change_map[i], 0, original_map[i].length);
    	}
		//all_gold = allGold();
		initializePlayerBot('P', false); //initialize player
		initializePlayerBot('B', true); //initialize bot
	}

	public void initializePlayerBot(char player_bot, Boolean isbot)//initializes a humanplayer/bot
	{
		while(true)//find the right random location for players
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

	//getters-------
	public int getRowBot()
	{
		return current_row_bot;
	}
	public int getColBot()
	{
		return current_col_bot;
	}
	public int getRow()
	{
		return current_row;
	}
	public int getCol()
	{
		return current_col;
	}
	public int getCurrentGold()
	{
		return current_gold;
	}
	public int getCurrentGoldBot()
	{
		return current_gold_bot;
	}
	public char[][] getOriginalMap()
	{
		return original_map;
	}
	int getRequiredGold()
	{
		return required_gold;
	}
	char[][] getChangeMap()
	{
		return change_map;
	}
	//--------------

	//---setters---

	public void setCurrentGold(int setgold)
	{
		current_gold = setgold;
	}
	public void setCurrentGoldBot(int setgoldbot)
	{
		current_gold_bot = setgoldbot;
	}
	public void setOriginalMap(int row, int column)
	{
		original_map[row][column] = '.';
	}
	//--------------

	public void runGame()
	{
		player = new HumanPlayer(this); //constructor inside HumanPlayer that creates GameLogic object
		bot = new BotPlayer(this); 
		System.out.println("name is " + mapname_title);
		while(true)
		{
			player.humanplayer_start(); //will prompt with scanner and process commmand
			bot.botplayerStart(); //bot responds after player makes a decision
		}
	}
	//---------------------------------
	//human player responses

    /**
	 * Returns the gold required to win.
	 *
     * @return : Gold required to win.
     */
    public void hello() {//returns required gold to win
		System.out.printf("You need %d of gold ingots\n",required_gold);
    }
    public void gold() {//returns current gold of a player
		System.out.printf("You have %d gold ingots\n", current_gold);
    }

	public void move_detailed(int row, int col, Boolean isbot) //changes how player is displayed on a map
	{//depends on the input from "move x" prompt
		char[] no_move = {'#'};//where players can not move(could add some more symbols)
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
					System.out.println("LOOSE");//instant loose if player stables into bot
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
				{//bot catches player
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

	//---------------------------------
}