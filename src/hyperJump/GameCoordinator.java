package hyperJump;

public class GameCoordinator {
	SerialPortHandle sph; // to read/write bytes
	private int gameRunTime; // control total time of game in ms
	
	public GameCoordinator(SerialPortHandle sph, int gameRunTime) {
		this.sph = sph;
		this.gameRunTime = gameRunTime;
	}
	
	//This function starts the game for the specified time
	public void beginGame() {
		// broadcast to start game -- end nodes await for 11111111
		for (int i = 0; i < 5; ++i) { //repeat 5 times to ensure end-nodes have read
			sph.writeByte((byte) 0b11111111); //transmit thru serial port
		}
		State player1State = new State(0); //create state for player 1
		long startTime = System.currentTimeMillis(); // store the starting time of the game

		while (System.currentTimeMillis() <= startTime + gameRunTime) { // game continues for one minute
			byte b = sph.readByte(); //read a byte from serial port
			ByteDecoder.DecodeMessage(b, player1State);// analyze the byte and update player1State
			
			//if the critical region has just been entered (less than 0.5 ms since it was read), print to console
			if (System.currentTimeMillis() - player1State.timeLedCritical < 0.5) {
				System.out.println("LED: Critical Region Entered");

			}
			if (System.currentTimeMillis() - player1State.timeJumpDetected < 0.5) { //if a jump is detected now
				//if the jump is close to the critical region entry with a difference less than 1 second
				if (Math.abs(player1State.timeJumpDetected - player1State.timeLedCritical) < 1000) {
					System.out.println("Successful Jump"); // display success

				} else {
					System.out.println("Ooops!! missed the Jump");
				}
			}
		}
	}
//Broadcast 0000 0000 to the end-nodes, signaling them to stop
	public void endGame() {
		for (int i = 0; i < 5; ++i) { //repeat it 5 times to ensure all nodes have read
			sph.writeByte((byte) 0); // to end the game;
		}
	}
}
