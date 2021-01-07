package uk.ac.leedsbeckett.oop;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import uk.ac.leedsbeckett.oop.TurtleGraphics;

public class MainClass implements ActionListener
{
private TurtleGraphics gp;
JButton but1,but2, but3;
public MainClass()
{
	gp = new TurtleGraphics();
	
    JFrame MainFrame = new JFrame();
    MainFrame.setLayout(new FlowLayout());
    MainFrame.add(gp);
     but1 = new JButton("RotateR");
     but3 = new JButton("RotateL");
    but2 = new JButton("Move");
    MainFrame.add(but1);
    MainFrame.add(but2);
    MainFrame.add(but3);
   // ButtonListener bl = new ButtonListener();
    but1.addActionListener(this); //djm tell the listener to check this
    but2.addActionListener(this); //djm tell the listener to check this
    but3.addActionListener(this); //djm tell the listener to check this
    
    MainFrame.setSize(640, 480);
    MainFrame.setVisible(true);;
    //gp.about();
    gp.penDown();
   // gp.setTurtleImage("c:\\temp\\turtle.png");
  /*  gp.forward(100);
    gp.turnRight();
    gp.forward(100);
    gp.turnRight();
    gp.forward(100);
    gp.turnRight();
    gp.forward(100);
    gp.turnRight(); 
    gp.setTurtleSpeed(500);
    gp.forward(100);
    gp.turnRight(45);
    gp.forward(100);
    gp.turnRight(45);
    gp.forward(100);
    gp.turnRight(45);
    gp.forward(100);
    gp.turnRight(45);
    gp.forward(100);
    gp.turnRight(45);
    gp.forward(100);
    gp.turnRight(45);
    gp.forward(100);
    gp.turnRight(45);
    gp.forward(100);
    gp.turnRight(45);
    gp.forward(100);


    gp.turnLeft(45);
    gp.forward(100);
    gp.turnLeft(45);
    gp.forward(100);
    gp.turnLeft(45);
    gp.forward(100);
    gp.turnLeft(45);
    gp.forward(100);
    gp.turnLeft(45);
    gp.forward(100);
    gp.turnLeft(45);
    gp.forward(100);
    gp.turnLeft(45);
    gp.forward(100);
    gp.turnLeft(45);
    gp.forward(100);
    
    gp.setTurtleSpeed(1000);

    gp.turnRight(10);
    gp.turnRight(10);
    gp.turnRight(10);
    gp.turnRight(10);
    gp.turnRight(10);
    gp.turnRight(10);
    gp.turnRight(10);
    gp.turnRight(10);
    gp.turnRight(10);
    gp.turnRight(10);
    gp.turnRight(10);
    
    */
    System.out.println("x = "+gp.getxPos()+" y = "+gp.getyPos());
   

}
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		new MainClass();

	}
	
	 public void actionPerformed(ActionEvent event)
     {
   	  if (event.getSource() == but1)
   		  gp.turnRight(22);
   	  else if (event.getSource()==but2)
   		  	gp.forward(50);
   	  else if (event.getSource()==but3)
   		  	gp.turnLeft(22);
  
   	  System.out.println("hello");
     }
	  /* private class ButtonListener implements ActionListener
	   {
	      //--------------------------------------------------------------
	      //  Updates the counter and label when the button is pushed.
	      //--------------------------------------------------------------
	     
	   }*/


}