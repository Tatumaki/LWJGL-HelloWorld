import static org.lwjgl.glfw.GLFW.*;
import org.joml.*;

public class Entity {
  public Image image;

  public void loadImage(String file){
    this.image = new Image(file);
  }

  private Vector3f position;

  public Entity() {
    this.position = new Vector3f();
  }

  public Vector3f addPosition(Vector3f position){
    return this.position.add(position);
  }

  public Vector3f setPosition(Vector3f position){
    return this.position = position;
  }

  public void updateInput(Input input) {
    input.isKeyPressed(GLFW_KEY_UP, () -> {
      addPosition(new Vector3f(0,1,0));
    });
    input.isKeyPressed(GLFW_KEY_DOWN, () -> {
      addPosition(new Vector3f(0,-1,0));
    });
    input.isKeyPressed(GLFW_KEY_LEFT, () -> {
      addPosition(new Vector3f(-1,0,0));
    });
    input.isKeyPressed(GLFW_KEY_RIGHT, () -> {
      addPosition(new Vector3f(1,0,0));
    });
  }

  public void draw(Shader shader, Matrix4f world, Camera camera){
    Matrix4f position = new Matrix4f().translate( this.position );
    Matrix4f target   = new Matrix4f();

    camera.getProjection().mul(world, target);
    target.mul(position);

    shader.bind();
    shader.setUniform("sampler", 0);
    shader.setUniform("projection", target);
    // shader.setUniform("projection", camera.getProjection().mul(target));

    image.render();

    // System.out.println("its drown on ("+String.valueOf(point.x)+","+String.valueOf(point.y)+"), could you see it?");
  }
}
