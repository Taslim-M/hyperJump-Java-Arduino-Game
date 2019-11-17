package hyperJump;

import java.io.IOException;
import java.util.ArrayList;

public class Proxy implements Observer, Subject {
	Dispatcher d; // Subject which sends to this observer
	Byte hardwareID; // hardware ID for each proxy
	msg msgToForward;
	ArrayList<Observer> observers; // For maintaining a list of observers - Game thread(s)

	Proxy(Byte id, Dispatcher d) throws IOException {
		this.d = d;
		// Read ID Mask
		this.hardwareID = id;
		// register yourself as a proxy with the dispatcher
		d.registerObserver(this);
		// Initiate the fields
	
		observers = new ArrayList<Observer>();
	}

	void send_msg(msg m) {
		// tell the dispatcher to send your message
		d.send_msg(this, m);
	}

	@Override
	public void call_back(msg m) {
			msgToForward = m;
			// call the game thread
			notifyObservers(); // directly call_back the game thread
			try {
				Debug.trace("Message " + m.value + " received by " + this + " from dispacher");
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	@Override
	public void registerObserver(Observer o) {
		observers.add(o);
	}

	@Override
	public void removeObserver(Observer o) {
		int i = observers.indexOf((Proxy) o);
		if (i >= 0) {
			observers.remove(i);
		}
	}

	@Override
	public void notifyObservers() {
		// Using the standard syntax for notify Observers
		// If multiple observers are added in the future (maybe for logs).. no need to
		// change code
		// Currently, only Game thread receives this msg
		for (Observer observer : observers) {
			observer.call_back(msgToForward);
			try {
				Debug.trace(this + " sending " + msgToForward.value + " to the Game Thread");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}