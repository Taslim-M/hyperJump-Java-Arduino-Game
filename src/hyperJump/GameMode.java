package hyperJump;

//Template Method is used here
public abstract class GameMode {
	GameCoordinator gC;

	public GameMode(GameCoordinator gC) {
		this.gC = gC;
	}

	// Templated Method on how to Initialize Game
	final public void gameStarter() {
		notifyStartEndNodes();
		displayInfo();
		startSound();
	}

	// send Start_Game msg according to Protocol table
	private void notifyStartEndNodes() {
		gC.notifyEndNodes(new msg((byte) 0b11111111));
	}

	public abstract void displayInfo();

	public abstract void startSound();

	// Templated Method on how to Handle End Score

	final public void evaluatePlayer(int score, String jumperName, String opponentName) {
		announcement();
		displayResult(score, jumperName, opponentName);
	}

	public void announcement() {
		gC.playAnnouncement();
	}

	public abstract void displayResult(int score, String jumperName, String opponentName);
}
