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
		System.out.println("To beat your opponent - Score more than "+scoreToBeat + " points");
	}

	@Override
	public void displayResult(int score, String jumperName, String opponentName) {
		System.out.println("Good Game ! But there can only be one winner");
		if(score>this.scoreToBeat) {
			System.out.println(jumperName+ " HAS WON THE GAME!!!!!!!!!!");
		}else {
			System.out.println(opponentName+ " HAS WON THE GAME!!!!!!!!!!");
		}
	}
	

}
