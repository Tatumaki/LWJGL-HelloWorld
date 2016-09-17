import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.glfw.*;
import org.lwjgl.*;

import java.util.function.*;

public class Input {
  private long window;

  private boolean keys[];

  public Input(long window){
    this.window = window;
    this.keys   = new boolean[GLFW_KEY_LAST];

    for(int i=0; i< GLFW_KEY_LAST; i++){
      keys[i] = false;
    }
  }

  public boolean isKeyDown(int key){
    return glfwGetKey(window, key) == GL_TRUE;
  }

  public boolean isMouseButtonDown(int button){
    return glfwGetMouseButton(window, button) == GL_TRUE;
  }

  public void isKeyPressed(int key, Runnable lambda){
    if (isKeyDown(key)){
      lambda.run();
    }
  }

  public boolean isKeyPressed(int key){
    return (isKeyDown(key) && !keys[key]);
  }

  public void update(){
    // http://www.glfw.org/docs/latest/group__keys.html
    // Key code under 32 does not exists. Also it couldn't found 33 - 38, but it works.
    for(int i=32; i < GLFW_KEY_LAST; i++){
      keys[i] = isKeyDown(i);
    }
  }

  public void forAllKey(Consumer<Integer> consumer) {
    for(int i=32; i < GLFW_KEY_LAST; i++){
      consumer.accept(i);
    }
  }
}
