package uk.ac.leedsbeckett.oop;

import java.awt.event.ActionEvent;

public class MyTurtle extends TurtleGraphics
{

	@Override
	public void processCommand(String command)
	{

		command=command.toLowerCase();
		if(command.equals("left"))
			this.turnLeft();
		else if(command.equals("right"))
			this.turnRight();
		else if(command.equals("forward"))
			this.forward(100);
		else if(command.equals("hide"))
			this.setGUIVisible(false);
		else if(command.equals("show"))
				this.setGUIVisible(true);
		else 
			 this.displayMessage("Unknown Command");
		
	}

	
	
}
