import java.awt.FlowLayout;

import javax.swing.JFrame;

import uk.ac.leedsbeckett.oop.LBUGraphics;

public class MainClass extends LBUGraphics{

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
		setStroke(1,false);
		//circle(150);
		about();
		penDown();
		
		
	}
	
	@Override
	public void processCommand(String command)
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
			fill();
		if (command.equalsIgnoreCase("set"))
		{
			
			int degrees = Integer.parseInt(params[1]);
			pointTurtle(degrees);
			
			
		}
		System.out.println(direction);
	}

}
