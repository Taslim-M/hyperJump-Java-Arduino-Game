package hyperJump;

import java.util.ArrayList;
import java.util.Scanner;

/*
,---.               
|  _.,---.,-.-.,---.
|   |,---|| | ||---'
`---'`---^` ' '`---'
*/
public class GameCoordinator implements Observer {
	GameContext context;
	ArrayList<Subject> availableProxies;

	Object callBackLock;
	private GameMusic gameMusic;

	public GameCoordinator(ArrayList<Subject> subjects) {
		this.context = new GameContext(this);
		gameMusic = new GameMusic();
		callBackLock = new Object();
		this.availableProxies = subjects;
		// register with all the proxy subjects
		for (Subject subject : availableProxies) {
			subject.registerObserver(this);
		}
		registerPlayers();
	}

	// Need to stop this long sound whenever the game changes from Round on state
	public void playMainGameSound() {
		gameMusic.startPlaying();
	}

	public void stopMainGameSound() {
		gameMusic.stopPlaying();
	}

	// For good and bad jump, just start the sound and dont worry about stopping
	// because they are only 1 second
	public void playGoodJumpSound() {
		new GameMusic().startPlaying("yay.wav");
	}

	public void playBadJumpSound() {
		new GameMusic().startPlaying("ohNo.wav");
	}

	// Take player names from the user
	public void registerPlayers() {
		System.out.println("Input the Jumping Player Name");
		Scanner in = new Scanner(System.in);
		String playerJumper = in.nextLine();
		System.out.println("Input the LED Controlling Player Name");
		String playerOpponent = in.nextLine();
		context.updatePlayerNames(playerJumper, playerOpponent);
	}

	// will be invoked by the proxy
	@Override
	public void call_back(msg m) {
		synchronized (callBackLock) {
			context.updateContext(m);
		}
	}

	public void notifyEndNodes(msg m) {
		requestProxies(m); // to notify the other devices to start/stop the game
	}

	// Request proxies to send this msg to dispatcher
	public void requestProxies(msg m) {
		System.out.println("Sending msg" + m.value);
		for (Subject proxy : availableProxies) {
			((Proxy) proxy).send_msg(m);
		}
	}

}
