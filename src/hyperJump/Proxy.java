package hyperJump;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Proxy implements Runnable, Observer, Subject {
	Dispatcher d;
	Byte hardwareID;
	Queue<msg> fromDispatcher; // Send to Game Thread
	Queue<msg> toDispatcher; // Send to Dispatcher
	ArrayList<Observer> observers;

	Proxy(Byte id, Dispatcher d) throws IOException {

		this.d = d;
		// Read ID Mask
		this.hardwareID = id;
		// register yourself as a proxy with the dispatcher
		d.registerObserver(this);
		fromDispatcher = new LinkedList<msg>();
		toDispatcher = new LinkedList<msg>();
		observers = new ArrayList<Observer>();
		// start your thread
		new Thread(this).start();
	}

	void send_msg(msg m) throws IOException {
		// tell the dispatcher to send your message
		d.send_msg(this, m);
	}

	msg get_msg() {

		// does this really make sense?
		// do you need this message?

		return d.get_msg(this);
	}

	public void run() {
		msg b;
		while (true) {

			// logic of proxy here
			// this on is implementing simple
			// forwarding of message to the dispacher

			try {

				if (!toDispatcher.isEmpty()) {

					// read a byte from the queue
					// this will come from the Application or the Game class.

					b = toDispatcher.remove();

					Debug.trace(this + " sending " + b + " to the dispatcher");

					// just send to out the dispatcher
					d.send_msg(this, b);
				}
				if (!fromDispatcher.isEmpty()) {

					// this will come from the Dispatcher

					b = fromDispatcher.remove();

					Debug.trace(this + " sending " + b + " to the Game Thread");

					// just send to out the dispatcher
					observers.get(0).call_back(b);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	@Override
	public void update(Object o) {
		// call-back function -- will be called by dispatcher
		// then decide to do what with the incoming message
		fromDispatcher.add((msg) o);
		try {
			Debug.trace("Message " + ((msg)o).value + " received by " + this + " from dispacher");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void call_back(msg m) {
		// call-back function -- will be called by dispatchr
		// then decide to do what with the incoming meessage
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
		for (Observer observer : observers) {
//			observer.call_back();
		}

	}

}