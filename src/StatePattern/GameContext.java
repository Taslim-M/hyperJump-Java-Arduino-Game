package StatePattern;

import MessageBased.msg;
import hyperJump.GameCoordinator;

public class GameContext {
	public msg currentMsg;
	public GameState currentState;
	public String currentJumperName;
	public String currentOpponentName;
	private GameCoordinator gameCoordinator;

	public GameContext(GameCoordinator gameCoordinator) {
		currentMsg = null;
		this.gameCoordinator = gameCoordinator;
		currentState = new PlayerRegistrationState();
		currentOpponentName = currentJumperName = null;
	}

	public void setState(GameState state) {
		this.currentState = state;
	}

	// Whenever a new msg has arrived, try to change state
	public void updateContext(msg m) {
		this.currentMsg = m;
		currentState.next(this);
	}

	public void updatePlayerNames(String jumper, String opponent) { // stores the player names
		this.currentJumperName = jumper;
		this.currentOpponentName = opponent;
		currentState.next(this);
	}

	// Gives states access to Coordinator Functions

	// Game Mode can be set Dynamically
	public void setCasualGameMode() {
		gameCoordinator.casualGameMode();
	}
	
	public void setCompetitiveGameMode() {
		gameCoordinator.competitiveGameMode();
	}
	public void decideWinner(int score, String jumper,String controller) {
		gameCoordinator.evaluatePlayers(score, jumper, controller);
	}
	public void stopGameOnSound() {
		gameCoordinator.stopMainGameSound();
	}

	public void playSuccessJumpSound() {
		gameCoordinator.playGoodJumpSound();
	}

	public void playNotSuccessJumpSound() {
		gameCoordinator.playBadJumpSound();
	}

	public void notifyEndNodes(msg m) {
		gameCoordinator.notifyEndNodes(m);
	}

	public void requestPlayerNames() {
		gameCoordinator.registerPlayers();
	}

	public void resetPlayerNames() {
		this.currentJumperName = this.currentOpponentName = null;
	}
}
