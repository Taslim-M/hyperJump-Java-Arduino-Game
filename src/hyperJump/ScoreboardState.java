package hyperJump;
import java.io.*;
import java.util.Scanner;
public class ScoreboardState implements GameState {
ByteDecoder bD;
boolean anotherRound; // false by Default
	
	public ScoreboardState() {
		bD= new ByteDecoder();
	}
	public void printStatus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void next(GameContext context) {
		// TODO Auto-generated method stub
		bD.updateFlags(context.currentMsg);
		if(bD.finalScoreReceived) {
			System.out.println("Final Scores: "+(int)context.currentMsg.getPayLoad());
			// ask the user input if the want to play another round
			System.out.println("Please press y if you wish to play an other round ");
			Scanner in = new Scanner(System.in);
			 char input = sc.next().charAt(0); 
			
			 if(input=='y' | input=='Y') {
				 context.setState(new PlayerRegistrationState());
				 
			 }
			 
				 
		}
		else {
			System.out.println("Waiting for Final Scores");
		}
		
		bD.resetFlags();
	}

	@Override
	public void prev(GameContext context) {
		// TODO Auto-generated method stub
		
	}

	

}
