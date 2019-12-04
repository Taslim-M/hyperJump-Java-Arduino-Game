package StatePattern;

import java.util.Scanner;

import MessageBased.msg;

public class ScoreboardState implements GameState {
	boolean playAnotherRound;

	public ScoreboardState() {
		playAnotherRound = false;// false by Default
	}

	@Override
	public void next(GameContext context) {
		
		//Evaluate the players depending on difficulty mode selected
		//Depending on user choice, different methods will be called here
		context.decideWinner((int)context.currentMsg.getPayLoad(), context.currentJumperName, context.currentOpponentName);
		// ask the user input if the want to play another round
		System.out.println("Please enter to play an other round ");
		Scanner in = new Scanner(System.in);
		in.nextLine(); // Wait for enter from user
		context.notifyEndNodes(new msg((byte) 0b00001111)); // Signal end nodes to go back to waiting state
		context.resetPlayerNames();
		context.setState(new PlayerRegistrationState());
		context.requestPlayerNames();
	}

	@Override
	public void prev(GameContext context) {
	}

	public void printStatus() {
		System.out.println("Scoreboard Displaying");
	}
}
