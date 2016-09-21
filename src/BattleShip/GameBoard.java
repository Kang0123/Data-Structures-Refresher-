package BattleShip;
import java.util.ArrayList;

public class GameBoard
{
	int rowCount = 10;
	int colCount = 10;
	
	final String LINE_END = System.getProperty("line.separator"); 
	
	ArrayList< ArrayList< Cell > > cells = new ArrayList <ArrayList<Cell> >();
	ArrayList< Ship > myShips = new ArrayList<Ship>();
	
	public GameBoard( int rowCount, int colCount )
	{
		this.rowCount = rowCount;
		this.colCount = colCount;
		
		//create the 2D array of cells
		for (int col = 0; col < this.colCount; col++) {
			cells.add(new ArrayList<Cell>());
			for (int row = 0; row < this.rowCount; row++) {
				cells.get(col).add(new Cell());
			}
		}
	}
	
	public String draw()
	{
		StringBuilder board = new StringBuilder();
		
		for (int i = 0; i < this.colCount + 2; i++) {
			board.append('*');
		}
		board.append('\n');
		
		for (int row = 0; row < this.colCount; row++) {
			board.append('*');
			for (int col = 0; col < this.rowCount; col++) {
				board.append(cells.get(row).get(col).draw());
			}
			board.append("*\n");
		}
		
		for (int i = 0; i < this.rowCount + 2; i++) {
			board.append('*');
		}
			
		return board.toString();

		//draw the entire board... I'd use a StringBuilder object to improve speed
		//remember - you must draw one entire row at a time, and don't forget the
		//pretty border...
	}
	
	//add in a ship if it fully 1) fits on the board and 2) doesn't collide w/
	//an existing ship.
	//Returns true on successful addition; false, otherwise
	public boolean addShip( Ship s , Position sternLocation, HEADING bowDirection )
	{		
		if (sternLocation.x < 0 || sternLocation.x > this.rowCount - 1 || sternLocation.y < 0 || sternLocation.y > this.colCount - 1) {
			System.out.println("Out of bounds! Try again.");
			return false;
		}

		switch (bowDirection) {
			case N: if (sternLocation.y - s.getLength() < 0) {
							System.out.println("Out of bounds! Try again.");
							return false;
						}
						if (checkVertShips(s.getLength(), sternLocation, -1)) {
							return drawVertShip(s, sternLocation, -1);
						}
						break;
			case S:	if (sternLocation.y + s.getLength() > this.rowCount) {
							System.out.println("Out of bounds! Try again.");
							return false;		
						}
						if (checkVertShips(s.getLength(), sternLocation, 1)) {
							return drawVertShip(s, sternLocation, 1);
						}
						break;
			case E: 	if (sternLocation.x + s.getLength() > this.colCount) {
							System.out.println("Out of bounds! Try again.");
							return false;
						}
						if (checkHoriShips(s.getLength(), sternLocation, 1)) {
							return drawHoriShip(s, sternLocation, 1);
						}
						break;
			case W:	if (sternLocation.x - s.getLength() < 0) {
							System.out.println("Out of bounds! Try again.");
							return false;
						}
						if (checkHoriShips(s.getLength(), sternLocation, -1)) {
							return drawHoriShip(s, sternLocation, -1);
						}
						break;
			default: return false;
		} 
		return false;
	}
	
	
	public boolean drawHoriShip (Ship s, Position location, int heading) {
		myShips.add(s);
		for (int i = 0; i < s.getLength(); i++) {
			cells.get(location.y).get(location.x + (i * heading)).setShip(s);
		}
		return true;
	}
	
	
	public boolean drawVertShip (Ship s, Position location, int heading) {
		myShips.add(s);
		for (int i = 0; i < s.getLength(); i++) {
			cells.get(location.y + (i * heading)).get(location.x).setShip(s);
		}
		return true;
	}
	

	// Check for ship collisions. False = collision, true = clear
	public boolean checkHoriShips (int length, Position location, int heading) {		
		for (int i = 0; i < length; i++) {
			if (cells.get(location.y).get(location.x + (i * heading)).getShip() != null) {
				System.out.println("Collides with " + cells.get(location.y).get(location.x + (i * heading)).getShip().getName() + "!");
				return false;
			}
		}
		return true;
	}
	
	public boolean checkVertShips (int length, Position location, int heading) {		
		for (int i = 0; i < length; i++) {
			if (cells.get(location.y + (i * heading)).get(location.x).getShip() != null) {
				System.out.println("Collides with " + cells.get(location.y + (i * heading)).get(location.x).getShip().getName() + "!");
				return false;
			}
		}
		return true;
	}
	
	//Returns A reference to a ship, if that ship was struck by a missle.
	//The returned ship can then be used to print the name of the ship which
	//was hit to the player who hit it.
	//Ensure you handle missiles that may fly off the grid
	public Ship fireMissle( Position coordinate )
	{
		if (coordinate.x < 0 || coordinate.x > this.colCount - 1 || coordinate.y < 0 || coordinate.y > this.rowCount - 1) {
			System.out.println("Fired out of bounds... Try again?");
		} else if (cells.get(coordinate.y).get(coordinate.x).getShip() != null) {
			cells.get(coordinate.y).get(coordinate.x).hasBeenStruckByMissile(true);
			System.out.println(cells.get(coordinate.y).get(coordinate.x).getShip().getName() + " was hit!");
			return cells.get(coordinate.y).get(coordinate.x).getShip();
		} else {
			System.out.println("Boo! You missed...");
		}
		return null;
	}
	
	//Here's a simple driver that should work without touching any of the code below this point
	public static void main( String [] args )
	{
		System.out.println( "Hello World" );
		GameBoard b = new GameBoard(10, 10);	
		System.out.println( b.draw() );
		
		Ship s = new Cruiser( "Cruiser" );
		if( b.addShip(s, new Position(3,6), HEADING.W ) )
			System.out.println( "Added " + s.getName() + " Location is ");
		else
			System.out.println( "Failed to add " + s.getName() );
		
		s = new Destroyer( "Vader" );
		if( b.addShip(s, new Position(3,5), HEADING.N ) )
			System.out.println( "Added " + s.getName() + " Location is " );
		else
			System.out.println( "Failed to add " + s.getName() );
		
		System.out.println( b.draw() );

//		b.fireMissle( new Position(3,5) );
//		System.out.println( b.draw() );
//		b.fireMissle( new Position(3,4) );
//		System.out.println( b.draw() );
//		b.fireMissle( new Position(3,3) );
//		System.out.println( b.draw() );
//		
//		b.fireMissle( new Position(0,6) );
//		b.fireMissle( new Position(1,6) );
//		b.fireMissle( new Position(2,6) );
//		b.fireMissle( new Position(3,6) );
//		System.out.println( b.draw() );
//		
//		b.fireMissle( new Position(6,6) );
//		System.out.println( b.draw() );
	}

}
