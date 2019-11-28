package hyperJump;
//Template Method is used here
public abstract class GameMode {
	GameCoordinator gC;
	public GameMode(GameCoordinator gC) {
		this.gC=gC;
	}
	//Templated Method on how to Initialize Game
	final public void gameStarter() {
		notifyEndNodes();
		displayInfo();
		startSound();
	}
	private void notifyEndNodes() {
		gC.notifyEndNodes(new msg((byte)0b11111111));
	}
	public abstract void displayInfo();
	public abstract void startSound();
	
	//Templated Method on how to Handle End Score
	
}
