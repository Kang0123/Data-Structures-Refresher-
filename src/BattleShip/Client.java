package BattleShip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class Client
{
	final String NEWL = System.getProperty("line.separator");
	
	private String name = "Player";
	PrintWriter out = null;
	BufferedReader in = null;
	GameManager man = null;
	GameBoard board = new GameBoard(10,10);
	GameBoard targets = new GameBoard(10,10);
	
	Client( BufferedReader in, PrintWriter out, GameManager manager )
	{
		this.in = in;
		this.out = out;
		this.man = manager;
	}
	
	public void playGame() throws IOException
	{
		this.out.println( NEWL + NEWL + "   Missiles Away! Game has begun" );
		this.out.println( "   To Launch a missle at your enemy:" );
		this.out.println( "F 2 4" );
		this.out.println( "Fires a missile at coordinate x=2, y=4." );
		
		while( !allMyShipsAreDestroyed() && !allEnemyShipsAreDestroyed()) // put Code Here to process in game commands, after each command, print the target board and game board w/ updated state )
		{
			out.println( "------------------------" );
			out.println( "Target Board: " + this.targets.draw() );
			out.println( "Your Ships: " + this.board.draw() );
			out.println( "   Waiting for Next Command...\n\n" );
			out.flush();
			
			//Perform test here to see if we have run or lost
			processCommand();
			if (allMyShipsAreDestroyed()) {
				out.println("You have sunk. You LOSE!");
				out.flush();
				board.draw();
			}
			if (allEnemyShipsAreDestroyed()) {
				out.println("You have sunk the enemy. You WIN!");
				out.flush();
				board.draw();
			}
		}
	}
	
	//Returns a bool, true iff all of this client's ships are destroyed
	boolean allMyShipsAreDestroyed()
	{
		for (int i = 0; i < board.myShips.size(); i++) {
			if (board.myShips.get(i).isAlive()) {
				return false;
			}
		}
		return true;
	}

	//Returns a bool, true iff all of the opponent's ships are destroyed
	boolean allEnemyShipsAreDestroyed()
	{
		for (int i = 0; i < targets.myShips.size(); i++) {
			if (targets.myShips.get(i).isAlive()) {
				return false;
			}
		}
		return true;
	}

	//"F 2 4" = Fire command
	//"C Hello world, i am a chat message"
	//"D" - Redraw the latest game and target boards
	boolean processCommand() throws IOException
	{
		String[] commands = in.readLine().split(" ");
		if (commands[0].equalsIgnoreCase("F") && isNum(commands[1]) && isNum(commands[2])) {
			processFireCmd(new String[] {commands[1], commands[2]});
		} else if (commands[0].equalsIgnoreCase("C")) {
			processChatCmd (Arrays.copyOfRange(commands, 1, commands.length).toString());
		} else if (commands[0].equalsIgnoreCase("D")) {
			out.println("My board: \n");
			board.draw();
			out.println("Target's board: \n");
			targets.draw();
			out.flush();
		} else {
			out.println("Unknown command.");
			out.flush();
			return false;
		}
		return true;
	}
	
	boolean isNum (String s) {
		try {
			Integer num = Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	//When a fire command is typed, this method parses the coordinates and launches a missle at the enemy
	boolean processFireCmd( String [] s )
	{
		Ship ship = targets.fireMissle(new Position(Integer.parseInt(s[0]), Integer.parseInt(s[1])));
		
		if (ship == null) {
			out.println("You missed!");
			out.flush();
			return false;
		} else {
			out.println(ship.getName() + " was hit!");
			out.flush();
			return true;
		}
	}
	
	//Send a message to the opponent
	boolean processChatCmd( String s )
	{
		
		man.getOpponent(this).out.println(s);
		man.getOpponent(this).out.flush();
		out.println("Message sent.");
		out.flush();
		return true;
	}
	
	GameBoard getGameBoard() { return this.board; }
	
	public void initPlayer() throws IOException
	{
		//1.Get player name
		//2.Print out instructions
		
		out.println("Enter your name: ");
		out.flush();
		this.name = in.readLine();
		
//Here's some nice instructions to show a client		
		out.println("   You will now place 2 ships. You may choose between either a Cruiser (C) " );
		out.println("   and Destroyer (D)...");
		out.println("   Enter Ship info. An example input looks like:");
		out.println("\nD 2 4 S USS MyBoat\n");
		out.println("   The above line creates a Destroyer with the stern located at x=2 (col)," );
		out.println("   y=4 (row) and the front of the ship will point to the SOUTH (valid" );
		out.println("   headings are N, E, S, and W.\n\n" );
		out.println("   the name of the ship will be \"USS MyBoat\"");
		out.println("Enter Ship 1 information:" );
		out.flush();
		
		//Get ship locations from the player for all 2 ships (or more than 2 if you're using more ships)
		String[] ships;
		do {
			ships = in.readLine().split(" ");
			if (ships[0].equalsIgnoreCase("C") && ships.length > 3) {
				boolean yes = board.addShip(new Cruiser(Arrays.copyOfRange(ships, 4, ships.length).toString()), new Position(Integer.parseInt(ships[1]), Integer.parseInt(ships[2])), HEADING.valueOf((ships[3])));
				out.println("Added a cruiser.");
			} else if (ships[0].equalsIgnoreCase("D") && ships.length > 3) {
				board.addShip(new Destroyer(Arrays.copyOfRange(ships, 4, ships.length).toString()), new Position(Integer.parseInt(ships[1]), Integer.parseInt(ships[2])), HEADING.valueOf((ships[3]).toUpperCase()));
				out.println("Added a destroyer.");
			} else if (ships[0].equalsIgnoreCase("pic")) {
				String pic = board.draw();
				pic.replaceAll("\\n", NEWL);
				out.println(pic);
				System.out.println(pic);
				out.flush();
			}
			
			else {
				out.println("Wrong ship placement. Try again.");
			}
			out.flush();
		} while (board.myShips.size() < 2);
		
		
		//After all game state is input, draw the game board to the client
		
		
		System.out.println( "Waiting for other player to finish their setup, then war will ensue!" );
	}
	
	String getName() { return this.name; }
	
	public static void main( String [] args )
	{
		
		
	}
}
