/**
 * Runs the game with a human player and contains code needed to read inputs.
 *
 */
import java.util.Scanner;

public class HumanPlayer {
    
    private GameLogic respond_logic;
    public HumanPlayer(String mapname_file)
    {
        respond_logic = new GameLogic(mapname_file);
    }
    
    public void humanplayer_start()
    {
        respond(getCommand());
    }
    public String getCommand() {
        Scanner myObj = new Scanner(System.in);
        System.out.println("Enter command: ");
        String command = myObj.nextLine();  // Read user input
        command = command.toLowerCase();
        return command;
    }
    
    public void respond(String command)
    {
        switch(command)
        {
            case "hello": 
                respond_logic.hello();
                break;
            case "gold": 
                respond_logic.gold();
                break;
            case "pickup":
                respond_logic.pickup();
                break;
            case "look": 
                respond_logic.look();
                break;
            case "move n": 
                respond_logic.move('n');
                break;
            case "move s": 
                respond_logic.move('s');
                break;
            case "move w":
                respond_logic.move('w');
                break;
            case "move e":
                respond_logic.move('e');
                break;
            case "quit":
                respond_logic.quit();
                break;
            default:
                System.out.println("Command was not recognized");
                break;
        }
    }
}