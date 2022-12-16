public class BotPlayer implements HumanBot{//implements methods from general interface
    GameLogic respond_logic;
    private int player_row;
    private int player_col;
    private int gold_row;
    private int gold_col;
    private int exit_row;
    private int exit_col;
    private int BotGridRow;
    private int BotGridCol;
    private int current_gold_bot = 0;
    private int required_gold;
    private boolean player_found=false;
    private boolean gold_found=false;
    private boolean can_exit=false;
    private int count = 3;//every 3 commands use look
    public BotPlayer(GameLogic respond_logic)
    {
        this.respond_logic = respond_logic;
        required_gold = respond_logic.getRequiredGold();
    }
    public void botplayerStart()
    {
        respond();
    }
    public void respond()
    {
        if(count==3)
        {
            look();
            count=0;
        }
        else
        {
            if(player_found==true)//catching player is a priority
            {
                boolean horizontal = true;
                boolean vertical = true;
                if(player_col==BotGridCol)
                {
                    horizontal = false;
                }

                if(player_row==BotGridRow)
                {
                    vertical = false;
                }
                
                processMoves(horizontal, vertical);
            }
            else if(gold_found)
            {
                if(gold_col==BotGridCol && gold_row==BotGridCol)
                {
                    pickup();
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
                switch((int)(Math.random()*4))
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
    public void look() 
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
            if(current_gold_bot==required_gold)
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
            else
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
        int range_lookup = 5;
		char[][] gridArray = new char[range_lookup][range_lookup];
        int current_row_bot = respond_logic.getRowBot();
        int current_col_bot = respond_logic.getColBot();
        char[][] change_map = respond_logic.getChangeMap();
		for(int i=0, i_changeMap=-2; i<range_lookup; i++, i_changeMap++)
		{
			for(int j=0, j_changeMap=-2; j<range_lookup; j++, j_changeMap++)
			{
				boolean inBoundsRow = (i_changeMap+current_row_bot >= 0) && (i_changeMap+current_row_bot  < change_map.length);
				boolean inBoundsCol = (j_changeMap+current_col_bot  >= 0) && (j_changeMap+current_col_bot  < change_map[0].length);
				if(inBoundsCol && inBoundsRow)
				{
					gridArray[i][j] = change_map[current_row_bot+i_changeMap][current_col_bot+j_changeMap];
				}
				else
				{
					gridArray[i][j] = '#';
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
    public void moveHoriz()
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
    public void moveVert()
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
            switch((int)(Math.random()*2))
            {
                case 0:
                    moveHoriz();
                    break;
                case 1:
                    moveVert();
                    break;
            }
        }
        else if(horizontal && !vertical)
        {
            moveHoriz();
        }
        else if(vertical && !horizontal)
        {
            moveVert();
        }
    }
}
