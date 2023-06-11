
import java.awt.*;
import java.io.IOException;
import java.net.Socket;

import javax.swing.*;


public class GameFrame extends JFrame  {

	GamePanel panel;


    GameFrame(Socket s) throws IOException {
			panel = new GamePanel(s);
			this.add(panel);
			this.setTitle("Pong Game");
			this.setResizable(false);
			this.setBackground(Color.black);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.pack();
			this.setVisible(true);
			this.setLocationRelativeTo(null);
    }
}