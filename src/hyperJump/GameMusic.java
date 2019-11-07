package hyperJump;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class GameMusic implements Playable {
	private Clip clip;

	public void startPlaying(String filePath) {
		File musicPath = new File(filePath);
		try {
			AudioInputStream audioInp = AudioSystem.getAudioInputStream(musicPath);
			clip = AudioSystem.getClip();
			clip.open(audioInp);
			clip.start();
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			System.out.println("Error- Invalid File type or missing file");
			e.printStackTrace();
		}

	}

	public void stopPlaying() {
		clip.stop();
	}
}
