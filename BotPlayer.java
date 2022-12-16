public class BotPlayer implements HumanBot{
    GameLogic respond_logic;
    private int player_row;
    private int player_col;
    private int BotGridRow;
    private int BotGridCol;
    private boolean player_found=false;
    private int count = 3;//every 3 commands use look
    public BotPlayer(GameLogic respond_logic)
    {
        this.respond_logic = respond_logic;
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
            if(player_found==true)
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
        char[][] gridarray = respond_logic.returnGridLookBot();
        BotGridRow = 2;
        BotGridCol = 2;
        player_found=false;
        for(int i=0;i<gridarray.length;i++)
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
}
