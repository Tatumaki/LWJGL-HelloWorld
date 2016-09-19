import org.joml.*;
import org.lwjgl.glfw.*;
import static org.lwjgl.glfw.GLFW.*;

public class World {
  private Matrix4f world;
  private Entity entity;
  private Window window;

  public World(Window window){
    this.window = window;

    world = new Matrix4f().setTranslation(new Vector3f(0)); // LEFT TOP
    world.scale(16);

    entity = new Entity();
    entity.loadImage("res/corn.png");
  } 

  public void render(Shader shader, Camera camera) {
    entity.draw( shader, world, camera );
  }

  public void update(){
    entity.updateInput(window.getInput());
  }
}
