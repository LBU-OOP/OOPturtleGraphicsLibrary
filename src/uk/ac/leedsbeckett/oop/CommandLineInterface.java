package uk.ac.leedsbeckett.oop;

/**
 * interface to ensure classes have a processCommand(String) method
 * 
 */
public interface CommandLineInterface
{
	/**
	 * abstract method that must be in your class so LBUGraphics can call it when the user interacts with the interface
	 * @param command is the text types into the textfield
	 */
	public void processCommand(String command);
}
