package hyperJump;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class GameCoordinator implements Observer, Runnable {
	GameContext context;
	ArrayList<Subject> proxies;
	msg msgToUpdateContext;
	

	private GameMusic gameMusic;

	public GameCoordinator(ArrayList<Subject> subjects) {
		this.context = new GameContext(this);
		proxies = new ArrayList<Subject>();
		msgToUpdateContext=null;
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

	@Override
	public void update(Object o) {

	}

	@Override
	public synchronized void call_back(msg m) {
		
		msgToUpdateContext = m;
	}

	public void notifyEndNodes(msg m) {
		requestProxies(m); // to notify the other devices to start the game
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
			if (msgToUpdateContext!=null)
				context.updateContext(msgToUpdateContext);
			msgToUpdateContext= null;
		}
	}

}
