package hyperJump;

import java.util.Scanner;

public class PlayerRegistrationState implements GameState {

	@Override
	public void next(GameContext context) {
		//Check for successful Player Registration
		if (context.currentJumperName != null && context.currentOpponentName!=null) {
			// notify all devices to start the game by broad casting Ob:1111 1111
			System.out.println("Press 1 to select Casual Game, Press 2 to select Competitive Game");
			Scanner in = new Scanner(System.in);
			String ans= in.nextLine(); // Wait for enter from user
			if(ans =="1") {
				context.setCasualGameMode();
			}else {
				context.setCompetitiveGameMode();
			}
			context.setState(new RoundOnState());
		} else {
			System.out.println("Player names are not set. Please set them first!");
			context.requestPlayerNames();
		}
	}

	@Override
	public void prev(GameContext context) {
	}

	@Override
	public void printStatus() {
		System.out.println("Waiting for Player to Register themselves");
	}

}
