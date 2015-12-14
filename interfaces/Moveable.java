package interfaces;

public interface Moveable extends Drawable {

	int getX();
	int getY();
	
	void move(int xOff, int yOff);
	
	void moveX(int off);
	
}
