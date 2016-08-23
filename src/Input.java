import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.glfw.*;
import org.lwjgl.*;

public class Input {
  private long window;

  public Input(long window){
    this.window = window;
  }

  public boolean isKeyDown(int key){
    return glfwGetKey(window, key) == GL_TRUE;
  }

  public boolean isMouseButtonDown(int button){
    return glfwGetMouseButton(window, button) == GL_TRUE;
  }
}
