package hyperJump;

import java.util.ArrayList;
import java.util.Scanner;

import GameModes.*;
import MessageBased.*;
import Music.GameMusic;
import StatePattern.GameContext;

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
	public GameMusic gameMusic;
	GameMode gameMode;
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
	//Game Modes can be updated Dynamically
	public void casualGameMode() {
		setGameMode(new CasualGameMode(this));
	}
	public void competitiveGameMode() {
		setGameMode(new CompetitiveGameMode(this));
	}
	private void setGameMode(GameMode gameMode) {
		this.gameMode=gameMode;
	}
	public void evaluatePlayers(int score, String jumper,String controller) {
		gameMode.evaluatePlayer(score, jumper, controller);
	}
	// Need to stop this long sound whenever the game changes from Round on state
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
	public void playAnnouncement() {
		new GameMusic().startPlaying("dundundun.wav");
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
		// synchronized so that only one proxy can use the call_back function of the game co-ordinator at one time
		synchronized (callBackLock) {  
			context.updateContext(m);
		}
	}

	public void notifyEndNodes(msg m) {
		requestProxies(m); // to notify the other devices to start/stop the game
	}

	// Request proxies to send this msg to dispatcher - Since it's a broadcast- sending only one is enough
	public void requestProxies(msg m) {
		((Proxy)availableProxies.get(0)).send_msg(m);
	}

}
