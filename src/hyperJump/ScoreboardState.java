package hyperJump;

import java.util.Scanner;

public class ScoreboardState implements GameState {
	boolean playAnotherRound;

	public ScoreboardState() {
		playAnotherRound = false;// false by Default
	}

	@Override
	public void next(GameContext context) {
		System.out.println(context.currentJumperName + " Jumping Against " + context.currentOpponentName);
		System.out.println("Final Scores of Jumper: " + (int) context.currentMsg.getPayLoad());
		// ask the user input if the want to play another round
		System.out.println("Please press y if you wish to play an other round ");
		Scanner in = new Scanner(System.in);
		char input = in.next().charAt(0);
		if (Character.toUpperCase(input) == 'Y') {
			context.notifyEndNodes(new msg((byte)0b00001111)); // Signal end nodes to go back to waiting state
			context.resetPlayerNames();
			context.requestPlayerNames();
			context.setState(new PlayerRegistrationState());
		}
	}

	@Override
	public void prev(GameContext context) {
	}

	public void printStatus() {
		System.out.println("Scoreboard Displaying");
	}
}
