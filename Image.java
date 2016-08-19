import static org.lwjgl.opengl.GL11.*;

public class Image extends Drawable {
  public Point point = new Point();

  public void draw(){
    this.draw(point.x, point.y);
  }
}
