/**
 * Runs the game with a human player and contains code needed to read inputs.
 *
 */
import java.util.Scanner;


public class HumanPlayer implements HumanBot{//implements methods from general interface
    private char[][] original_map;
    GameLogic respond_logic;
    char[][] change_map;
    int current_row;
    int current_col;
    int current_gold;
    public HumanPlayer(GameLogic respond_logic)
    {
        this.respond_logic = respond_logic;//object to call for it's methods
        current_gold = respond_logic.getCurrentGold();
    }
    
    public void humanplayer_start()
    {
        respond(getCommand());
    }
    public String getCommand() {
        Scanner myObj = new Scanner(System.in);//continuous commands from prompt
        System.out.println("Enter command: ");
        String command = myObj.nextLine();  // Read user input
        command = command.toLowerCase();//standartize input
        return command;
    }
    
    public void respond(String command)
    {
        switch(command)
        {
            case "hello": //gold required
                respond_logic.hello();
                break;
            case "gold": //gold owned
                respond_logic.gold();
                break;
            case "pickup"://pick gold
                pickup();
                break;
            case "look": //5x5 grid
                look();
                break;
            case "move n": //description in GameLogic.java
                respond_logic.move('n', false);
                break;
            case "move s": 
                respond_logic.move('s', false);
                break;
            case "move w":
                respond_logic.move('w', false);
                break;
            case "move e":
                respond_logic.move('e', false);
                break;
            case "quit"://exits
                quit();
                break;
            default:
                System.out.println("Command was not recognized");
                break;
        }
    }
    public void look()//processes 5x5 grid
    {
        char[][] gridlook = returnGridLook();
        for(int i=0;i<gridlook.length;i++)
        {
            for(int j=0;j<gridlook[0].length;j++)
            {
                System.out.print(gridlook[i][j]);
            }
            System.out.println();
        }
    }
    public void pickup() {//picks gold if the is any
        current_row = respond_logic.getRow();
        current_col = respond_logic.getCol();
        original_map = respond_logic.getOriginalMap();
		if(original_map[current_row][current_col]=='G')
		{
			current_gold++;//increases gold owned by human player
            respond_logic.setCurrentGold(current_gold);
            respond_logic.setOriginalMap(current_row, current_col);//if gold was picked up then leaves empty space
            original_map = respond_logic.getOriginalMap();
			System.out.println("Successful. Your number of gold is " + current_gold);
		}
		else
		{//gold not found on the spot
			System.out.println("Unsuccessful");
		}
    }

	public void quit() {
        int required_gold = respond_logic.getRequiredGold();
        current_row = respond_logic.getRow();
        current_col = respond_logic.getCol();
		if(current_gold == required_gold && original_map[current_row][current_col]=='E')
		{//player collected all gold and stands of E -> win
			System.out.println("WIN");
		}
		else//otherwise instant loose
		{
			System.out.println("LOOSE");
		}
		System.exit(0);//exits a game
    }
    
    public char[][] returnGridLook()
    {
		int range_lookup = 5;
		char[][] gridArray = new char[range_lookup][range_lookup];
        current_row = respond_logic.getRow();
        current_col = respond_logic.getCol();
        change_map = respond_logic.getChangeMap();
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
					gridArray[i][j] = '#';//# for empty spaces
				}
			}
		} 
		return gridArray;
    }
}