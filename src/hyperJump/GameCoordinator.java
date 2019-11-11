package hyperJump;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class GameCoordinator implements Observer, Runnable {
	GameContext context;
	ArrayList<Subject> proxies;
	Queue<msg> fromProxies; // to store any updates from proxies
	private GameMusic gameMusic;

	public GameCoordinator(ArrayList<Subject> subjects) {
		this.context = new GameContext(this);
		proxies = new ArrayList<Subject>();
		fromProxies = new LinkedList<msg>();
		// register with all the proxy subjects
		for (Subject subject : subjects) {
			subject.registerObserver(this);
		}
	}
	
	public void playMainGameSound() {
		gameMusic.startPlaying();
	}
	public void stopMainGameSound() {
		gameMusic.stopPlaying();
	}
	//Broadcast 0000 0000 to the end-nodes, signaling them to stop
	public void notifyEndGame() {
		requestProxies(new msg((byte) 0b00000000));
	}

	@Override
	public void update(Object o) {
		
	}

	@Override
	public synchronized void call_back(msg m) {
		fromProxies.add(m); // store the message from proxy into a queue
	}

	public void notifySuccessfulRegistration() {
		requestProxies(new msg((byte) 0b11111111)); // to notify the other devices to start the game
	}

	// Request proxies to send this msg to dispatcher
	public void requestProxies(msg m) {
		for (Subject proxy : proxies) {
			((Proxy) proxy).send_msg(m);
		}
	}

	@Override
	public void run() {
		while (true) {
			if (!fromProxies.isEmpty()) { // if fromProxies is not empty
				// remove a byte from the queue and update the context
				context.updateContext(fromProxies.remove());
			}
		}

	}
}
