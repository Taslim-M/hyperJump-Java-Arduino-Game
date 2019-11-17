package hyperJump;

import java.io.IOException;
import java.util.ArrayList;

public class main {
	public static void main(String[] args) {
		// Create new Serial Port Handler
		Dispatcher d ;
		
		// Create new GameCoordinator
		Proxy accP1 = null;
		Proxy ledP1 = null;
		try {
			d = new Dispatcher(new SerialPortHandle("COM11"));
			accP1 = new Proxy((byte)0,d); // create a new proxy with hardware id 0 (accelerometer)
			ledP1 = new Proxy((byte)1,d);// create a new proxy with hardware id 1 (LED)

		} catch (IOException e) {
			e.printStackTrace();
		}
		ArrayList<Subject> proxyList = new ArrayList<Subject>();
		proxyList.add(accP1);
		proxyList.add(ledP1);
		GameCoordinator GC = new GameCoordinator(proxyList); // pass the gameTime and serial port to Game Coordinator
	}
}
