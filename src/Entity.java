import static org.lwjgl.glfw.GLFW.*;
import org.joml.*;

public class Entity implements Controllable {
  public Texture texture = new Texture();
  public Model   model   = new Model();
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

  @Override
  public void onKeyPress(int key){
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

    this.texture.bind(0);
    shader.bind();
    shader.setUniform("sampler", 0);
    this.model.render();


    // System.out.println("its drown on ("+String.valueOf(point.x)+","+String.valueOf(point.y)+"), could you see it?");
  }
}
