import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.io.*;
import java.util.Scanner;

public class GamePanel extends JPanel implements KeyListener, ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	static final int SCREEN_WIDTH = 905;
	static final int SCREEN_HEIGHT = 700;
	static final int GAME_WIDTH = 850;
	static final int GAME_HEIGHT = 650;
	static final int UNIT_SIZE = 25;
	
	int GAME_UNITS = ((GAME_WIDTH)*(GAME_HEIGHT))/UNIT_SIZE;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int appleX; // stores value of apple at x
	int appleY; // stores value of apple at y
	int highScore = 0; // stores player highscore
	
        
        // direction of the snake in game
	boolean right = false;
	boolean left = false;
	boolean up = false;
	boolean down = false;
	
	boolean running = false;
	
	Timer timer;
	Random random;
	int delay = 100;
	int snakeLength = 3;
	int moves = 0;
	int apples = 0;
	
	char direction = 'R';
	
	
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension((SCREEN_WIDTH),(SCREEN_HEIGHT)));
		this.setBackground(Color.DARK_GRAY);
		this.setFocusable(true);
		startGame();
	}
	
	public final void startGame() {
		
		newApple();
		running = true;
		addKeyListener(this);
		setFocusTraversalKeysEnabled(true);
		timer = new Timer(delay, this);
		timer.start();
		
	}
	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		draw(g);
		
	}
	
	public void draw(Graphics g) {
	//start spawn position of the snake
            if(running) {
                if(moves == 0) {
                    x[0] = 100;
                    x[1] = 75;
                    x[2] = 50;
				
                    y[0] = 100;
                    y[1] = 100;
                    y[2] = 100;
				
            }
 
            // game play border
                        
            g.setColor(Color.white);
            g.drawRect(24, 24, 851, 627);
            g.setColor(Color.blue);
            g.fillRect(25, 25, 850, 625);
			
            // displays score during play
            
            g.setColor(Color.black);
            g.setFont(new Font("Blockhead", Font.BOLD, 14));
            g.drawString("Score: " + (apples * 15) , 780, 50);
			
            // displays high score during play
			
            g.setColor(Color.black);
            g.setFont(new Font("Blockhead",Font.BOLD, 14));
            g.drawString("High Score: " + highScore, 745, 70);
					
            // color of the snake head and body
			
            g.setColor(Color.orange);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			
            for(int i = 0; i < snakeLength; i++) {
                if(i == 0) {
                    g.setColor(Color.black);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }else{
                    g.setColor(new Color(45,180,0));
                    g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            checkApple();
            checkCollision(g);
            g.dispose();
            }
            gameOver(g);
	}

	public void newApple() {
            // sets bounds for the apple to spawnwithin the spawn radius GAME_WIDTH && GAME_HEIGHT
            
            int XPos = (GAME_WIDTH - 2 * UNIT_SIZE) / UNIT_SIZE + 1;
            int YPos = (GAME_HEIGHT - 2 * UNIT_SIZE) / UNIT_SIZE + 1;
            
            // spawns the apple at a random position within the spawn radius
            
            do {
                appleX = random.nextInt(XPos) * UNIT_SIZE + UNIT_SIZE; // Adjusted range and offset
                appleY = random.nextInt(YPos) * UNIT_SIZE + UNIT_SIZE; // Adjusted range and offset
            } while (isAppleOnSnake());
        }
        
	public void checkApple() {
                
                // checks if the apple collides with the snake head x[0] and y[0]
		if((x[0] == appleX)&&(y[0] == appleY)) {
			snakeLength++;// increases the length of the snake 
			moves++;
			apples++;// increments the score
			newApple();// generates a new apples if there's a collision
		}
	}
        
	public boolean isAppleOnSnake() {
            
		//checks if snake head x & y pos are the same as the apple's 
		for(int i = 0; i < snakeLength; i++) {
			if(appleX == x[i] && appleY == y[i]) {
				return true;
				
			}
		}
		return false;
	}
        
	public void move() {
            
		// moves the snake around in such a way that the body follows the head
                
		for(int i = snakeLength; i>0; i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		switch (direction) {
                // moves snake up
                case 'U' : {
                    y[0] = y[0] - UNIT_SIZE;
                    break;
                }
                // moves snake down
                case 'D' : {
                    y[0] = y[0] + UNIT_SIZE;
                    break;
                }
                // moves snake to the left
                case 'L' : {
                    x[0] = x[0] - UNIT_SIZE;
                    break;
                }
                // moves snake to the right
                case 'R' : {
                    x[0] = x[0] + UNIT_SIZE;
                    break;
                }
                default : {
                // Handles an unknown direction if needed
                }
            }
	}

	public void highScore(Graphics g){
		
            
                // displays and updates highScore whenever a player dies
                
		for(int b = 1; b < snakeLength; b++) {
                    // creates a .txt file named Highscore which is read from everytime a new highscore is reached
			if(x[b] == x[0] && y[b] == y[0]) {
                            if(highScore < apples * 15) {
                                highScore = apples * 15;
                                try(FileWriter myFileWriter = new FileWriter("Highscore.txt")){
                                String hiscore = Integer.toString(highScore);
                                myFileWriter.write(hiscore);
                                myFileWriter.write("\n");
                                } 
                                catch(IOException e) {}            
                                }else {
                                    highScore = highScore + 0;
                                }     
                        }
                        
                                // reads the high score stored in the .txt file    
                                try (Scanner scanner = new Scanner(new File("Highscore.txt"))) {
                                    highScore = scanner.nextInt();
                                } catch (FileNotFoundException e) {
                                    System.out.println("An error occurred.");
                                }

				//displays High Score when player dies
				
                                g.setColor(Color.red);
                                g.setFont(new Font("Blockhead",Font.BOLD, 60));
                                g.drawString("High Score: " + highScore, 240, 460);
                                
				// displays Score when player dies
				
				g.setFont(new Font("Blockhead",Font.BOLD, 50));
				g.drawString("Score: " + apples * 15, 320 , 380);
				
				
			}
		
	}

	public void checkCollision(Graphics g) {
		
                // checks if the snake collides with itself
                
		for(int b = 1; b < snakeLength; b++) {
			if(x[b] == x[0] && y[b] == y[0]) {
				right = false;
				left = false;
				up = false;
				down = false;
				
				running = false;
			}
                        // calls gameOver method if the snake has collided with itself
                        
			if(!running) {
				timer.stop();
				gameOver(g);
			}
		}
	}
	
        public void pauseGame(){
            
        }
	public void gameOver(Graphics g) {
                        
                        // border
			g.setColor(Color.white);
			g.drawRect(24, 24, 851, 627);
			g.setColor(Color.blue);
			g.fillRect(25, 25, 850, 625);
				
				for(int i = 0; i < snakeLength; i++) {
					if(i == 0) {
					g.setColor(Color.black);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}else{
					g.setColor(new Color(45,180,0));
					g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
					}
				}
				
				// Game Over Text
				g.setColor(Color.red);
				g.setFont(new Font("Lorem Ipsum",Font.BOLD, 120));
				g.drawString("GAME OVER", 100, 290);
				
				
				// Restart Game Text
				g.setFont(new Font("Lorem Ipsum", Font.BOLD, 30));
				g.drawString("SPACE to RESTART", 320, 530);	
				
				highScore(g);
	
	}
		
	@Override 
        //draws the snake based on movement
	public void actionPerformed(ActionEvent args0) {
		// if snake moves through the right border it comes out throught the left border
		if(right){
			for(int i = snakeLength - 1; i >= 0; i-- ) {
				y[i + 1] = y[i];
			}
				for(int i = snakeLength; i >=0; i--) {
					if(i == 0) {
						x[i] = x[i] + 25;
					}else {
						x[i] = x[i - 1];
					}
                                        //screen wrap
					if(x[i] > 850) {
						x[i] = 25;
				}
			}
		}
		repaint();
		
                // if snake moves through the left border it comes out throught the right border
		if(left){
			for(int i = snakeLength - 1; i >= 0; i-- ) {
				y[i + 1] = y[i];
			}
				for(int i = snakeLength; i >=0; i--) {
					if(i == 0) {
						x[i] = x[i] - 25;
					}else {
						x[i] = x[i - 1];
					}
                                        //screen wrap
					if(x[i] < 25) {
						x[i] = 850;
				}
			}
		}
		repaint();
		
                // if snake moves through the top border it comes out throught the bottom border
		if(up){
			for(int i = snakeLength - 1; i >= 0; i-- ) {
				x[i + 1] = x[i];
			}
				for(int i = snakeLength; i >=0; i--) {
					if(i == 0) {
						y[i] = y[i] - 25;
					}else {
						y[i] = y[i - 1];
					}
                                        //screen wrap
					if(y[i] < 10) {
						y[i] = 625;
				}
			}
		}
		repaint();
                
		// if snake moves through the bottom border it comes out throught the top border
		if(down){
			for(int i = snakeLength - 1; i >= 0; i-- ) {
				x[i + 1] = x[i];
			}
				for(int i = snakeLength; i >=0; i--) {
					if(i == 0) {
						y[i] = y[i] + 25;
					}else {
						y[i] = y[i - 1];
					}
                                        //screen wrap
					if(y[i] > 625) {
						y[i] = 25;
				}
			}
		}
		repaint();
		
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
	@Override
        // movement
	public void keyPressed(KeyEvent e) {
		
                // ensures that the snake can not make a 180 degree turn
                
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			moves ++;
			if(!left) {
				right = true;
			}else {
				right = false;
				left = true;
			}
			up = false;
			down = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			moves ++;
			if(!right) {
				left = true;
			}else {
				right = true;
				left = false;
			}
			up = false;
			down = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			moves ++;
			if(!down) {
				up = true;
			}else {
				up = false;
				down = true;
			}
			left = false;
			right = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			moves ++;
			if(!up) {
				down = true;
			}else {
				down = false;
				up = true;
			}
			left = false;
			right = false;
		}
                
                // alternate movement keys
                if(e.getKeyCode() == KeyEvent.VK_D) {
			moves ++;
			if(!left) {
				right = true;
			}else {
				right = false;
				left = true;
			}
			up = false;
			down = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_A) {
			moves ++;
			if(!right) {
				left = true;
			}else {
				right = true;
				left = false;
			}
			up = false;
			down = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_W) {
			moves ++;
			if(!down) {
				up = true;
			}else {
				up = false;
				down = true;
			}
			left = false;
			right = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_S) {
			moves ++;
			if(!up) {
				down = true;
			}else {
				down = false;
				up = true;
			}
			left = false;
			right = false;
		}
                
                // allows user to restart the game only and only if the game has ended and is not running
                
		if((running == false) && e.getKeyCode() == KeyEvent.VK_SPACE) {
			apples =0;
			moves = 0;
			snakeLength = 3;
			startGame();
			repaint();
		}
                
                // alocates the ESC key to pause the game
                if((running == true) && e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			pauseGame();
			repaint();
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}
