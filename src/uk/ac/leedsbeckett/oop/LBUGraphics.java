package uk.ac.leedsbeckett.oop;


import java.awt.BasicStroke;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * LBUGraphics (Duncan Mullier, Leeds Beckett University)
 * extended JPanel with simple drawing commands and a visual representation of a turtle to perform "turtle graphics" drawing operations.
 * <h2>Adding the Jar File</h2>
 * The jar file should be added to your build path. You must have created a project and be in the package explorer view if you don't see it (Window->Show View->Package Explorer)
 * Right click on your project, select "Build Path-Add External Archive" and add jar file.
 * It will appear in your project explorer under "referenced libraries", inside the jar will be LBUGraphics.class.
 * Don't forget to look at the inherited methods from JPanel and above, which will also be if use.
 * 
 * <h2>Updating the Jar File</h2>
 * If you need to update the jar file then remove the old one by expending Referenced Libraries in your project so that LBUGraphics.jar appears.
 * Right click on LBUGraphics.jar and select Build path->Remove From Build Path.
 * You will get syntax errors in your project where it references LBUGraphics but you can now ass the new version of LBUGraphics.jar using the steps above.
 * @author Duncan Mullier
 * @version 4.4 
 * 
 * All software has bugs, if you find one please report to author. Ensure you have the latest version
 * V4.5 setPanelSize(int, int) now revalidates the display so its effect is immediately seen.
 * V4.4 updated documentation, changed reset to point turtle down
 * V4.3 change back to bitmap from index color model. 
 * V4.2 fixed bugs -no animation with turn without integer and setPenColour not working fixed>
 * V4.1 January 2023 exception added for fill operation
 * V4.0 rewritten to have pixel by pixel animated turtle
 * V3.1 threaded about() now holds execution until it has completed, added stroke and graphics2d
 * V2.0 adds simple GUI interface, now an abstract class with CommandLineInterface Interface
 *<h2> example code </h2>
 * <pre>
import java.awt.FlowLayout;

import javax.swing.JFrame;

import uk.ac.leedsbeckett.oop.LBUGraphics;

public class Main extends LBUGraphics
{
	public static void main(String[] args)
	{
		new Main(); //create instance of class that extends LBUGraphics (could be separate class without main), gets out of static context
	}

	public Main()
	{
		JFrame MainFrame = new JFrame();		//create a frame to display the turtle panel on
		MainFrame.setLayout(new FlowLayout());	//not strictly necessary
		MainFrame.add(this);					//"this" is this object that extends turtle graphics so we are adding a turtle graphics panel to the frame
		MainFrame.pack();						//set the frame to a size we can see
		MainFrame.setVisible(true);				//now display it
		about();								//call the LBUGraphics about method to display version information.
	}

	
	public void processCommand(String command)	//this method must be provided because LBUGraphics will call it when it's JTextField is used
	{
		//String parameter is the text typed into the LBUGraphics JTextfield
		//lands here if return was pressed or "ok" JButton clicked
		//TO DO 
	}
}

</pre>
@since 3/2023
 */
@SuppressWarnings("serial")

public abstract class LBUGraphics extends JPanel implements ActionListener, CommandLineInterface
{

	/**
	 * public version number.
	 */
	public final float VERSION = 4.5f; 
	private  Color background_Col = Color.BLACK;	//background colour of the panel
	private final static int TURTLE_X_SIZE = 72, TURTLE_Y_SIZE = 69;
	private final int TURTLESTARTX = 800, TURTLESTARTY = 400;
	private  int panelWidth = TURTLESTARTX;
	private  int panelHeight = TURTLESTARTY;
	private float StrokeWidth = 1;
	private BasicStroke Stroke = new BasicStroke( StrokeWidth );
	
	
	private JTextField commandLine = null;
	private JLabel messages = null;
	private JButton okBut = null;
	/**
	 * The underlying image used for drawing. This is required so any previous drawing activity is persistent on the panel.
	 * image is the drawing area and turtleImage is for the graphical representation of the turtle/pen
	 */
	private BufferedImage image, turtleDisplay, turtle0;
	//index colour model, palette of 16 RGB colours with no alpha (so 3 elements, 4 bits - 16 colours)

