
public class Test {
	public void main() {
		System.out.println("Hello, world!");
	}
    public Rectangle createRectangle(Point x, Point y) {
        width = Math.abs(y.getX() - x.getX());
        height = Math.abs(y.getY() - x.getY());
        return new Rectangle(x, width, height); 
}
