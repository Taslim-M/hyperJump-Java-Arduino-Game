package hyperJump;

public class PlayerRegistrationState implements GameState{

	@Override
	public void next(GameContext context) {
		if(context.currentPlayerName!=null) {
			//notify all devices to start
			context.notifyRegistrationSuccess();
			context.setState(new RoundOnState());
		}
	}

	@Override
	public void prev(GameContext context) {
	}

	@Override
	public void printStatus() {
		System.out.println("Waiting for Player to Register themselves");
	}

}
