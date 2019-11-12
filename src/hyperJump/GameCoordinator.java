package hyperJump;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
/*
,---.                   --.--|                       |
|  _.,---.,-.-.,---.      |  |---.,---.,---.,---.,---|
|   |,---|| | ||---'      |  |   ||    |---',---||   |
`---'`---^` ' '`---'      `  `   '`    `---'`---^`---'
*/
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
		//Start game thread
		new Thread(this).start();
		registerPlayers();
	}
	//Need to stop this long sound whenever the game changes from Round on state
	public void playMainGameSound() {
		gameMusic.startPlaying();
	}

	public void stopMainGameSound() {
		gameMusic.stopPlaying();
	}
	//For good and bad jump, just start the sound and dont worry about stopping
	//because they are only 1 second
	public void playGoodJumpSound() {
		new GameMusic().startPlaying("yay.wav");
	}
	public void playBadJumpSound() {
		new GameMusic().startPlaying("ohNo.wav");
	}
	//Take player names from the user
	public void registerPlayers() {
		System.out.println("Input the Jumping Player Name");
		Scanner in = new Scanner(System.in);
		String playerJumper =in.nextLine();
		System.out.println("Input the LED Controlling Player Name");
		String playerOpponent =in.nextLine();
		context.updatePlayerNames(playerJumper,playerOpponent);
	}
	@Override
	public void update(Object o) {
	}

	//will be invoked by the proxy.. used by this thread
	@Override
	public synchronized void call_back(msg m) {
		msgToUpdateContext = m;
	}

	public void notifyEndNodes(msg m) {
		requestProxies(m); // to notify the other devices to start/stop the game
	}

	// Request proxies to send this msg to dispatcher
	public void requestProxies(msg m) {
		for (Subject proxy : proxies) {
			((Proxy) proxy).send_msg(m);
		}
	}
	
	@Override
	public void run() {
		//just check if u need to update the context
		while (true) {
			if (msgToUpdateContext!=null)
				context.updateContext(msgToUpdateContext);
			msgToUpdateContext= null;
		}
	}

}