	private IndexColorModel colourModel; // = new IndexColorModel(4,16,red, green, blue); //palette image
	WritableRaster raster; 		//actual array of pixels.
	//colour palette for indexed colour image
	int[] colors = {
		    0x000000, // Black
		    0x000080, // Navy
		    0x008000, // Green
		    0x008080, // Teal
		    0x800000, // Maroon
		    0x800080, // Purple
		    0x808000, // Olive
		    0xC0C0C0, // Silver
		    0x808080, // Gray
		    0x0000FF, // Blue
		    0x00FF00, // Lime
		    0x00FFFF, // Aqua
		    0xFF0000, // Red
		    0xFF00FF, // Fuchsia
		    0xFFFF00, // Yellow
		    0xFFFFFF  // White
		};
	
	/**
	 * Colour of the pen the turtle draws with (A Java Color)
	 */
	protected int Colour = 15; //indexed pallette colour to draw in
	protected Color PenColour = Color.RED;
	protected int penSize = 1; //used for raw drawing
	
	/**
	 * a moving turtle will draw if this is true and not if it is false (set by penDown and PenUp methods)
	 */
	protected boolean penDown = false;
	
	/**
	 * x position of the turtle on the screen
	 */
	protected int xPos=100;
	
	/**
	 * y position of the turtle on the screen
	 */
	protected int yPos=100;
	
	/**
	 * direction the turtle is pointing in in degrees
	 */
	protected int direction = 180; //robot pointing down the screen;
	
	/**
	 * delay for turtle animation
	 */
	private int sleepPeriod=1; //delay for turtle animation thread
	protected int turtleSpeed = 1; //speed for turtle animation

	/**
	 * must be implemented in your class so that TurtleGraphics can call your code when something happens at the LBUGraphics GUI (i.e. user presses return in text field or clicks ok button).
	 * If you do not implement this method you will get a syntax error.
	 * @param command is the String typed into the text field before return was pressed or ok was clicked.
	 */
	public abstract void processCommand(String command);
	
	
	//takes a string, splits it and tried to convert it into integers
	//if it can't it throws a numberformatexception
	private int[] getParameters(String args) 
	{
		String[] split = args.split(" ");
		int[] params = new int[split.length];
		for (int i=0; i<split.length; i++)
		{
			try
			{
				params[i] = Integer.parseInt(split[i]);
			}
			catch(NumberFormatException e)
			{
				throw new NumberFormatException("***LBUGraphics Exception*** cannot convert parameter "+(i+1)+" (\""+split[i]+ "\") to an integer");				
			}
		}
		return params;
	}
	
	
		
	/**
	 * returns the graphicsContext of the Turtle display so you can draw on it using the normal Java drawing methods
	 * example
	 * Graphics g = getGraphicsContext();
	 *	g.setColor(Color.red);
	 *	g.drawLine(0, 0, 250, 500);
	 * @return graphics context
	 */
	public Graphics getGraphicsContext()
	{
		return  image.getGraphics();
	}
	
	/**
	 * returns the graphicsContext of the Turtle display so you can draw on it using the extended Graphics2Dl Java drawing methods
	 * example
	 * Graphics g = getGraphics2DContext();
	 *	g.setColor(Color.red);
	 *	g.drawLine(0, 0, 250, 500);
	 * @return graphics context
	 */
	public Graphics2D getGraphics2DContext()
	{
		Graphics2D g2 = (Graphics2D) getGraphicsContext();
		return  g2;
	}
	
	/**
	 * return a BufferedImage of the display, so that it can be saved
	 * example
	 *  	BufferedImage bufImg = getBufferedImage(); 
	 * 		Graphics g = bufImg.getGraphics();
	 * 		g.drawLine(0,0,250,250);
	 * @return BufferedImage of display
	 */
	public BufferedImage getBufferedImage()
	{
		return image;
	}
	
	/**
	 * sets the background image to be the passed in BufferedImage, so for example, a previously saved image can be restored
	 * @param newImage saved BufferedImage
	 */
	public void setBufferedImage(BufferedImage newImage)
	{
		image = newImage;
		repaint();
	}
	
	
	
	/**
	 * getPenColour returns the colour that the turtle draws in
	 * @return java Color
	 */
	public Color getPenColour()
	{
		return PenColour;
	}
	
	/**
	 * Get the width in pixels of the pen.
	 * @return stroke width
	 */
	public float getStroke()
	{
		return this.StrokeWidth;
	}
	/**
	 * sets the width of the line drawn
	 * @param strokeWidth integer representing the thickness of the line
	 *
	 */
	public void setStroke(int strokeWidth)
	{
			Stroke = new BasicStroke(strokeWidth);
			this.StrokeWidth = strokeWidth;
	
		Graphics2D g2 = (Graphics2D) image.getGraphics();
		g2.setStroke(Stroke);
		
	}
	
