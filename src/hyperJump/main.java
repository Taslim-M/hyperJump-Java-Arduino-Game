package hyperJump;

public class main {
	public static void main(String[] args) {
		// Create new Serial Port Handler
		SerialPortHandle sph = new SerialPortHandle("COM11");
		// Create new GameCoordinator
		GameCoordinator GC = new GameCoordinator(); // pass the gameTime and serial port to Game Coordinator
		System.out.println("Game Ended");
	}
}
