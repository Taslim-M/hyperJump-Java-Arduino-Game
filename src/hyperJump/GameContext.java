package hyperJump;

public class GameContext {
	public msg currentMsg;
	public GameState currentState;
	public String currentPlayerName;
	private GameCoordinator gameCoordinator;
	public GameContext(GameCoordinator gameCoordinator){
		currentMsg = null;
		this.gameCoordinator = gameCoordinator;
		currentState = new PlayerRegistrationState();
		currentPlayerName = null;
	}
	
	public void setState(GameState state) {
		this.currentState = state;
	}
	public void updateContext(msg m) {
		this.currentMsg = m;
		currentState.next(this);
	}
	
	//Gives states access to Coordinator Functions
	public void playGameOnSound() {
		gameCoordinator.playMainGameSound();
	}
	public void stopGameOnSound() {
		gameCoordinator.stopMainGameSound();
	}
	public void notifyRegistrationSuccess() {
		gameCoordinator.notifySuccessfulRegistration();
	}
	public void notifyEndGame() {
		gameCoordinator.notifyEndGame();
	}
}