	/**
	 * setPenColour sets the colour that the turtle will draw in
	 * @param col  Java Color to set the pen to
	 * 
	 */
	public void setPenColour(Color col)
	{
		PenColour = col;
	}
	
	/**
	 * getDirection gets the direction the turtle is pointing in.
	 * @return direction in degrees
	 */
	public int getDirection()
	{
		return direction;
	}
	
	/**
	 * draws a line directly on the panel without affecting the turtle, it uses the stroke previously set by setStroke()
	 * @param color colour to draw in 
	 * @param x1 xpos of start of line
	 * @param y1 ypos of start of line
	 * @param x2 xpos of end of line
	 * @param y2 ypos of end of line
	 */
	public void drawLine(Color color, int x1, int y1, int x2, int y2) 
	{
		//x2+=10;
		//y2+=10;
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setColor(color);
		g.setStroke(Stroke);
		g.drawLine(x1, y1, x2, y2);
	
	}
	
	/** gets the background colour used by clear() to fill the panel.
		 * @return the background_Col used when clear() is called.
		 */
		public Color getBackground_Col() 
		{
			return background_Col;
		}

		/** sets the background colour used by clear() to fill the panel.
		 * @param background_Col the background_Col to set (used when clear() is called.).
		 */
		public void setBackground_Col(Color background_Col)
		{
			this.background_Col = background_Col;
		}

		/** get the x position of the turtle
		 * @return the xPos
		 */
		public int getxPos() {
			return xPos;
		}

		/** manually set the x position of the turtle (i.e. lift the turtle up and drop it).
		 * @param xPos the xPos to set
		 */
		public void setxPos(int xPos) {
			this.xPos = xPos;
		}

		/** get the y position of the turtle
		 * @return the yPos
		 */
		public int getyPos() {
			return yPos;
		}

		/** manually set the y position of the turtle (i.e. lift the turtle up and drop it).
		 * @param yPos the yPos to set
		 */
		public void setyPos(int yPos) {
			this.yPos = yPos;
		}

	/**
	 * draws a simple graphic on the canvas and reports the version number of this class
	 */
	public void about()
	{
		
		Thread t = new Thread() 
		{
			private int savepenstroke;

			public void run() 
			{
				final int aboutX = 250, aboutY = 200;
				int saveX = getxPos();
				int saveY = getyPos();
				int saveDirection = getDirection()+90;
				int saveSleep = sleepPeriod;
				sleepPeriod = 1;
				int dx = 50;
			
				Graphics g = image.getGraphics();
				Color savePen = getPenColour(); //save drawing pen
				float penstroke = getStroke();
				boolean savePenState = getPenState();
				//move turtle to start position
				penUp();
				pointTurtle(270);
				bresenham(xPos,yPos,aboutX,aboutY);
				turnLeft();
				penDown();
				setStroke(10);
				setPenColour(Color.red);//Colour = 12;
				circle(50);
				penUp();
				turnLeft(90);
				forward(150);
				penDown();
				setPenColour(Color.green);//Colour = 9;
				circle(50);
				
				penUp();
				forward(100);
				penUp();
				setPenColour(Color.blue);//Colour = 2;
				turnLeft(90);
				forward(50);
				penDown();
				turnRight(180);
				forward(100);
				turnLeft(180);
				penUp();
				forward(75);
				turnRight(90);
				forward(25);
				penDown();
				circle(25);
				penUp();
				forward(100);
				turnRight(360);
				
				PenColour = Color.GREEN;
				g.setColor(Color.gray);
				g.drawString("LBUGraphics Version "+VERSION,225,200);
				g.setColor(Color.white);
				g.drawString("LBUGraphics Version "+VERSION,228,203);
				penSize=1;
		
				
				PenColour = savePen; //restore pen
				sleepPeriod = 100;
				
				sleepPeriod = saveSleep;
				//move turtle back to start position
				penUp();
				pointTurtle(270);
				bresenham(xPos,yPos,saveX,saveY);
				setxPos(saveX);
				setyPos(saveY);
				setPenState(savePenState);
				pointTurtle(saveDirection);
				setStroke(savepenstroke);
				repaint();
				//reset();
			}
		};
		t.start();
		while(t.isAlive()); //wait until drawing finished.
		
	}
	
	/**
	 * sets the speed of the turtle's movement.
	 * @param speed 0 is fastest 1 for each microsecond of delay
	 */
	public void setTurtleSpeed(int speed)
	{
		this.sleepPeriod = speed;
	}
	/**
	 * returns the current state of the pen
	 * @return true if pen up, false if pen down
	 */
	
	public boolean getPenState()
	{
		return penDown;
	}
	
