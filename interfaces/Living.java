package interfaces;

public interface Living extends Drawable, Moveable {

	boolean isLiving();
	void kill();
	byte getIType();//this is needed only for the invaders, dont know how to use the arraylist without placing the method in here.
	
}
