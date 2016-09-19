import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.Callbacks.*;
import org.lwjgl.glfw.*;

public class Window {
  private long window; 

  private int width, height;
  private boolean fullscreen;

  private Input input;

  public static void setCallbacks() {
    glfwSetErrorCallback(new GLFWErrorCallback() {
      @Override 
      public void invoke(int error, long description){
        throw new IllegalStateException( GLFWErrorCallback.getDescription(description));
      }
    });
  }

  public Window(){
    setSize(640,480);
    setFullscreen(false);
  }

  public void setWindowPosition(int x, int y) {
    if (!fullscreen) return;
    glfwSetWindowPos(window, x, y);
  }

  public void setWindowPositionCentor() {
    if (!fullscreen) return;
    // Get the resolution of the primary monitor
    GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

    setWindowPosition(
      (vidmode.width()  - width ) / 2,
      (vidmode.height() - height) / 2
    );
  }

  public void createWindow(String title){
    // window = glfwCreateWindow(width, height, title, 0 #<{(| screen, primary = 0|)}>#, 0 #<{(| multi screen|)}>#);
    window = glfwCreateWindow(
      width, height, title, 
      fullscreen ?  glfwGetPrimaryMonitor() : 0,
      0 /* multi screen*/);

    if(window == 0) {
      throw new IllegalStateException("Failed to create window.");
    }

    glfwShowWindow(window);
    glfwMakeContextCurrent(window);

    input = new Input(window);
  }

  public void setSize(int width, int height){
    this.width  = width;
    this.height = height;
  }
  

  public boolean shouldClose() {
    return glfwWindowShouldClose(window);
  }

  public void swapBuffers() {
    glfwSwapBuffers(window); // swap the color buffers
  }

  public void destroy() {
    glfwFreeCallbacks(window);
    glfwDestroyWindow(window);
  }

  public void setFullscreen(boolean fullscreen){
    this.fullscreen = fullscreen;
  }

  public void update() {
    input.update();
    glfwPollEvents();
  }

  public int     getWidth()      { return width;      }
  public int     getHeight()     { return height;     }
  public boolean getFullscreen() { return fullscreen; }
  public long    getWindow()     { return window;     }
  public Input   getInput()      { return input;      }
}
