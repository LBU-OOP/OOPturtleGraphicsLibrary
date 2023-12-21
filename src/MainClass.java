import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import uk.ac.leedsbeckett.oop.OOPGraphics;

public class MainClass extends OOPGraphics{

	public static void main(String[] args) {
		new MainClass();
		
		// TODO Auto-generated method stub

	}

	public MainClass()
	{
		 JFrame MainFrame = new JFrame();                //create a frame to display the turtle panel on
         MainFrame.setLayout(new FlowLayout());      //not strictly necessary
         MainFrame.add(this);                                        //"this" is this object that extends turtle graphics so we are adding a turtle graphics panel to the frame
         MainFrame.pack();                                           //set the frame to a size we can see
         MainFrame.setVisible(true);                         //now display it
                                                                   //call the TurtleGraphics about method to display version information.
		//setStroke(10);
		//circle(150);
		//about();
		//penDown();
		
		//setPenColour(Color.green);
		//BufferedImage bufImg = getBufferedImage();
		//Graphics g = getGraphicsContext();
		//g.setColor(Color.red);
		//g.drawLine(0, 0, 250, 500);
		
		
	}
	
	@Override
	 public void processCommand(String command)      //this method must be provided because OOPGraphics will call it when it's JTextField is used
    {
            //String parameter is the text typed into the OOPGraphics JTextfield
            //lands here if return was pressed or "ok" JButton clicked
            //TO DO 
    	command = command.toLowerCase();
    	String[] sut = command.split(" ");
    	int parameter=0;
    	String cmd = sut[0];
    	if (sut.length > 1)
		{
    		try
    		{
    			parameter = Integer.parseInt(sut[1]);
    		}catch(NumberFormatException e)
    		{
    			return;
    		}
		}
    	switch (cmd)
    	{
    		case "pendown":
    			penDown();
    			break;
    		case "penup":
    			penUp();
    			break;
    		case "forward":
    			forward(parameter);
    			break;
    		case "reset":
    			reset();
    			break;
    		case "left":
    			turnLeft();
    			break;
    		case "right":
    			turnRight();
    			break;
    		case "load":
    			load();
    			break;
    		case "save":
    			save();
    			break;
    		case "clear":
    			//setBackground_Col(Color.green);
    			clear();
    			break;
    		case "circle":
    			circle(parameter);
    			break;
    		case "about":
    			about();
    			break;
    		case "point":
    			pointTurtle(parameter);
    			break;
    		default:
    			System.out.println("unknown");
    	}
    	System.out.println("command = "+command);
    }
	

	public void load() 
	{
		BufferedImage image;
		try {
			image = ImageIO.read(new File("image.png"));
			setBufferedImage(image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error with library, turtle image not found.");
		}
	
		repaint();
		
	}

	public void save()
	{
		
		File outputfile = new File("image.png");
		try {
			ImageIO.write(getBufferedImage(), "png", outputfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
