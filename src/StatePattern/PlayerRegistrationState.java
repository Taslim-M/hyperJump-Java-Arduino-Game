package StatePattern;

import java.util.Scanner;

public class PlayerRegistrationState implements GameState {

	@Override
	public void next(GameContext context) {
		//Check for successful Player Registration
		if (context.currentJumperName != null && context.currentOpponentName!=null) {
		
			System.out.println("Press 1 to select Casual Game, Press 2 to select Competitive Game");
			Scanner in = new Scanner(System.in);
			String ans= in.nextLine(); // Wait for enter from user
			if(Integer.parseInt(ans) ==1) {
				context.setCasualGameMode();
				context.setState(new RoundOnState());
			}else if (Integer.parseInt(ans) ==2) {
				context.setCompetitiveGameMode();
				context.setState(new RoundOnState());
				
			}
			else {
				System.out.println("Invalid Input!\n Please select an appropriate game mode.");
			}
			
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
