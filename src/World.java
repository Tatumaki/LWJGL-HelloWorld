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
    entity.texture.load("./res/corn.png");
    entity.model.load(
      new float[] {
        -1.0f, 1.0f, 0, //TOP LEFT      0
         1.0f, 1.0f, 0, //TOP RIGHT     1
         1.0f,-1.0f, 0, //BUTTOM RIGHT  2
        -1.0f,-1.0f, 0, //BOTTOM LEFT   3
      },
      new float[] {
        0,0, // 0
        1,0, // 1
        1,1, // 2
        0,1, // 3
      },
      new int[] {
        0,1,2,
        2,3,0
      }
    );

    Controller.register(entity);
  } 

  public void render(Shader shader, Camera camera) {
    entity.draw( shader, world, camera );
  }

  public void update(){
    window.getInput().isKeyPressed(GLFW_KEY_UP, () -> {
      entity.addPosition(new Vector3f(0,1,0));
    });
    window.getInput().isKeyPressed(GLFW_KEY_DOWN, () -> {
      entity.addPosition(new Vector3f(0,-1,0));
    });
    window.getInput().isKeyPressed(GLFW_KEY_LEFT, () -> {
      entity.addPosition(new Vector3f(-1,0,0));
    });
    window.getInput().isKeyPressed(GLFW_KEY_RIGHT, () -> {
      entity.addPosition(new Vector3f(1,0,0));
    });
  }
}
