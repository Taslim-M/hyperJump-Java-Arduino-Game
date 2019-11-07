package hyperJump;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Dispatcher implements Runnable, Subject {

	// need to keep track of proxies

	ArrayList<Proxy> proxies;
	Random r = new Random();
	HashMap<Byte, Proxy> hashMap = new HashMap<Byte, Proxy>();

	FileInputStream fin = null;
	DataInputStream byteReader = null;
	Byte currentMsg;
	SerialPortHandle sph;
	Dispatcher(SerialPortHandle sph) throws FileNotFoundException {

		// create an array list to remember proxies
		proxies = new ArrayList<Proxy>();

		// To read one byte from file and process
		fin = new FileInputStream("bytes");
		byteReader = new DataInputStream(new BufferedInputStream(fin));
		this.sph = sph;
		// start the thread
		new Thread(this).start();
	}

	public void send_msg(Proxy proxy, msg m) throws IOException {

		// this message should be sent out to the appropriate hardware
		// resource
		Debug.trace("Dispatcher: Message m=" + m.value + " received from " + proxy);

		// decode the message and send to the right person.
		// should look like a switch statement
		/*
		 * switch(msg.get_id()) { case 0: // send msg.get_payload() to hardware edge
		 * node 0 break; case 1: // send send msg.get_payload() to hardware edge node 1
		 * break; default: // code block }
		 */
	}

	public msg get_msg(Proxy proxy) {
		return null;
		// receive a message
		// do you really need this?

	}

	public byte getID(byte b) {
		return (byte) (b >> 6); // assuming first 2 bits are ID
	}

	public byte getPayLoad(byte b) {
		return (byte) (b & 0b00111111); // assuming first 2 bits are ID
	}

	@Override
	public void run() {
		// look for messages from the hardware
		// when message arrives figure out who is the message
		// intended for and send them the message

		// we will use a call_back to send the message back.
		// hard-coded to the first element of the proxies list
		// change this to decode the message to figure out
		// who should get the message.

		while (true) {

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if (byteReader.available() > 0) {
					this.currentMsg = byteReader.readByte();
				} else {
					break; // IF all bytes from file are read
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			// should read and decode the message and figure out
			// the index of the proxy the message is intended for
			// the index has been hard-coded to 0 here.

			if (proxies.size() > 0) {
				Proxy correctProxy = hashMap.get(getID(this.currentMsg));
				if (correctProxy != null) {
					correctProxy.call_back(new msg(getPayLoad(this.currentMsg)));
				} else {
					System.out.println("The ID " + getID(this.currentMsg) + " does not belong to any proxy");
				}
			}
		}

	}

	@Override
	public void registerObserver(Observer o) {
		Proxy proxy = (Proxy)o;
		// add a new proxy to your list of known proxies
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
		int i = proxies.indexOf((Proxy) o);
		if (i >= 0) {
			hashMap.remove(proxies.get(i).hardwareID);
			proxies.remove(i);
		
		}
	}
	@Override
	public void notifyObservers() {
		
	}

}