package hyperJump;

public class GameContext {
	public msg currentMsg;
	public GameState currentState;
	private GameCoordinator gameCoordinator;
	public GameContext(GameCoordinator gameCoordinator){
		currentMsg = null;
		this.gameCoordinator = gameCoordinator;
		currentState = new PlayerRegistrationState();
	}
	
	public void setState(GameState state) {
		this.currentState = state;
	}
	public void updateContext(msg m) {
		this.currentMsg = m;
		
		currentState.next(this);
	}
}
