package hyperJump;

public class CasualGameMode extends GameMode{

	public CasualGameMode(GameCoordinator gC) {
		super(gC);
	}

	@Override
	public void startSound() {
		gC.gameMusic.startPlaying("gunsRoses.wav");
	}

	@Override
	public void displayInfo() {
		System.out.println("                                                      \r\n" + 
				",---.                    |        ,---.               \r\n" + 
				"|    ,---.,---..   .,---.|        |  _.,---.,-.-.,---.\r\n" + 
				"|    ,---|`---.|   |,---||        |   |,---|| | ||---'\r\n" + 
				"`---'`---^`---'`---'`---^`---'    `---'`---^` ' '`---'\r\n" + 
				"                                                      ");
	}

}
