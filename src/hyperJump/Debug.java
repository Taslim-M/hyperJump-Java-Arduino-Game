package hyperJump;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Debug {
	
	static BufferedOutputStream f=null;
	
	static void tracefile(String s) throws FileNotFoundException {
		f = new BufferedOutputStream(new FileOutputStream(s));
	}
	public static void trace(String s) throws IOException {
		System.out.println(s);
		if(f!=null) {
			f.write(s.getBytes());
			f.write('\n');
			f.flush();
		}
	}

}