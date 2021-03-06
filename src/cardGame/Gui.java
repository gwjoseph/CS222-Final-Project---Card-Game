package cardGame;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

public class Gui {
	private static JFrame window = new JFrame("Tinker Theory");
   
    public static void openGUI() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(); 
            }
        });
    }
    
    public static void repaint(){
    	window.repaint();
    }

    private static void createAndShowGUI() {
    	
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setMinimumSize(new Dimension(1000, 700));
       
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        
        //custom cursor 
        Toolkit toolkit = Toolkit.getDefaultToolkit();  
        Image image = Toolkit.getDefaultToolkit().getImage(CardGame.class.getResource("/resources/cursor.png"));
        Point hotSpot = new Point(0,0);
        Cursor cursor = toolkit.createCustomCursor(image, hotSpot, "Pencil");
        mainPanel.setCursor(cursor); 
        
        PaintPanel paintPanel  = new PaintPanel();
        JPanel buttonPanel     = new JPanel();
        
        JButton button1 = new JButton("Card 1");
        button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ActionHandler.buttonPressed(1);
			}
		});
        
    	JButton button2 = new JButton("Card 2");
    	button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ActionHandler.buttonPressed(2);
			}
		});
    	
    	JButton button3 = new JButton("Card 3");
    	button3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ActionHandler.buttonPressed(3);
			}
		});
    
        buttonPanel.add(button1);
        buttonPanel.add(button2);
        buttonPanel.add(button3);
        
        mainPanel.add(paintPanel);
        mainPanel.add(buttonPanel);
        
        window.add(mainPanel);
        
        window.pack();
        window.setVisible(true);
        
    }
}

class PaintPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	static final int WINDOW_WIDTH    = 1000;
	static final int WINDOW_HEIGHT   = 600;
	static final int CARD_WIDTH      = 175;
	static final int CARD_HEIGHT     = 224;
	static final int BUTTON_WIDTH    = 150;
	static final int BUTTON_HEIGHT   = 60;
	static final int ATTACK_STRING_X = 10;
	static final int ATTACK_STRING_Y = 20;

    public PaintPanel() {
        setBorder(BorderFactory.createLineBorder(Color.black));
    }

    public Dimension getPreferredSize() {
        return new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    public void paintComponent(Graphics g) {
    	
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        Font font = new Font("SansSerif", Font.PLAIN, 18);
        Font bigFont = new Font("SansSerif", Font.PLAIN, 100);
        g2.setFont(font);

        if(Combat.playerAttacking()){
        	g2.drawString("You are on the Attack.", ATTACK_STRING_X, ATTACK_STRING_Y);
        	
        } else {
        	g2.drawString("You are on the Defense.", ATTACK_STRING_X, ATTACK_STRING_Y);
        	
        }
        
        if(Combat.getGameOver()){
            g2.setFont(bigFont);
            
        	if(Combat.getVictory() == BattleOutcome.win){
        		g2.drawString("You win!", 550, 100);
        		
        	} else if(Combat.getVictory() == BattleOutcome.lose) {
        		g2.drawString("You lose.", 550, 100);
        		
        	} else {
        		g2.drawString("Tie.", 550, 100);
        	}
        	
            g2.setFont(font);
        }
        
        g2.drawString("Turn "+Combat.getTurn(), 10, 50);
        g2.drawString("Score - You: "+Combat.getPlayerInfo().getPoints()+
        		" Opponent "+Combat.getOpponentInfo().getPoints(), 100, 50);
        
        
        
        for(int i = 0; i < Combat.getPlayerInfo().getHand().size(); i++){ //Draw the user's hand.
        	drawCard(g2, 10 + ((CARD_WIDTH + 20) * i), 360, Combat.getPlayerInfo().getHand().lookAt(i));
        }
        
        if(Combat.getPlayerLastPlayed() != null){
        	drawCard(g2, 100, 100, Combat.getPlayerLastPlayed());
        	g2.drawString("Your Card", 130, 95);
        	
        	if(Combat.playerAttacked()){
        		g2.drawString("Attacker", 150, 340);
        	} else {
        		g2.drawString("Defender", 150, 340);
        	}
        }
        
        if(Combat.getOpponentLastPlayed() != null){
        	drawCard(g2, 300, 100, Combat.getOpponentLastPlayed());
        	g2.drawString("Opponent's Card", 310, 95);
        	
        	if(Combat.playerAttacked()){
        		g2.drawString("Defender", 350, 340);
        	} else {
        		g2.drawString("Attacker", 350, 340);
        	}
        }
  
    } 
   
    public void drawCard(Graphics2D g2, int x, int y, Card card){
    	
    	String name    = card.getName();
    	String attack  = Integer.toString(card.getAttack());
    	String defense = Integer.toString(card.getDefense());
    	int id = Library.getIdOfCard(card);  	
    	
    	Image img2 = Toolkit.getDefaultToolkit().getImage(CardGame.class.getResource("/resources/card.png"));
        g2.drawImage(img2, x, y, this);
        
        if(id >= 0){ //tests to see if the card has a valid ID.
        	//fetches the PNG associated with the card ID to be drawn.
    		Image img1 = Toolkit.getDefaultToolkit().getImage(CardGame.class.getResource("/resources/"+id+".png"));
    		g2.drawImage(img1, x + 10, y + 25, this);
    	}
        
    	FontMetrics metrics = g2.getFontMetrics();
        
    	g2.drawString(name,    x + 5, y + 18); //the lines below have calculations to center the text
    	g2.drawString(attack,  x + (15 - (metrics.stringWidth(attack) / 2)),               y+CARD_HEIGHT-5);
    	g2.drawString(defense, x + CARD_WIDTH - (16 + (metrics.stringWidth(defense) / 2)), y+CARD_HEIGHT-5);
    	
    }
}