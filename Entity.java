import static org.lwjgl.glfw.GLFW.*;

public class Entity implements Controllable {
  public Texture texture = new Texture();
  public Model   model   = new Model();
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
    this.texture.bind(0);
    this.model.render();

    // System.out.println("its drown on ("+String.valueOf(point.x)+","+String.valueOf(point.y)+"), could you see it?");
  }
}