	/**
	 * Change the pen state (true is down, false is up)
	 * @param state of pen
	 */
	public void setPenState(boolean state)
	{
		penDown = state;
	}
	
	/**
	 * puts pen down so a line will be drawn when the turtle is moved
	 */
	public void penDown()
	{
		penDown = true;
	}
	
	/**
	 * puts pen up so a line will not be drawn when turtle is moved
	 */
		public void penUp()
	{
		penDown = false;
	}
	
		/**
		 * turtle is rotated to the right in degrees. 
		 * @param amount is a String representation of the degrees to rotate
		 * if param cannot be converted to an integer a NumberFormatException is thrown.
		 */
		public void turnRight(String amount)
		{
			int degrees = getParameters(amount)[0];
			turnRight(degrees);
		}

		
	/**
	 * turtle is rotated 90 degrees to the right. i.e. if it is facing upwards (north) before it will facing right (east) after  	
	 * 
	 */
	public void turnRight()
	{
		turnRight(90);
	}
	
	/**
	 * turtle is rotated to the right by amount degrees. i.e. it will rotate right by amount degrees  	
	 * The turtle will wrap around if it goes beyond 360
	 * @param amount is an integer 
	 */
	public void turnRight(int amount)
	{
		amount = amount%360;
		final int a = amount;
		Thread t = new Thread() 
		{
			public void run() 
			{
				
				for(int i=0; i<a; i++)
				{
					direction++;
					repaint();
					try {
						Thread.sleep(sleepPeriod);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} //wait until drawing finished.
				}
			}
			
		};
		t.start();
		while(t.isAlive());
		//direction = direction + amount;
		direction = direction %360;
		repaint();
		
	}
	
	/**
	 * turtle is rotated to the left in degrees. 
	 * @param amount is a String representation of the degrees to rotate
	 * if param cannot be converted to an integer a NumberFormatException is thrown.
	 */
	public void turnLeft(String amount)
	{
		int degrees = getParameters(amount)[0];
		turnLeft(degrees);
	}
	

	
	/**
	 * turtle is rotated 90 degrees to the left. i.e. if it is facing upwards (north) before it will facing left (west) after  	
	 * 
	 */
	public void turnLeft()
	{
		turnLeft(90);
	}
	
	/**
	 * turtle is rotated 90 degrees to the right by amount degrees. i.e. it will rotate right by amount degrees  	
	 * The turtle will wrap around if it goes beyond 360
	 * @param amount is an integer
	 */
	public void turnLeft(int amount)
	{
		amount = amount%360;
		final int a = amount;
		Thread t = new Thread() 
		{
			public void run() 
			{
				
				for(int i=0; i<a; i++)
				{
					direction--;
					repaint();
					try {
						Thread.sleep(sleepPeriod);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} //wait until drawing finished.
				}
			}
			
		};
		t.start();
		while(t.isAlive());
		//direction = direction + amount;
		direction = direction %360;
		repaint();
	}
	
	/**
	 * rotate the turtle from its current rotation to the desired, absolute angle in the shortest direction, showing the animation
	 * @param degrees rotation amount
	 * 
	 */
	public void pointTurtle(int degrees)
	{
		//shift origin
		degrees -=90;
		if(degrees <0)
		{
			degrees = 360 + degrees;	//+ because it's negative
		}
		//clockwise or anticlockwise?
		int rot = degrees - direction;
		if (rot<0)
		{
			rot*=-1;	//make positive
			turnLeft(rot);
		}
		else
			turnRight(rot);
	}
	/**
	 * turtle is moved in the direction it is pointing by given nuymber of pixels. 
	 * @param amount is a String
	 * if param cannot be converted to an integer a NumberFormatException is thrown.
	 */
	public void forward(String amount)
	{
		int amt = getParameters(amount)[0];
		forward(amt);
	}
	
	
	/**
	 * move the turtle (in the direction it is pointing) by {distance} pixels. A line will be drawn if the pen is down, not if it is up
	 * 
	 * @param distance in an integer
	 */
	public void forward(int distance)
	{
		
		 double angle = (Math.PI / 180.0) * direction; //angle converted to radians
		 
		 double x1 = xPos;
		 double y1 = yPos;
		 double x2 = x1 + (Math.cos(angle) * distance);
		 double y2 = y1 + (Math.sin(angle) * distance);
		
		bresenham(xPos, yPos, (int)x2,(int) y2);		//move the turtle point by point
		//now robot has moved to the new position
		xPos = (int)x2;
		yPos = (int)y2;
			
		try {
				Thread.sleep(sleepPeriod);
		} catch (InterruptedException e)
		{
		} 
	}

