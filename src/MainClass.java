import java.awt.FlowLayout;

import javax.swing.JFrame;

import uk.ac.leedsbeckett.oop.LBUGraphics;

public class MainClass extends LBUGraphics{

	public static void main(String[] args) {
		MainClass o = new MainClass();
		
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
		about();
		/*setStroke(1,true);
		penDown();
		forward(200);
		turnRight(90);
		forward(200);
		turnRight(90);
		forward(200);
		turnRight(90);
		forward(200);
		turnRight(90);*/
		setStroke(1,false);
		forward(100);
		penDown();
		drawCircle(50,1);
		
		
	}
	@Override
	public void processCommand(String command) {
		// TODO Auto-generated method stub
		
	}

}
