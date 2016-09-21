package BattleShip;

public class Destroyer extends Ship {

	public Destroyer(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public char drawShipStatusAtCell(boolean isDamaged) {
		// TODO Auto-generated method stub
		if (isDamaged) {
			return 'd';
		} else {
			return 'D';
		}
	}

	@Override
	public int getLength() {
		// TODO Auto-generated method stub
		return 2;
	}
	
}

//I'm a destroyer and I should inherit a ship...