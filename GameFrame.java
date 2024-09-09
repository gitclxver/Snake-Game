import java.awt.Color;

import javax.swing.JFrame;

public class GameFrame extends JFrame{
	private static final long serialVersionUID = 1L;
	
	GameFrame(){
		this.add(new GamePanel());
		this.setTitle("Snake Game");
		this.setBounds(10, 10, 905, 700);
		this.setBackground(Color.DARK_GRAY);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
