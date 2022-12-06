/**
 * Contains the main logic part of the game, as it processes.
 *
 */
public class GameLogic {
	
	/* Reference to the map being used */
	private Map map;
	private HumanPlayer player;
	private int all_gold;
	private int current_gold = 0;
	private char[][] change_map;
	private char[][] original_map;
	private int current_row;
	private int current_col;

	public Object clone()throws CloneNotSupportedException{  
		return super.clone();  
	} 

	/**
	 * Default constructor
	 */
	public GameLogic() {
		map = new Map();
		original_map = map.returnMap();
		change_map = new char[original_map.length][original_map[0].length];
		for (int i = 0; i < original_map.length; i++) {
        	System.arraycopy(original_map[i], 0, change_map[i], 0, original_map[i].length);
    	}
		all_gold = allGold();
		initializePlayer();
	}

	public void initializePlayer()
	{
		while(true)
		{
			int rand_row = (int)(Math.random()*change_map.length);
			int rand_col = (int)(Math.random()*change_map[0].length);
			if((change_map[rand_row][rand_col]=='.' || change_map[rand_row][rand_col]=='E'))
			{
				change_map[rand_row][rand_col] = 'P';
				current_row = rand_row;
				current_col = rand_col;
				break;
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
		player = new HumanPlayer();
		while(true)
		{
			player.humanplayer_start();
		}
	}

	final int allGold()
	{
		int all_gold = 0;
		for (int i = 0; i<original_map.length; i++) {
			for (int j = 0; j<original_map[0].length; j++){
				if(original_map[i][j]=='G')
				{all_gold++;}
			}
		}
		return all_gold;
	}
	public void returnChangeMap()
	{
		for (int i = 0; i<change_map.length; i++) {
			System.out.print(change_map[i]);
			System.out.println();
		}
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
		System.out.printf("You need %d of gold ingots\n",all_gold);
        return null;
    }
    public String gold() {
		System.out.printf("You have %d gold ingots\n", current_gold);
        return null;
    }
    public String pickup() {
		if(original_map[current_row][current_col]=='G')
		{
			current_gold++;
			original_map[current_row][current_col] = '.';
			change_map[current_row][current_col] = '.';
			System.out.println("Successful");
		}
		else
		{
			System.out.println("Unsuccessful");
		}
        return null;
    }

    public String look() {
		returnChangeMap();
        return null;
    }

	public String quit() {
		if(original_map[current_row][current_col]=='E' && current_gold == all_gold)
		{
			System.out.println("WIN");
		}
		else
		{
			System.out.println("LOOSE");
		}
        return null;
    }

	public void move_detailed(int row, int col)
	{
		char[] no_move = {'#','B'};
		boolean is_movable=true;
		for (char element : no_move) {
			if (element == original_map[current_row+row][current_col+col]) {
				is_movable=false;
			}
		}
		if(is_movable)
		{
			change_map[current_row][current_col]=original_map[current_row][current_col];
			current_row = current_row+row;
			current_col = current_col+col;
			change_map[current_row][current_col]='P';
		}
	}

    public String move(char direction) {
        switch(direction){
			case 'n':
				move_detailed(-1,0);
				break;
			case 'w':
				move_detailed(0,-1);
				break;
			case 'e':
				move_detailed(0,1);
				break;
			case 's':
				move_detailed(1,0);
				break;
		}
		return null;
    }
}