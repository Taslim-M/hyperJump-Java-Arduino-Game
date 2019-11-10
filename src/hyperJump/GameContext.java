package hyperJump;

public class GameContext {
	public msg currentMsg= null;
	public GameState currentState;
	public GameContext(){
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
