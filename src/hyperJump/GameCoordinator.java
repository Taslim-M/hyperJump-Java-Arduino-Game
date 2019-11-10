package hyperJump;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class GameCoordinator implements Observer, Runnable {
	GameContext context;
	ArrayList<Subject> proxies;
	Queue<msg> fromProxies; // to store any updates from proxies

	public GameCoordinator(ArrayList<Subject> subjects) {
		this.context = new GameContext();
		proxies = new ArrayList<Subject>();
		fromProxies = new LinkedList<msg>();
		// register with all the proxy subjects
		for (Subject subject : subjects) {
			subject.registerObserver(this);
		}

	}

	// This function starts the game for the specified time
	public void beginGame() {
		// broadcast to start game -- end nodes await for 11111111
		for (int i = 0; i < 5; ++i) { // repeat 5 times to ensure end-nodes have read
			sph.writeByte((byte) 0b11111111); // transmit thru serial port
		}
		UnusuedState player1State = new UnusuedState(0); // create state for player 1
		long startTime = System.currentTimeMillis(); // store the starting time of the game
		byte b = sph.readByte(); // read a byte from serial port
		ByteDecoder.DecodeMessage(b, player1State);// analyze the byte and update player1State

		// if the critical region has just been entered (less than 0.5 ms since it was
		// read), print to console
		if (player1State.hasJumped && player1State.shouldJump) { // if a jump is detected correctly
			System.out.println("Great Jump Score added: " + player1State.scoreMultiplier);
		} else if (player1State.hasJumped && !player1State.shouldJump) {
			System.out.println("Wrong Jump");
		}
	}

	}

//Broadcast 0000 0000 to the end-nodes, signaling them to stop
	public void endGame() {
		for (int i = 0; i < 5; ++i) { // repeat it 5 times to ensure all nodes have read
			sph.writeByte((byte) 0); // to end the game;
		}
	}

	@Override
	public void update(Object o) {
		call_back((msg) o);
	}

	@Override
	public void call_back(msg m) {
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
		// TODO Auto-generated method stub

		while (true) {
			// forwarding of message to the dispacher
			try {
				if (!fromProxies.isEmpty()) { // if fromProxies is not empty
					
					// remove a byte from the queue and update the context
					context.updateContext(fromProxies.remove()); 

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
