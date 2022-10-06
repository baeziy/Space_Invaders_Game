// Main Menu
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
// Sound
import javax.sound.sampled.*;
import java.io.*;
// Frames
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    Main() throws Exception{
        Menu.main(new String[0]);
    }
    public static void main(String[] args) throws Exception{
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("mainCounter.txt"))) {
            writer.write("0");
            writer.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        new Main();
        while(true){
        BufferedReader reader = new BufferedReader(new FileReader("mainCounter.txt"));
        String line;
        int i = 0;
        while((line = reader.readLine()) != null){
            i = Integer.parseInt(line);
        }
            if( i == 1){
            Space_Invaders obj = new Space_Invaders();
            obj.gameLoop();
            break;
            }
            reader.close();
        }
    }
}

class Menu{
    static void main(String[] args) throws IOException{
       // new Sound();
        new MyFrame();
    }
}

class Sound{
    File file = new File("spaceMusic.wav");
    Clip clip;
   Sound(){
       System.out.print(file);
        try (AudioInputStream ais = AudioSystem.getAudioInputStream(file)) {
            clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    void close(){
        clip.close();   
    }
}

class MyFrame extends JFrame implements ActionListener{
    JButton enterButton = new JButton("Get Started");
    MyFrame(){
        
        Border border = BorderFactory.createLineBorder(Color.GREEN, 3);
        new ImageIcon(new ImageIcon("images/logo.png").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        JLabel label1 = new JLabel(); // creating label
        label1.setBounds(10,505 , 500, 100);
        label1.setText("<html>A project by:-<br>• Muhammad Mustafa Kamal Malik<br>• Abdullah Mehtab</html>");
        label1.setForeground(new Color(0x00FF00));
        label1.setFont(new Font("Space invaders", Font.PLAIN, 15));

        this.setTitle("Space Invaders"); //sets title of this
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exits out of application
        this.setResizable(false); // prevent user to change size of the frame
        this.setSize(800, 635); // sets x and y dimensions of frame
        this.setLocationRelativeTo(null);

        ImageIcon img = new ImageIcon("images/logo.png"); // imageicon created
        this.setIconImage(img.getImage()); // change icon of frame
        // this.getContentPane().setBackground(new Color(0, 0, 0));

        JLabel backgroundImageLabel = new JLabel();
        backgroundImageLabel.setIcon(new ImageIcon(new ImageIcon("images/menu-space.jpg").getImage().getScaledInstance(800, 635, Image.SCALE_DEFAULT)));
        backgroundImageLabel.setText("SPACE INVADERS"); // setting text of label
        backgroundImageLabel.setHorizontalTextPosition(JLabel.CENTER);
        backgroundImageLabel.setVerticalTextPosition(JLabel.TOP);
        backgroundImageLabel.setForeground(new Color(0x00FF00));
        backgroundImageLabel.setFont(new Font("Space invaders", Font.PLAIN, 50));
        backgroundImageLabel.setBorder(border);
        backgroundImageLabel.setVerticalAlignment(JLabel.CENTER); // set vertical position of icon + text within label
        backgroundImageLabel.setHorizontalAlignment(JLabel.CENTER); // set horizontal position of icon + text within label
        //  backgroundImageLabel.setIcon(image);
        backgroundImageLabel.setIconTextGap(-225);
        
        enterButton.setBounds(335,285,125,125);
        enterButton.addActionListener(this);
        ImageIcon buttonIcon = new ImageIcon(new ImageIcon("images/alien.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
        enterButton.setIcon(buttonIcon);
        enterButton.setHorizontalTextPosition(JButton.CENTER);
        enterButton.setVerticalTextPosition(JButton.BOTTOM);
        enterButton.setFont(new Font("Space invaders", Font.BOLD, 10));
        enterButton.setForeground(Color.green);
        enterButton.setBorder(BorderFactory.createLineBorder(Color.green,5));
        enterButton.setBorderPainted(true); 
        enterButton.setContentAreaFilled(false); 
        enterButton.setFocusPainted(true); 
        enterButton.setOpaque(false);
        
        backgroundImageLabel.add(enterButton);
        backgroundImageLabel.add(label1);
        
        add(backgroundImageLabel);
       // this.add(label);
       // this.pack();
        this.setVisible(true); // make this visible
        new Sound();
    }
    void FrameMaking(){
    }

    void LabelMaking(JLabel label, ImageIcon image, Border border){
    }
   
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == enterButton){
            this.dispose();
           // main.close();
           System.out.println("\nFrame 2 opened");
            new MyFrame2();
        }
    }
}


class MyFrame2 extends JFrame implements ActionListener{

    JLabel labelMenu = new JLabel();
    JLabel menu = new JLabel();

    JButton playGame = new JButton("Play Game");
    JButton highScore = new JButton("High Score");
    JButton instruction = new JButton("Instructions");
    JButton back = new JButton("Back");
    int backCount = 0;
    int instructionCount = 0;
    int playGameCount = 0;
    int highScoreCount = 0;

    MyFrame2(){
        Border border = BorderFactory.createLineBorder(Color.GREEN, 3);
       
        labelMenu.setIcon(new ImageIcon(new ImageIcon("images/menu-space.jpg").getImage().getScaledInstance(800, 635, Image.SCALE_DEFAULT)));
        menu.setText("Main Menu"); // setting text of label
      
        menu.setHorizontalTextPosition(JLabel.CENTER);
        menu.setVerticalTextPosition(JLabel.CENTER);
        menu.setForeground(new Color(0x00FF00));
        menu.setFont(new Font("Space invaders", Font.PLAIN, 50));
        menu.setBounds(235, 115, 500, 100);

        buttonLocation(285, 220, playGame);
        buttonLocation(285, 295, highScore);
        buttonLocation(285, 370, instruction);
        buttonLocation(285, 445, back);

        labelMenu.setBorder(border);
    
        this.setTitle("Space Invaders"); //sets title of this
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exits out of application
        this.setResizable(false); // prevent user to change size of the frame
        this.setSize(800, 635); // sets x and y dimensions of frame
        this.setLocationRelativeTo(null);

        labelMenu.add(menu);
        add(labelMenu);
        //pack();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == back){
            this.dispose();
            if (backCount == 0){
                backCount++;
                System.out.println("\nBack to MyFrame");
                new MyFrame();
            }
        }
        else if (e.getSource() == instruction){
            dispose();
            if (instructionCount == 0){
                instructionCount++;
                System.out.println("\nInstructions opened");
                new Instructions();
            }
        }
        else if( e.getSource() == playGame){
            dispose();
            
			if (playGameCount == 0){
				try (BufferedWriter writer = new BufferedWriter(new FileWriter("mainCounter.txt"))) {
					writer.write("1");
					writer.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
            }
        }

        else if( e.getSource() == highScore){
            dispose();
            if(highScoreCount == 0){
                highScoreCount++;
                try {
                    new HighScore();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    
    void buttonLocation(int xCoordinate, int yCoordinate, JButton button){
        button.addActionListener(this);
        button.setBounds(xCoordinate, yCoordinate, 200, 50);
        button.addActionListener(this);
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.BOTTOM);
        button.setFont(new Font("Space invaders", Font.BOLD, 15));
        button.setForeground(Color.green);
        button.setBorder(BorderFactory.createLineBorder(Color.green, 4));
        button.setBorderPainted(true); 
        button.setContentAreaFilled(false); 
        button.setFocusPainted(true); 
        button.setOpaque(false);

        labelMenu.add(button);
    }
}

class HighScore extends JFrame implements ActionListener{
    JLabel label;
    JLabel labelMenu = new JLabel();
    JLabel Filetext;
    Border border;
    ImageIcon spaceShipIcon;
    int backCount = 0;
    JButton back = new JButton("Back");
    HighScore() throws IOException{

        border =  BorderFactory.createLineBorder(Color.GREEN, 3);

        this.setTitle("Space Invaders Game"); //sets title of this
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exits out of application
        this.setResizable(false); // prevent user to change size of the frame
        

       // this.setLayout(null);
        
        this.setSize(800, 635); // sets x and y dimensions of frame


        

        labelMenu.setIcon(new ImageIcon(new ImageIcon("images/menu-space.jpg").getImage().getScaledInstance(800, 635, Image.SCALE_DEFAULT)));
        labelMenu.setBorder(border);

        label = new JLabel();
        label.setText("High Score");
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setVerticalTextPosition(JLabel.CENTER);
        label.setForeground(new Color(0x00FF00));
        label.setFont(new Font("Space invaders", Font.PLAIN, 50));
        label.setBounds(230, 115, 500, 100);

        Filetext = new JLabel();
        try (BufferedReader reader = new BufferedReader(new FileReader("High_Score.txt"))) {
            String line;
            while((line = reader.readLine()) != null ){
                Filetext.setText(line);  
            }
        }
        Filetext.setForeground(new Color(252, 3, 144));
        Filetext.setFont(new Font("Space invaders", Font.PLAIN, 40));
        Filetext.setBounds(350, 275, 500, 100);

        buttonLocation(275, 500, back);
        
        labelMenu.add(label);
        labelMenu.add(Filetext);
        this.add(labelMenu);

        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
            if (e.getSource() == back){
                this.dispose();
                if (backCount == 0){
                    backCount++;
                    new MyFrame2();
                }
            }        
    }

    void buttonLocation(int xCoordinate, int yCoordinate, JButton button){
        button.addActionListener(this);
        button.setBounds(xCoordinate, yCoordinate, 200, 50);
        button.addActionListener(this);
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.BOTTOM);
        button.setFont(new Font("Space invaders", Font.BOLD, 20));
        button.setForeground(Color.green);
        button.setBorder(BorderFactory.createLineBorder(Color.green, 4));
        button.setBorderPainted(true); 
        button.setContentAreaFilled(false); 
        button.setFocusPainted(true); 
        button.setOpaque(false);

        labelMenu.add(button);
    }
}

class Instructions extends JFrame implements ActionListener{
    JLabel labelMenu = new JLabel();
    JLabel instructionText = new JLabel();
    JLabel text = new JLabel();
    JButton back = new JButton("Back");

    int backCount = 0;
    
    Instructions(){

        labelMenu.setIcon(new ImageIcon(new ImageIcon("images/menu-space.jpg").getImage().getScaledInstance(800, 635, Image.SCALE_DEFAULT)));
        Border border = BorderFactory.createLineBorder(Color.GREEN, 3);

        labelMenu.setBorder(border);


        instructionText.setText("Instructions"); // setting text of label
        instructionText.setHorizontalTextPosition(JLabel.CENTER);
        instructionText.setVerticalTextPosition(JLabel.CENTER);
        instructionText.setForeground(new Color(0x00FF00));
        instructionText.setFont(new Font("Space invaders", Font.PLAIN, 40));
        instructionText.setBounds(240, 25, 500, 100);

        text.setText("<html><ul><li>Basic gameplay: The player controls the ship at the bottom of the screen, which can move only horizontally. The aliens moves both horizontally and vertically (approaching the player).  The cannon can be controlled to shoot bullets to destroy the alies.  If an alien is shot by the cannon, it is destroyedt. The Mode is HardCore, there is only one life.</li><br><li>Alien behavior: The aliens are aligned in a rectangular formation, floating slowly in horizontal direction.  As the formation reaches one side, the aliens approach the bottom by a certain amount and start floating in the opposite horizontal direction.  The aliens moves faster and faster as they are slain, or per level.</li><br><li>Scores: Each eliminated alien is worth 1 point.</li><br><li>Completing a level: When all aliens are eliminated, the level is completed and a congratulation screen is displayed within the playfield.  If the player's score is higher than the stored high score, the new high score is stored.  No signature (i.e. record holder name) is needed for the high score.  After the player hits any key, the game proceeds to the next level.</li><br><li>Game over: When player loses its life, or the aliens have reached a certain vertical position (successfully invaded the planet), the game ends and a game over screen is shown in the playfield.  If the player's score is higher than the stored high score, the new high score is stored.  After the player hits any key, the game is reset.</li></ul></html>");
        text.setForeground(new Color(0x00FF00));
        text.setFont(new Font("Space invaders", Font.PLAIN, 10));
        text.setBounds(5, 60, 750, 500);

        buttonLocation(300, 515, back);

        this.setTitle("Space Invaders"); //sets title of this
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exits out of application
        this.setResizable(false); // prevent user to change size of the frame
        this.setSize(800, 635); // sets x and y dimensions of frame

        labelMenu.add(instructionText);
        labelMenu.add(text);
        add(labelMenu);
        this.setLocationRelativeTo(null);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
            if (e.getSource() == back){
                this.dispose();
                if (backCount == 0){
                    backCount++;
                    System.out.println("\nBack to MyFrame2");

                    new MyFrame2();
                }
            }        
    }

    void buttonLocation(int xCoordinate, int yCoordinate, JButton button){
        button.addActionListener(this);
        button.setBounds(xCoordinate, yCoordinate, 200, 50);
        button.addActionListener(this);
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.BOTTOM);
        button.setFont(new Font("Space invaders", Font.BOLD, 20));
        button.setForeground(Color.green);
        button.setBorder(BorderFactory.createLineBorder(Color.green, 4));
        button.setBorderPainted(true); 
        button.setContentAreaFilled(false); 
        button.setFocusPainted(true); 
        button.setOpaque(false);

        labelMenu.add(button);
    }


    
}
