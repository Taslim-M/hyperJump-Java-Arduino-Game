package hyperJump;

public class main {
	public static void main(String[] args) {
		//Create new Serial Port Handler
		SerialPortHandle sph = new SerialPortHandle("COM11");
		//Create new GameCoordinator
		GameCoordinator GC = new GameCoordinator(sph,30000); //pass the gameTime and serial port to Game Coordinator
		//Signal begin and start processing
		GC.beginGame();
		//Signal End to end-nodes
		GC.endGame();
		System.out.println("Game Ended");
	}
}
