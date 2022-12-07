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
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * LBUGraphics
 * extended JPanel with simple drawing commands and a visual representation of a turtle to perform "turtle graphics" drawing operations.
 * the jar file should be added to your build path.
 * right click on your project, select "Build Path-Add External Archive" and add jar file.
 * It will appear in your project explorer under "referenced libraries", inside the jar will be LBUGraphics.class
 * Don't forget to look at the inherited methods from JPanel and above, which will also be if use.
 * 
 * @author Duncan Mullier
 * @version 3.1 
 * All software has bugs, if you find one please report to author. Ensure you have the latest version
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
	    MainFrame.setLayout(new FlowLayout()); 	//not strictly necessary
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
@since 1/2022
 */
@SuppressWarnings("serial")

public abstract class LBUGraphics extends JPanel implements ActionListener, CommandLineInterface
{

	/**
	 * public version number.
	 */
	public final float VERSION = 4.0f; 
	private  Color background_Col = Color.DARK_GRAY;
	private final static int TURTLE_X_SIZE = 72, TURTLE_Y_SIZE = 69;
	private final int TURTLESTARTX = 1000, TURTLESTARTY = 400;
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
	
	/**
	 * Colour of the pen the turtle draws with (A Java Color)
	 */
	protected Color PenColour = Color.RED;
	
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
	protected int sleepPeriod=10; //delay for turtle animation

	/**
	 * must be implemented in your class so that TurtleGraohics can call your code when something happens at the LBUGraphics GUI (i.e. user presses return in text field or clicks ok button).
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
	 * @return graphics context
	 */
	public Graphics getGraphicsContext()
	{
		return  image.getGraphics();
	}
	
	/**
	 * returns the graphicsContext of the Turtle display so you can draw on it using the extended Graphics2Dl Java drawing methods
	 * @return graphics context
	 */
	public Graphics2D getGraphics2DContext()
	{
		Graphics2D g2 = (Graphics2D) getGraphicsContext();
		return  g2;
	}
	
	/**
	 * return a BufferedImage of the display, so that it can be saved
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
	 * @return Color pencolour
	 */
	public Color getPenColour()
	{
		return PenColour;
	}
	
	/**
	 * sets the width of the line drawn
	 * @param strokeWidth integer representing the thickness of the line
	 * @param dashed if true line will be dashed, solid if false
	 */
	public void setStroke(int strokeWidth, boolean dashed)
	{
		if(dashed)
		{
		 float[] fa = {10, 10, 10};       // The dash pattern
		 Stroke = new BasicStroke(strokeWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10, fa, 10);
		}
		else
		{
			Stroke = new BasicStroke(strokeWidth);
		}
		this.StrokeWidth = strokeWidth;
		
		Graphics2D g2 = (Graphics2D) image.getGraphics();
		g2.setStroke(Stroke);
		
	}
	
	/**
	 * setPenColour sets the colour that the turtle will draw in
	 * @param col Java Color
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
		x2+=10;
		y2+=10;
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
		setStroke(10,true);
		Thread t = new Thread() 
		{
			public void run() 
			{
				int dx = 50;
				xPos=yPos=250;
				Graphics g = image.getGraphics();
				Color savePen = PenColour; //save drawing pen
				boolean savePendown = penDown;
				penDown();
				drawCircle(200);
				/*
				for(int i=0; i<10; i++)
				{
					forward(50+(i*2));
					turnRight();
					forward(50+(i*2));
					turnRight();
					forward(50+(i*2));
					turnRight();
					forward(50+(i*2));
					forward(50+(i*2));
					turnRight(45);
					forward(50+(i*2));
					turnRight(45);
					forward(50+(i*2));
					turnRight(45);
					forward(50+(i*2));
					forward(50+(i*2));
					turnRight(45);
					forward(50+(i*2));
					turnRight(45);
					forward(50+(i*2));
					turnRight(45);
					forward(50+(i*2));
					penUp();
					forward(5);
					turnLeft();
					forward(5);
					turnRight();
					turnRight();
					penDown();
					if (i%2 == 0)
						PenColour = Color.YELLOW;
					else
						PenColour = Color.RED;
					drawCircle();
				}
*/
				PenColour = Color.GREEN;
				
				g.drawString("LBUGraphics Version "+VERSION,250,250);
				/*penUp();
				forward(100);
				for(int i = 0; i<360; i++)
					turnRight(10);*/
				penDown = savePendown;
				PenColour = savePen; //restore pen
				
