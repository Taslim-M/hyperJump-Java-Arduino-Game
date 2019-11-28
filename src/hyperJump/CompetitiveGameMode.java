package hyperJump;

public class CompetitiveGameMode extends GameMode {
	private Integer scoreToBeat;

	public CompetitiveGameMode(GameCoordinator gC) {
		super(gC);
		scoreToBeat = 20;
	}

	@Override
	public void startSound() {
		//Playing Eye of the Tiger for Competitive mode
		gC.gameMusic.startPlaying("rocky.wav");
	}

	@Override
	public void displayInfo() {
		System.out.println(""
				+ "_________                                     __   .__   __   .__                \r\n" + 
				"\\_   ___ \\   ____    _____  ______    ____  _/  |_ |__|_/  |_ |__|___  __  ____  \r\n" + 
				"/    \\  \\/  /  _ \\  /     \\ \\____ \\ _/ __ \\ \\   __\\|  |\\   __\\|  |\\  \\/ /_/ __ \\ \r\n" + 
				"\\     \\____(  <_> )|  Y Y  \\|  |_> >\\  ___/  |  |  |  | |  |  |  | \\   / \\  ___/ \r\n" + 
				" \\______  / \\____/ |__|_|  /|   __/  \\___  > |__|  |__| |__|  |__|  \\_/   \\___  >\r\n" + 
				"        \\/               \\/ |__|         \\/                                   \\/ ");
	}

}
