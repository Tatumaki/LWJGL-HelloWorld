import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.*;

import java.util.function.*;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {
  private Vector3f position;
  private Matrix4f projection;

  public Camera(int width, int height){
    position = new Vector3f(0,0,0);
    projection = new Matrix4f().setOrtho2D(-width/2, width/2, -height/2, height/2);
  }

  public void setPosition(Vector3f position){
    this.position = position;
  }

  public void addPosition(Vector3f position){
    this.position.add(position);
  }

  public Vector3f getPosition(){
    return position;
  }

  public Matrix4f getProjection(){
    Matrix4f target = new Matrix4f();
    Matrix4f pos    = new Matrix4f().setTranslation(position);

    target = projection.mul(pos, target);
    return target;
  }

  public void update(Input input){
    input.isKeyPressed(GLFW_KEY_W, () -> {
      getPosition().sub(new Vector3f(0,1,0));
    });
    input.isKeyPressed(GLFW_KEY_S, () -> {
      getPosition().sub(new Vector3f(0,-1,0));
    });
    input.isKeyPressed(GLFW_KEY_D, () -> {
      getPosition().sub(new Vector3f(1,0,0));
    });
    input.isKeyPressed(GLFW_KEY_A, () -> {
      getPosition().sub(new Vector3f(-1,0,0));
    });
  }
}