	/**
	 * Clears the image contents with the current background colour.
	 */
	public void clear() 
	{
		
		Graphics g = image.getGraphics();
		g.setColor(background_Col);
		g.fillRect(0, 0, image.getWidth(),  image.getHeight());
		repaint();
	}
	
	/**
	 * set graphic to be the one in the correct direction the turtle is pointing by rotating the image
	 */
	
	private void setTurtleGraphicDirection()
	{
		final double rads = Math.toRadians(direction);
		final double sin = Math.abs(Math.sin(rads));
		final double cos = Math.abs(Math.cos(rads));
		final int w = (int) Math.floor(turtle0.getWidth() * cos + turtle0.getHeight() * sin);
		final int h = (int) Math.floor(turtle0.getHeight() * cos + turtle0.getWidth() * sin);
		  turtleDisplay = new BufferedImage(w, h, turtle0.getType());
		final AffineTransform at = new AffineTransform();
		at.translate(w / 2, h / 2);
		at.rotate(rads,0, 0);
		at.translate(-turtle0.getWidth() / 2, -turtle0.getHeight() / 2);
		final AffineTransformOp rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		turtleDisplay = rotateOp.filter(turtle0,turtleDisplay);
		
	}
	
	
	/**
	 * draws a circle 
	 * @param radius radius of the circle to draw
	 * 
	 */
	public void circle(int radius)
	{
		int saveDirection = direction;
		boolean savePendown = penDown;
		int savex = xPos;
		int savey = yPos;
		//direction = 90;
		//move turtle to outer edge of circle
		pointTurtle(0);
		penUp();
		forward(radius);
		penDown = savePendown;
		xPos = savex;
		yPos = savey;
		drawCircle(radius, xPos, yPos);  
		//move turtle back
		pointTurtle(180);
		penUp();
		forward(radius);
		penDown = savePendown;
		direction = saveDirection;
	}
	/**
	 * Perform a flood fill operation at the current turtle position, needs to be surounded by a graphical area or exception is thrown
	 * @throws Exception when fill area is too big
	 */
	public void fill() throws Exception
	{
		try
		{
			int pixels[] = new int[4];
			System.out.println(xPos+"    "+yPos);
			raster.getPixel(xPos, yPos, pixels); //get colour at turtle as colour to fill
			int currentColor = pixels[0];
			floodfill(raster, 0, 14, xPos,yPos);
		}
		catch(java.lang.StackOverflowError e)
		{
			throw new Exception("Fill area too big");
		}
		//floodfill(raster, currentColor, Colour, xPos,yPos);
	}
	@Override
	/**
	 * overridden paintComponent method to handle image updating (do not call directly, use repaint();)
	 */
	public void paintComponent(Graphics g) 
	{
		setTurtleGraphicDirection();
		// render the image on the panel.
		///colourModel = new IndexColorModel(4, 16, colors, 0, false, -1, DataBuffer.TYPE_BYTE); //create new colour model in case palate had been updated
		///image = new BufferedImage(colourModel, image.getRaster(), colourModel.isAlphaPremultiplied(), null);
		g.drawImage(image, 0, 0, null);
		g.drawImage(turtleDisplay, xPos-TURTLE_X_SIZE/2, yPos-TURTLE_Y_SIZE/2, null);
		
	}

	/**
	 * set the preferred size of the LBUGraphics panel
	 * @param width in pixels
	 * @param height in pixels
	 */
	public void setPreferredSize(int width, int height)
	{
		//System.out.println("setting size");
		setPreferredSize(new Dimension(width, height));
	}
	
	/**
	 * Replace the standard turtle image with given image
	 * give full path or store in application directory
	 * don't make the image too big, have background of the image transparent and it should be pointing right (90 degrees)
	 * best to make the image have a transparent background(Google it).
	 * @param filename file or path and filename of the image to load
	 */
	public void setTurtleImage(String filename)
	{
		File file = new File(filename);

		try {
			turtle0 = ImageIO.read(file);
			
		} catch (IOException e) {
			//throw new IOException("turtle images not found, you must have turtle0/90/180/270.png in project directory");
			e.printStackTrace();
		}
	}
	
	/**
	 * Resize the turtle area, current display will be lost.
	 * @param xSize width of panel
	 * @param ySize height of panel
	 */
	public void setPanelSize(int xSize, int ySize)
	{
		panelHeight = ySize;
		panelWidth = xSize;
		///image = new BufferedImage(panelWidth, panelHeight, BufferedImage.TYPE_INT_RGB);
		setPreferredSize(new Dimension(panelWidth, panelHeight));
		clear();
		revalidate();
		repaint();
	}
	
