package hyperJump;

import java.io.IOException;
import java.util.ArrayList;

public class main {
	public static void main(String[] args) {
		Dispatcher d ;
		//Proxies will register themselves to Dispatcher
		Proxy accP1 = null;
		Proxy ledP1 = null;
		try {
			d = new Dispatcher(new SerialPortHandle("COM11"));
			accP1 = new Proxy((byte)0,d);
			ledP1 = new Proxy((byte)1,d);

		} catch (IOException e) {
			e.printStackTrace();
		}
		ArrayList<Subject> proxyList = new ArrayList<Subject>();
		proxyList.add(accP1);
		proxyList.add(ledP1);
		//Create GameCoordinator and pass all the proxies avaialable
		GameCoordinator GC = new GameCoordinator(proxyList); 
	}
}