				repaint();
			   
			}
		};
		t.start();
		while(t.isAlive()); //wait until drawing finished.
		setStroke(1,false);
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
		direction +=90;
		if (direction >= 360)
			direction = 0;
		repaint();
	}
	
	/**
	 * turtle is rotated to the right by amount degrees. i.e. it will rotate right by amount degrees  	
	 * The turtle will wrap around if it goes beyond 360
	 * @param amount is an integer 
	 */
	public void turnRight(int amount)
	{
		amount = amount%360;
		direction = direction + amount;
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
		direction -=90;
		if (direction < 0)
			direction = 270;
		repaint();
	}
	
	/**
	 * turtle is rotated 90 degrees to the right by amount degrees. i.e. it will rotate right by amount degrees  	
	 * The turtle will wrap around if it goes beyond 360
	 * @param amount is an integer
	 */
	public void turnLeft(int amount)
	{
		direction -= amount%360;
		repaint();
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
		
		 if (penDown)
			{
				drawLine(PenColour, xPos, yPos, (int)x2,(int) y2);
			}
			//now robot has moved to the new position
		xPos = (int)x2;
		yPos = (int)y2;
			
		try {
				Thread.sleep(sleepPeriod);
		} catch (InterruptedException e)
		{
				// TODO Auto-generated catch block
				System.out.println("exception ****** "+e);
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
		
		repaint();

		
	}
	
	
	/**
	 * unimplemented circle command
	 * @param radius radius of the circle to draw
	 * 
	 */
	public void circle(int radius)
	{
		
		System.out.println("I can't draw a circle of "+radius+" because the code hasn't been written");
	}
	
//	@Override
	/**
	 * overridden paintComponent method to handle image updating (do not call directly, use repaint();)
	 */
	public void paintComponent(Graphics g) 
	{
		setTurtleGraphicDirection();
		// render the image on the panel.
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
		image = new BufferedImage(panelWidth, panelHeight, BufferedImage.TYPE_INT_RGB);
		setPreferredSize(new Dimension(panelWidth, panelHeight));
		clear();
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
		//System.out.println("xpos,ypos="+panelWidth+","+panelHeight);
		//System.out.println("xpos,ypos="+xPos+","+yPos);
		//set default turtle state
		xPos = TURTLESTARTX/2;
		yPos =  TURTLESTARTY/2;
		penDown = false;
		direction = 180; //down
		repaint();
		setStroke(1,false);
		
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
	final int quadrants = 8;
	ArrayList<ArrayList<Integer>> xcoords = new ArrayList<>(quadrants);
	ArrayList<ArrayList<Integer>> ycoords = new ArrayList<>(quadrants);
	final int QUADSIZE = 10000;
	int[][] xs = new int[8][QUADSIZE];
	int[][] ys = new int[8][QUADSIZE];
	int up = 0;
	int down = 0;//QUADSIZE;
	public void drawCircle(int radius)
	{
		//initialise multi-dimensional arraylists
		for(int i=0; i < quadrants; i++) {
		    xcoords.add(new ArrayList<Integer>());
		    ycoords.add(new ArrayList<Integer>());
		}
		setTurtleSpeed(500);
		Thread t = new Thread() 
		{
			public void run() 
			{
				int count = 0;
				int gd=0, gm; //,h,k,r;  
				double x,y,x2;  
				int h=xPos, k=yPos, r=radius;  
				x=0;
				y=r;  
				x2 = r/Math.sqrt(2);  
				while(x<=x2)  
				{  
					y = Math.sqrt(r*r - x*x);  
					setPixel((int)Math.floor(x), (int)Math.floor(y), h,k);  
					x += 1;  
					count++;
				} 
				//calculated 8 quads now plot them in order
				PenColour = Color.red;
				for(int i=0; i<8; i++)
				{
					int start = 0;
					int end = count-1;
					int step = 1;
					if(i % 2 == 1 )
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
						
						//drawLine(PenColour, xs[i][j], ys[i][j],  xs[i][j]+10, ys[i][j]+10); 
						drawLine(PenColour, xcoords.get(i).get(j), ycoords.get(i).get(j), xcoords.get(i).get(j)+10, ycoords.get(i).get(j)+10);//[i][j], ys[i][j],  xs[i][j]+10, ys[i][j]+10); 
					   try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} //wait until drawing finished.
					   xPos = xs[i][j];
					   yPos = ys[i][j];
					    repaint();
					}
				}
			}
		};
		t.start();
		while(t.isAlive());
		int x=0;	
		
	}
	
	void setPixel(int x, int y, int h, int k)  
	{  
		
		//up 3
		xs[3][up] = x+h;
		ys[3][up] = y+k;
	    xcoords.get(3).add(x+h);
	    ycoords.get(3).add(y+k);
		//drawLine(PenColour, x+h, y+k, x+h, y+k);
	    
	    PenColour = Color.green;  //down 0
	    xs[0][down] = x+h;
		ys[0][down] = -y+k;
		xcoords.get(0).add(x+h);
		ycoords.get(0).add(-y+k);
	    //drawLine(PenColour, x+h, -y+k, x+h, -y+k); 
	    
	    PenColour = Color.blue; //down 7
	    xs[7][down] = -x+h;
		ys[7][down] = -y+k;
		xcoords.get(7).add(-x+h);
		ycoords.get(7).add(-y+k);
	    //drawLine(PenColour, -x+h, -y+k, -x+h, -y+k);  
	    
	    PenColour = Color.cyan; //up 4
	    xs[4][up] = -x+h;
		ys[4][up] = y+k;
		xcoords.get(4).add(-x+h);
		ycoords.get(4).add(y+k);

	    //drawLine(PenColour, -x+h, y+k, -x+h, y+k);  
	    
	    PenColour = Color.white; //down 2
	    xs[2][down] = y+h;
		ys[2][down] = x+k;
		xcoords.get(2).add(y+h);
		ycoords.get(2).add(x+k);

	    //drawLine(PenColour, y+h, x+k, y+h, x+k);  
	    
	    PenColour = Color.pink; //up 1
	    xs[1][up] = y+h;
		ys[1][up] = -x+k;
		xcoords.get(1).add(y+h);
		ycoords.get(1).add(-x+k);

	    //drawLine(PenColour, y+h, -x+k, y+h, -x+k);  
	    
	    PenColour = Color.yellow; //up 6
	    xs[6][up] = -y+h;
		ys[6][up] = -x+k;
		xcoords.get(6).add(-y+h);
		ycoords.get(6).add(-x+k);

	    //drawLine(PenColour, -y+h, -x+k, -y+h, -x+k);  
	    
	    PenColour = Color.orange; //down 5
	    xs[5][down] = -y+h;
		ys[5][down] = x+k;
		xcoords.get(5).add(-y+h);
		ycoords.get(5).add(x+k);

		up++;
		down++;
	    //drawLine(PenColour, -y+h, x+k, -y+h, x+k);  
	 
	}  
	//save quad x,y data for later drawing in sequence
	//some quads go forwards, some backwards, store in the same order for sequential drawing

	/*int[][] quad = new int[8][QUADSIZE];
	int[] q2 = new int[QUADSIZE];
	int[] q3 = new int[QUADSIZE];
	int[] q4 = new int[QUADSIZE];
	int[] q5 = new int[QUADSIZE];
	int[] q6 = new int[QUADSIZE];
	int[] q7 = new int[QUADSIZE];
	int[] q8 = new int[QUADSIZE];
	private void saveQuad(int quad, boolean direction, int x, int y)
	{
		int inc=1; //incrementor for array
		
	}*/
	/**
	 * Constructor.
	 * Create a panel with pen set to the middle and turtle pointing down the screen
	 * The pen is up.
	 *
	 */
	
	public LBUGraphics()
	{
		//set default turtle state
				xPos = panelWidth/2;
				yPos = panelHeight/2;
				penDown = false;
				direction = 180; //down

				//JPanel panel = new JPanel();
				
				setPreferredSize(new Dimension(panelWidth, panelHeight));
				setLayout(new FlowLayout());
				//setLayout(new GridLayout());
				
				commandLine = new JTextField(25);
				
				//panel.add(commandLine);
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
				//messages.setText("hello");
				add(messages);
				messages.setVisible(true);
				//main drawing area
				image = new BufferedImage(panelWidth, panelHeight, BufferedImage.TYPE_INT_RGB);
				
				//small image to display on top of drawing area to represent the turtle
				
				try {
					turtle0 = ImageIO.read(LBUGraphics.class.getResource("turtle90.png"));
				
				} catch (IOException e) 
				{
					//throw new IOException("turtle images not found, you must have turtle0/90/180/270.png in project directory");
					e.printStackTrace();
				}	
				
				//setTurtleSpeed(50);
				// Set max size of the panel, so that is matches the max size of the image.
				setMaximumSize(new Dimension(image.getWidth(), image.getHeight()));
				setSize(panelWidth, panelHeight);
				setVisible(true);
				revalidate();
				//clear();
			
	}


	/**
	 * implemented abstract method from ActionListener interface.
	 * Reads text from commandLine JTextField and calls abstract method processCommand()
	 * passing the text in the jTextField to it. The jTextField is then cleared.
	 * Derived classes must provide a definition of void processCommand(String command)
	 */
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		processCommand(commandLine.getText());
		commandLine.setText("");
		
	}
	
	
	
		
		
}
