import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;

public class Main {
	public static void main(String[] args) throws FileNotFoundException {
		
		MyFrame frame = new MyFrame();
		frame.setBackground(Color.BLACK);
		frame.setForeground(Color.WHITE);
		frame.setTitle("My dictionary");
		frame.setSize(900, 500);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

	}
}