	/**
	 * hide or show the textfield, ok button and label
	 * @param visible true to show, false to hide
	 */
	public void setGUIVisible(boolean visible)
	{
		messages.setVisible(visible);
		commandLine.setVisible(visible);
		okBut.setVisible(visible);
	}
	
	/**
	 * reset() moves turtle to initial position
	 */
	public void reset()
	{
		penUp();
		bresenham(xPos, yPos, TURTLESTARTX/2, TURTLESTARTY/2);
		//setxPos();
		//setyPos();
		setPenState(false);
		pointTurtle(180);
		setStroke(1);
		repaint();
		
	}
	
	/**
	 * display a message in the message JLabel
	 * @param message string to display in the panel's textfield
	 */
	public void displayMessage(String message)
	{
		this.messages.setText(message);
		
	}
	
	/**
	 * clears JTextField and JLabel
	 */
	public void clearInterface()
	{
		this.messages.setText("");
		this.commandLine.setText("");
	}
	
	/**
	 * raw drawing methods
	 */
	
	/**
	 * draw a circle using the polynomial method to give access to each pixel, turtle is updated in a thread
	 * @param radius circlue radius
	 * @param x circle x position
	 * @param y circle y position
	 */
	
	public void drawCircle(int radius, int x, int y)
	{
		//set turtle to right
		
		//declare array lists to hold 8xoctant data xcoords[8][data]
		final int octants = 8;
		ArrayList<ArrayList<Integer>> xcoords = new ArrayList<>(octants);
		ArrayList<ArrayList<Integer>> ycoords = new ArrayList<>(octants);
		//initialise multi-dimensional arraylists
		for(int i=0; i < octants; i++) {
		    xcoords.add(new ArrayList<Integer>());
		    ycoords.add(new ArrayList<Integer>());
		}
		
		Thread t = new Thread() //needs to be in a thread so system can update the display
		{
			public void run() 
			{
				int speed = 0;
				int count = 0;
				int gd=0, gm; //,h,k,r;  
				double xd,yd,x2;  
				int h= x, k= y, r=radius;  
				xd=0;
				yd=r;  
				x2 = r/Math.sqrt(2);  
				while(xd<=x2)  
				{  
					yd = Math.sqrt(r*r - xd*xd);  
					int x = (int) xd;
					int y = (int) yd;
					
					//up 3
				    xcoords.get(3).add(x+h);
				    ycoords.get(3).add(y+k);
					//down 0
				    xcoords.get(0).add(x+h);
					ycoords.get(0).add(-y+k);
				    //down 7
				    xcoords.get(7).add(-x+h);
					ycoords.get(7).add(-y+k);
				    //up 4
				    xcoords.get(4).add(-x+h);
					ycoords.get(4).add(y+k);
					//down 2
				    xcoords.get(2).add(y+h);
					ycoords.get(2).add(x+k);
					//up 1
				   	xcoords.get(1).add(y+h);
					ycoords.get(1).add(-x+k);
					//up 6
				   	xcoords.get(6).add(-y+h);
					ycoords.get(6).add(-x+k);
					//down 5
					xcoords.get(5).add(-y+h);
					ycoords.get(5).add(x+k);
					xd += 1;  
					count++;
					
				} 
				//calculate number of rotations
				int rotations = xcoords.get(0).size() * 8; //plots per octants x octants
				int round = (Math.round(rotations/360)) + 1;
				
				//calculated 8 octs now plot them in order
				int plots = xcoords.get(0).size(); //the number of plots in this circle
				for(int i=0; i<8; i++)
				{
					int start = 0;
					int end = count-1;
					int step = 1;
					if(i % 2 == 1 )		//some octs are calculated in the opposite direction (for drawing)
					{
						start = count-1;
						end = 0;
						step = -1;
					}
					else
					{
						start = 0;
						end = count;
						step = 1;
					}
					for(int j=start; j != end; j = j + step)
					{
						//turn the turtle a bit for each plot
						if(j % round == 0)
							direction++;
						setPixel(xcoords.get(i).get(j),ycoords.get(i).get(j), Colour, raster);//drawLine(PenColour, xcoords.get(i).get(j) , ycoords.get(i).get(j) , xcoords.get(i).get(j)+penSize, ycoords.get(i).get(j));//[i][j], ys[i][j],  xs[i][j]+10, ys[i][j]+10); 
					   //spiral++;
						try {
							Thread.sleep(sleepPeriod);
						} catch (InterruptedException e) {
							
						} //wait until drawing finished.
					   xPos = xcoords.get(i).get(j);
					   yPos = ycoords.get(i).get(j);
					   if (speed++ % turtleSpeed == 0)
						   repaint();
					}
				}
			}
		};
		t.start();
		while(t.isAlive());
		
	}
	
