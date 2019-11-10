package hyperJump;

import java.io.*;
import java.util.Scanner;

public class ScoreboardState implements GameState {
	boolean anotherRound; // false by Default

	public ScoreboardState() {
	}

	public void printStatus() {
		System.out.println("Scoreboard Displaying");
	}

	@Override
	public void next(GameContext context) {
		ByteDecoder.updateFlags(context.currentMsg);
		if (ByteDecoder.finalScoreReceived) {
			System.out.println("Final Scores: " + (int) context.currentMsg.getPayLoad());
			// ask the user input if the want to play another round
			System.out.println("Please press y if you wish to play an other round ");
			Scanner in = new Scanner(System.in);
			char input = in.next().charAt(0);
			if (Character.toUpperCase(input) == 'Y') {
				context.setState(new PlayerRegistrationState());
			}
		} else {
			System.out.println("Waiting for Final Scores");
		}
		ByteDecoder.resetFlags();
	}

	@Override
	public void prev(GameContext context) {
	}

}
