package hyperJump;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Dispatcher implements Runnable, Subject {

	// need to keep track of proxies
	ArrayList<Proxy> proxies;
	// HashMap improves search speed
	HashMap<Byte, Proxy> hashMap = new HashMap<Byte, Proxy>();
	Object broadcastLock;
	msg currentMsg;
	SerialPortHandle sph;

	Dispatcher(SerialPortHandle sph) throws FileNotFoundException {

		// create an array list to remember proxies
		proxies = new ArrayList<Proxy>();
		broadcastLock = new Object();
		// initiate the byte to null
		currentMsg = null;
		this.sph = sph;
		// start the thread
		new Thread(this).start();
	}

	// Broadcast message
	public void send_msg(Proxy proxy, msg m) {
		// immediate processes the message when invoked - can be shared by multiple
		// proxies.. hence we need to synchronize this for exclusive usage
		synchronized (broadcastLock) {
			// this message should be sent out to the appropriate hardware resource
			try {
				Debug.trace("Dispatcher: Message m=" + m.value + " received from " + proxy);
			} catch (IOException e) {
				e.printStackTrace();
			}
			sph.writeByte(m.value);
		}
	}

	// only for direct Broadcast message - private to Dispatcher
	private void send_msg(msg m) {
		// this message should be sent out to the appropriate hardware resource
		synchronized (broadcastLock) {
			sph.writeByte(m.value);
		}
	}

	@Override
	public void run() {
		// look for messages from the hardware
		// when message arrives figure out who is the message
		// intended for and send them the message
		// we will use a call_back to send the message back.
		while (true) {
			// read from hardware if available
			if (sph.available() > 0) {
				currentMsg = new msg(sph.readByte());
				if (currentMsg.isBroadcastMessage()) {
					send_msg(currentMsg);
					System.out.println(currentMsg.value);
				} else {
					notifyObservers();
				}
			}
		}
		
	}

	@Override
	public void registerObserver(Observer o) {
		Proxy proxy = (Proxy) o;
		// add a new proxy to your list of known proxies and hashmap
		hashMap.put(proxy.hardwareID, proxy);
		proxies.add(proxy);
		try {
			Debug.trace("Adding " + proxy + " to list of proxies known to " + this);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void removeObserver(Observer o) {
		// remove the observer from the list of proxies and the hashmap
		int i = proxies.indexOf((Proxy) o);
		if (i >= 0) {
			hashMap.remove(proxies.get(i).hardwareID);
			proxies.remove(i);
		}
	}

	// This method is invoked only when a msg is received from serial port
	// Sends to the correct proxy
	@Override
	public void notifyObservers() {
		// should read and decode the message and figure out
		// the index of the proxy the message is intended for
		if (proxies.size() > 0) {
			Proxy correctProxy = hashMap.get(currentMsg.getID());
			if (correctProxy != null) {
				correctProxy.call_back(new msg(this.currentMsg.value));
			} else {
				System.out.println("The ID " + (this.currentMsg.getID()) + " does not belong to any proxy");
			}
			currentMsg = null;
		}
	}

}