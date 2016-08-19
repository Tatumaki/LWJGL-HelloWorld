import static org.lwjgl.glfw.GLFW.*;

public class Entity extends Drawable implements Controllable {
  public Texture texture = new Texture();
  public Point   point   = new Point();

  @Override
  public void onKeyPress(int key){
    // System.out.println("key pressed: " + String.valueOf(key) +", and i got it!!");

    switch(key){
      case GLFW_KEY_A:
        this.point.x -= 5;
        break;
      case GLFW_KEY_D:
        this.point.x += 5;
        break;
      case GLFW_KEY_W:
        this.point.y += 5;
        break;
      case GLFW_KEY_S:
        this.point.y -= 5;
        break;
    }
  }

  public void draw(){
    this.draw(point.x, point.y);
    // System.out.println("its drown on ("+String.valueOf(point.x)+","+String.valueOf(point.y)+"), could you see it?");
  }
}
