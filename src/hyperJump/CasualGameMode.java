package hyperJump;

public class CasualGameMode extends GameMode {

	public CasualGameMode(GameCoordinator gC) {
		super(gC);
		// Every time a gameMode is created, the game starter method is also called
		gameStarter();
	}

	@Override
	public void startSound() {
		gC.gameMusic.startPlaying("gunsRoses.wav");
	}

	@Override
	public void displayInfo() {
		System.out.println("                                                      \r\n"
				+ ",---.                    |        ,---.               \r\n"
				+ "|    ,---.,---..   .,---.|        |  _.,---.,-.-.,---.\r\n"
				+ "|    ,---|`---.|   |,---||        |   |,---|| | ||---'\r\n"
				+ "`---'`---^`---'`---'`---^`---'    `---'`---^` ' '`---'\r\n"
				+ "                                                      ");
	}

	@Override
	public void displayResult(int score, String jumperName, String opponentName) {
		System.out.println("Winner or Loser does not matter, we had fun :))");
		System.out.println(jumperName + " Jumping Against " + opponentName);
		System.out.println("Final Scores of " + jumperName + ": " + score);
	}

}
