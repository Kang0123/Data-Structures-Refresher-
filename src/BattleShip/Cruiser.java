package BattleShip;

public class Cruiser extends Ship{

	public Cruiser(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public char drawShipStatusAtCell(boolean isDamaged) {
		// TODO Auto-generated method stub
		if (isDamaged) {
			return 'c';
		} else {
			return 'C';
		}
	}

	@Override
	public int getLength() {
		// TODO Auto-generated method stub
		return 3;
	}
	
}

//I'm a cruiser and I should inherit a ship...