	/**
	 * plots a bresenham line point by point. It draws if the pen is down otherwise it just moves the turtle
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	void bresenham(int x1, int y1, int x2, int y2) 
	{    
		Thread t = new Thread() 
		{
			
			public void run() 
			{
				int dx = x2 - x1;
				int dy = y2 - y1;
		
			    int error;
			    /** first quarter */
			    if(dx >= 0 && dy >= 0) 
			    {
			        /** 1st octant */
			        if (dx >= dy) 
			        {
			            error = -dx;
			            int y = y1;
			            for(int x = x1; x < x2; x++) 
			            {
			            	
			            	sleepThread(x, y);
			            	if (penDown)
			            		setPixel(x,y, Colour, raster);
			                
			            		error = error + 2 * dy;
			                if (error >= 0) {
			                    y++;
			                    error = error - 2 * dx;
			                }
			            }
			        }
			        /** 2nd octant */
			        else {
			            error = -dy;
			            int x = x1;
			            for(int y = y1; y < y2; y++) 
			            {
			            	sleepThread(x, y);
			            	if (penDown) setPixel(x,y, Colour, raster);
			                error = error + 2 * dx;
			                if (error >= 0) {
			                    x++;
			                    error = error - 2 * dy ;
			                }
			            }
			        }
			    }
			    /** second quarter */
			    else if (dx <= 0 && dy >= 0) 
			    {
			        /** 4th octant */
			        if(dx < -dy) {
			            error = dx;
			            int y = y1;
			            for(int x = x1; x > x2; x--) 
			            {
			            	sleepThread(x, y);
			            	if (penDown) setPixel(x,y, Colour, raster);
			                error = error + 2 * dy;
			                if (error >= 0) {
			                    y++;
			                    error = error + 2 * dx;
			                }
			            }
			        }
			        /** 3rd octant */
			        else {
			            error = -dy;
			            int x = x1;
			            for(int y = y1; y < y2; y++) 
			            {
			            	sleepThread(x, y);
			            	if (penDown) setPixel(x,y, Colour, raster);
			                error = error - 2 * dx;
			                if (error >= 0) 
			                {
			                    x--;
			                    error = error - 2 * dy;
			                }
			            }
			        }
			    }
			    /** 3rd quarter */
			    else if (dx <= 0 && dy <= 0) 
			    {
			        /** 5th octant */
			        if(dx <= dy) {
			            error = 2 * dx;
			            int y = y1;
			            for(int x = x1; x > x2; x--) 
			            {
			            	sleepThread(x, y);
			            	if (penDown) setPixel(x,y, Colour, raster);
			                error = error - 2 * dy;
			                if (error >= 0) {
			                    y--;
			                    error = error + 2 * dx;
			                }
			            }
			        }
			        /** 6th octant */
			        else {
			            error = 2 * dy;
			            int x = x1;
			            for(int y = y1; y > y2; y--) 
			            {
			            	sleepThread(x, y);
			            	if (penDown) setPixel(x,y, Colour, raster);
			                error = error - 2 * dx;
			                if (error >= 0) 
			                {
			                    x--;
			                    error = error + 2 * dy ;
			                }
			            }
			        }
			    }
			    /* 4th quarter */
			    else if(dx >= 0 && dy <= 0) {
			        /** 7th octant */
			        if(dx < -dy) 
			        {
			            error = 2 * dy;
			            int x = x1;
			            for(int y = y1; y > y2; y--) 
			            {
			            	sleepThread(x, y);
			            	if (penDown) setPixel(x,y, Colour, raster);
			                error = error + 2 * dx;
			                if (error >= 0) {
			                    x++;
			                    error = error + 2 * dy ;
			                }
			            }
			        }
			        /** 8th octant */
			        else {
			            error = -dx;
			            int y = y1;
			            for(int x = x1; x < x2; x++) 
			            {
			            	sleepThread(x, y);
			            	if (penDown) setPixel(x,y, Colour, raster);
			                error = error - 2 * dy;
			                if (error >= 0) {
			                    y--;
			                    error = error - 2 * dx;
			                }
			            }
			        }
			    }
			    repaint();
			}//run
		};//thread
		t.start();
		while(t.isAlive());
	}

	/**
	 * sets a pixel and adjacent pixels, according to the global penSize in the passed raster of the passed colour
	 * @param x position of pixel
	 * @param y position of pixel
	 * @param colour palate number
	 * @param raster raster of pixels to set
	 * 
	 */
	public void setPixel(int x, int y, int colour, WritableRaster raster) 
	{
		if(x>=panelWidth)  
			x=0;
		if (y>=panelHeight)
			y=0;
		if (penDown) drawLine(PenColour, x, y, x + penSize, y + penSize);

	}
	
	/**
	 * Cycle the full colour palette
	 */
	public void cycleColours()
	{
		int lastCol = colors[15];
		for(int i=15; i>1; i--)
		{
			colors[i] = colors[i-1];
			
		}
		colors[1] = lastCol;
		repaint();
	}
	
	/**
	 * unimplemented, it needs updating to work with an rgb bitmap
	 * @param picture raster map of pixels
	 * @param colorToReplace colour of background to fill
	 * @param colorToPaint colour to replace background with
	 * @param x x position
	 * @param y y position
	 * 
	 */
	 public void floodfill(WritableRaster picture, int colorToReplace, int colorToPaint, int x, int y) 
	 {
		   throw  new java.lang.UnsupportedOperationException("not yet implemented flood fill");
	 }

		  private void validatePicture(WritableRaster picture) 
		  {
		    if (picture == null) {
		      throw new IllegalArgumentException("You can't pass a null instance as picture");
		    }
		  }

		  /**
		   * Method created to avoid IndexOutOfBoundExceptions. This method return -1 if you try to access
		   * an invalid position.
		   */
		  private static int getValueAt(int[][] picture, int x, int y) 
		  {
		    if (x < 0 || y < 0 || x > picture.length || y > picture[x].length) 
		    {
		      return -1;
		    } else 
		    {
		      return picture[x][y];
		    }
		  }
	/*=========end flood fill========*/
	
	/**
	 * sleep the thread and update the turtle xPos and yPos (if positive) and repaint the display
	 * @param x
	 * @param y
	 */
	private void sleepThread(int x, int y)
	{
		
		try {
			Thread.sleep(sleepPeriod);
		} catch (InterruptedException e) {}
		if (xPos >-1 && yPos >-1)
		{
			xPos = x;
			yPos = y;
			// if (speed++ % turtleSpeed == 0)
				   repaint();
		}
	}
	/**
	 * Constructor.
	 * Create a panel with pen set to the middle and turtle pointing down the screen
	 * The pen is up.
	 *
	 */
	
	public LBUGraphics()
	{
	
		image = new BufferedImage(panelWidth, panelHeight, BufferedImage.TYPE_INT_RGB);//image = new BufferedImage(panelWidth, panelHeight, BufferedImage.TYPE_BYTE_INDEXED, colourModel);
		raster = image.getRaster();
		
		
		//set default turtle state
				xPos = panelWidth/2;
				yPos = panelHeight/2;
				penDown = false;
				direction = 90; //right

				setPreferredSize(new Dimension(panelWidth, panelHeight));
				setLayout(new FlowLayout());
							
				commandLine = new JTextField(25);
				
				
				add(commandLine);
				commandLine.setVisible(true);
				commandLine.addActionListener(this);
				okBut = new JButton("ok");
				add(okBut);
				okBut.setVisible(true);
				okBut.addActionListener(this);
				messages = new JLabel("LBUGraphics V"+VERSION);
				messages.setBackground(Color.white);
				messages.setForeground(Color.red);
				
				add(messages);
				messages.setVisible(true);
				//main drawing area
				
				//small image to display on top of drawing area to represent the turtle
				
				try {
					turtle0 = ImageIO.read(LBUGraphics.class.getResource("turtle90.png"));
				
				} catch (IOException e) 
				{
						e.printStackTrace();
				}	
				
				// Set max size of the panel, so that is matches the max size of the image.
				setMaximumSize(new Dimension(image.getWidth(), image.getHeight()));
				setSize(panelWidth, panelHeight);
				setVisible(true);
				revalidate();
				clear();
				repaint();
			
	}
	
	
	/**
	 * implemented abstract method from ActionListener interface.
	 * Reads text from commandLine JTextField and calls abstract method processCommand()
	 * passing the text in the jTextField to it. The jTextField is then cleared.
	 * Derived classes must provide a definition of void processCommand(String command)
	 * runs in a thread so animations are visible
	 */
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		Thread t = new Thread(new Runnable()
		{
			public void run()
			{
				processCommand(commandLine.getText());
				commandLine.setText("");
				
			}
		}
		);
		t.start();
		
		
	}
		
}
