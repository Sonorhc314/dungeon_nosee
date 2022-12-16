public class BotPlayer implements HumanBot{
    GameLogic respond_logic;
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
    public void look() {
        // char[][] gridarray = respond_logic.returnGridLook();
    }
}
