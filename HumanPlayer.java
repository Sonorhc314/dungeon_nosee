/**
 * Runs the game with a human player and contains code needed to read inputs.
 *
 */
import java.util.Scanner;

public class HumanPlayer {
    GameLogic respond_logic;
    public HumanPlayer(GameLogic respond_logic)
    {
        this.respond_logic = respond_logic;
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
            case "quit":
                respond_logic.quit();
                break;
            default:
                System.out.println("Command was not recognized");
                break;
        }
    }
}