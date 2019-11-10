package hyperJump;
import java.io.*;
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
			 BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			 int input= -1;
			try {
				input = in.read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
