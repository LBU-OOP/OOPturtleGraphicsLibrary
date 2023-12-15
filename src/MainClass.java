import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
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
	
	private void setBackgroundColour(Color darkgray) {
		// TODO Auto-generated method stub
		
	}

	public void load() 
	{
		BufferedImage image;
		try {
			image = ImageIO.read(new File("image.png"));
			setBufferedImage(image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		repaint();
		
	}
	public void load2()
	{
		String imagePath = "image.png";
		Image indexImage = Toolkit.getDefaultToolkit().getImage(imagePath);
		
		
		BufferedImage bufferedImage = getBufferedImage(); 
	    Graphics g = bufferedImage.getGraphics();  // Graphics for the first object only
	    // Create a BufferedImage object from the loaded Image object
       // BufferedImage bufferedIndexImage = new BufferedImage(indexImage.getWidth(null),
        //    indexImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = getGraphics2DContext();
        g2d.drawImage(indexImage, 0, 0, null);
        g2d.dispose();
	 /*   try {
	        // BufferedImage object TWO
	        bufferedImage = ImageIO.read(getClass().getResource("image.jpg"));

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    setBufferedImage(bufferedImage);*/
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
	/*public void processCommand(String command)
	{
		String[] params = command.split(" ");
		command = params[0];
		//cycleColours();
		if (command.equalsIgnoreCase("forward"))
			forward(200);
		if (command.equalsIgnoreCase("circle"))
			circle(50);
		if (command.equalsIgnoreCase("turnleft"))
			turnLeft(90);
		if (command.equalsIgnoreCase("turnright"))
			turnRight(90);
		if (command.equalsIgnoreCase("pendown"))
			penDown();
		if (command.equalsIgnoreCase("penup"))
			penUp();
		if (command.equalsIgnoreCase("about"))
			about();
		if (command.equalsIgnoreCase("fill"))
		{
			Colour = 2;
			try {
				fill();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("area too big "+e);
			}
			System.out.println("fill");
		}
		if (command.equalsIgnoreCase("red"))
		{
			setPenColour(Color.red);
			System.out.println("red");
		}
			if (command.equalsIgnoreCase("green"))
			setPenColour(Color.green);
		if (command.equalsIgnoreCase("blue"))
			setPenColour(Color.blue);
		if (command.equalsIgnoreCase("set"))
		{
			
			int degrees = Integer.parseInt(params[1]);
			pointTurtle(degrees);
			
			
		}
		System.out.println(direction);
	}
*/
}
