package hyperJump;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Proxy implements Runnable, Observer, Subject {
	Dispatcher d; // Subject which sends to this observer
	Byte hardwareID; // hardware ID for each proxy
	Queue<msg> fromDispatcher; // Send to Game Thread
	Queue<msg> toDispatcher; // Send to Dispatcher
	ArrayList<Observer> observers; // For maintaining a list of observers - Game thread(s)

	Proxy(Byte id, Dispatcher d) throws IOException {
		this.d = d;
		// Read ID Mask
		this.hardwareID = id;
		// register yourself as a proxy with the dispatcher
		d.registerObserver(this);
		// Initiate the fields
		fromDispatcher = new LinkedList<msg>();
		toDispatcher = new LinkedList<msg>();
		observers = new ArrayList<Observer>();
		// start your thread
		new Thread(this).start();
	}

	void send_msg(msg m) {
		// tell the dispatcher to send your message
		d.send_msg(this, m);
	}

	public void run() {
		msg b;
		while (true) {
			// forwarding of message to the dispacher
			try {
				if (!toDispatcher.isEmpty()) {
					// read a byte from the queue
					// this will come from the Application or the Game class.

					b = toDispatcher.remove();
					Debug.trace(this + " sending " + b + " to the dispatcher");

					// just send to out the dispatcher
					send_msg(b);
				}
				if (!fromDispatcher.isEmpty()) {
					// this will come from the Dispatcher
					// just send to out the dispatcher
					notifyObservers();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void update(Object o) {
		// just use the same code in call_back function
		call_back((msg) o);
	}

	@Override
	public void call_back(msg m) {
		// call-back function -- will be called by dispatcher
		// then decide to do what with the incoming message
		fromDispatcher.add(m);
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
		// We only have one game Thread observer.. can use the standard format
		msg b;
		for (Observer observer : observers) {
			b = fromDispatcher.remove();
			observer.call_back(b);

			try {
				Debug.trace(this + " sending " + b + " to the Game Thread");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}