public class BotPlayer implements HumanBot{//implements methods from general interface
    GameLogic respond_logic;
    //----coordinates bot looks for using look()
    private int player_row;
    private int player_col;
    private int gold_row;
    private int gold_col;
    private int exit_row;
    private int exit_col;
    //---------------------
    //+bot coordinates on the grid
    private int BotGridRow;
    private int BotGridCol;
    //---------------------
    //gold
    private int current_gold_bot = 0;
    private int required_gold;
    //---------------------
    //where bot is in rel to original(change_map) map
    int current_row_bot;
    int current_col_bot;
    //---------------------
    private char[][] change_map;
    private boolean player_found=false;
    private boolean gold_found=false;
    private boolean can_exit=false;
    private int count = 3;//every 3 commands use look
    public BotPlayer(GameLogic respond_logic)
    {
        this.respond_logic = respond_logic;
        required_gold = respond_logic.getRequiredGold();
    }
    public void botplayerStart()//starts bot
    {
        respond();
    }
    public void respond()
    {
        if(count==3)//every 3rd command is look(), 1st in the start as well
        {
            look();
            count=0;//reset counter
        }
        else
        {
            if(player_found==true)//catching player is a priority
            {
                boolean horizontal = true;
                boolean vertical = true;
                if(player_col==BotGridCol)
                {
                    horizontal = false;//if bot needs to move horizontally
                }

                if(player_row==BotGridRow)
                {
                    vertical = false;//if bot needs to move vertically
                }
                
                processMoves(horizontal, vertical);//decides where it is most rational to move
            }
            else if(gold_found)
            {
                if(gold_col==BotGridCol && gold_row==BotGridRow)
                {
                    pickup();//bot picks gold if it stands on G
                }
                else
                {
                    boolean horizontal = true;
                    boolean vertical = true;
                    if(gold_col==BotGridCol)
                    {
                        horizontal = false;
                    }

                    if(gold_row==BotGridRow)
                    {
                        vertical = false;
                    }
                    
                    processMoves(horizontal, vertical);
                }
            }
            else if(can_exit)
            {
                boolean horizontal = true;
                boolean vertical = true;
                if(exit_col==BotGridCol)
                {
                    horizontal = false;
                }

                if(exit_row==BotGridRow)
                {
                    vertical = false;
                }
                
                processMoves(horizontal, vertical);
            }
            else
            {
                switch((int)(Math.random()*4))//move randomly if nothing in the area
                {
                    case 0: 
                        respond_logic.move('n', true);
                        break;
                    case 1: 
                        respond_logic.move('s', true);
                        break;
                    case 2:
                        respond_logic.move('w', true);
                        break;
                    case 3:
                        respond_logic.move('e', true);
                        break;
                }
            }
        }
        count++;
    }
    public void look() //process 5x5 grid
    {
        char[][] gridarray = returnGridLook();
        BotGridRow = 2;
        BotGridCol = 2;
        player_found=false;
        gold_found=false;
        can_exit=false;
        for(int i=0;i<gridarray.length;i++)//catching player is priority
        {
            for(int j=0;j<gridarray[0].length;j++)
            {
                if(gridarray[i][j] == 'P')
                {
                    player_row=i;
                    player_col=j;
                    player_found=true;
                }
            }
        }
        if(!player_found)
        {
            if(current_gold_bot==required_gold)//if bot has all gold, then looks for exit
            {
                for(int i=0;i<gridarray.length;i++)
                {
                    for(int j=0;j<gridarray[0].length;j++)
                    {
                        if(gridarray[i][j] == 'E')
                        {
                            exit_row=i;
                            exit_col=j;
                            can_exit=true;
                        }
                    }
                }
            }
            else//otherwise looks for G
            {
                for(int i=0;i<gridarray.length;i++)
                {
                    for(int j=0;j<gridarray[0].length;j++)
                    {
                        if(gridarray[i][j] == 'G')
                        {
                            gold_row=i;
                            gold_col=j;
                            gold_found=true;
                        }
                    }
                }
            }
        }
    }
    public char[][] returnGridLook()
    {
        int range_lookup = 5;// 5x5
		char[][] gridArray = new char[range_lookup][range_lookup];
        current_row_bot = respond_logic.getRowBot();
        current_col_bot = respond_logic.getColBot();
        change_map = respond_logic.getChangeMap();
		for(int i=0, i_changeMap=-2; i<range_lookup; i++, i_changeMap++)
		{
			for(int j=0, j_changeMap=-2; j<range_lookup; j++, j_changeMap++)
			{//check if map is in bounds
				boolean inBoundsRow = (i_changeMap+current_row_bot >= 0) && (i_changeMap+current_row_bot  < change_map.length);
				boolean inBoundsCol = (j_changeMap+current_col_bot  >= 0) && (j_changeMap+current_col_bot  < change_map[0].length);
				if(inBoundsCol && inBoundsRow)//if in bounds just copy
				{
					gridArray[i][j] = change_map[current_row_bot+i_changeMap][current_col_bot+j_changeMap];
				}
				else
				{
					gridArray[i][j] = '#';//# for empty
				}
			}
		} 
		return gridArray;
    }
    public void pickup() {//picks gold if the is any
        int current_row_bot = respond_logic.getRowBot();
        int current_col_bot = respond_logic.getColBot();
        char[][] original_map = respond_logic.getOriginalMap();
		if(original_map[current_row_bot][current_col_bot]=='G')
		{
			current_gold_bot++;//increases gold owned by human player
            respond_logic.setCurrentGoldBot(current_gold_bot);
            respond_logic.setOriginalMap(current_row_bot, current_col_bot);//if gold was picked up then leaves empty space
		}
    }

	public void quit() {
        int current_gold_bot = respond_logic.getCurrentGoldBot();
        char[][] original_map = respond_logic.getOriginalMap();
        int current_row_bot = respond_logic.getRowBot();
        int current_col_bot = respond_logic.getColBot();
		if(current_gold_bot == required_gold && original_map[current_row_bot][current_col_bot]=='E')
		{//player collected all gold and stands of E -> win
			System.out.println("LOOSE");
            System.out.println("Bot left with gold");
		}
		System.exit(0);//exits a game
    }
    public void moveHoriz()//look how bot should move horizontally in rel to human
    {
        if(player_col>BotGridCol)
        {
            respond_logic.move('e', true);
            BotGridCol++;
        }
        else if(player_col<BotGridCol)
        {
            respond_logic.move('w', true);
            BotGridCol--;
        }
    }
    public void moveVert()//look how bot should move vertically in rel to human
    {
        if(player_row>BotGridRow)
        {
            respond_logic.move('s', true);
            BotGridRow++;
        }
        else if(player_row<BotGridRow)
        {
            respond_logic.move('n', true);
            BotGridRow--;
        }
    }
    public void processMoves(boolean horizontal, boolean vertical)//decides which direction is rational to move
    {
        if(horizontal && vertical)
        {
            switch((int)(Math.random()*2))//if bot can move both vert. and horiz. in rel to player ->choose randomly how
            {
                case 0:
                    moveHoriz();
                    break;
                case 1:
                    moveVert();
                    break;
            }
        }
        else if(horizontal && !vertical)//can move just horizontally, because bot and player have the same column
        {
            moveHoriz();
        }
        else if(vertical && !horizontal)//can move just vertically, because bot and player have the same row
        {
            moveVert();
        }
    }
}
