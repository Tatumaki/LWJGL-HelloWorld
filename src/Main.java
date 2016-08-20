import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.util.Arrays;

import org.joml.*;

public class Main {
  // The window handle
  private long window;
  private int WIDTH = 1280;
  private int HEIGHT = 1024;
  private long variableYieldTime, lastTime;

  private Entity entity;
  private Shader shader;
  private Matrix4f projection;
  private Matrix4f scale;
  private Matrix4f target;
  private Camera camera = new Camera(WIDTH, HEIGHT);

  private int count = 0;

  public static void main(String[] args) {
    new Main().run();
  }

  public Main(){
  }

  public void run() {
    System.out.println("Hello LWJGL " + Version.getVersion() + "!");

    try {
      init();
      loop();

      // Free the window callbacks and destroy the window
      glfwFreeCallbacks(window);
      glfwDestroyWindow(window);
    } finally {
      // Terminate GLFW and free the error callback
      glfwTerminate();
      glfwSetErrorCallback(null).free();
    }
  }

  private void initGame(){
  }

  private void init() {
    // Setup an error callback. The default implementation
    // will print the error message in System.err.
    GLFWErrorCallback.createPrint(System.err).set();

    // Initialize GLFW. Most GLFW functions will not work before doing this.
    if ( !glfwInit() )
      throw new IllegalStateException("Unable to initialize GLFW");

    // Configure our window
    // glfwDefaultWindowHints(); // optional, the current window hints are already the default
    // glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
    // glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable


    // Create the window
    window = glfwCreateWindow(WIDTH, HEIGHT, "Hello World!", NULL, NULL);
    if ( window == NULL )
      throw new RuntimeException("Failed to create the GLFW window");

    // Setup a key callback. It will be called every time a key is pressed, repeated or released.
    glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
      if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
        glfwSetWindowShouldClose(window, true); // We will detect this in our rendering loop
    });

    // Get the resolution of the primary monitor
    GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
    // Center our window
    glfwSetWindowPos(
      window,
      (vidmode.width() - WIDTH) / 2,
      (vidmode.height() - HEIGHT) / 2
    );

    // Make the OpenGL context current
    glfwMakeContextCurrent(window);
    // Enable v-sync
    glfwSwapInterval(1);

    // Make the window visible
    glfwShowWindow(window);

    initGame();
  }

  private void loop() {
    // This line is critical for LWJGL's interoperation with GLFW's
    // OpenGL context, or any context that is managed externally.
    // LWJGL detects the context that is current in the current thread,
    // creates the GLCapabilities instance and makes the OpenGL
    // bindings available for use.
    GL.createCapabilities();

    // 2D描画をオンにする
    glEnable(GL_TEXTURE_2D);

    // ポリゴンの片面（表 or 裏）表示を有効にする
    // glEnable(GL_CULL_FACE);
    //  ポリゴンの表示面を表（裏を表示しない）のみに設定する
    // glCullFace(GL_BACK);
       
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    // 視体積（描画する領域）を定義する
    // glOrtho(0, WIDTH, 0, HEIGHT, 0, 300);
    glMatrixMode(GL_MODELVIEW);

    shader = new Shader("shader");
    // center of monitor.
    projection = new Matrix4f().ortho2D(-1280/2.0f, 1280/2.0f, -1024/2.0f, 1024/2.0f);
    scale      = new Matrix4f().translate(new Vector3f(100, 0, 0)).scale(64);
    target     = new Matrix4f();

    camera.setPosition(new Vector3f(-100,0,0));

    entity = new Entity();
    entity.texture.load("./res/image.png");
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

    // Run the rendering loop until the user has attempted to close
    // the window or has pressed the ESCAPE key.
    while ( !glfwWindowShouldClose(window) ) {
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

      target = scale;

      update();

      // Poll for window events. The key callback above will only be
      // invoked during this call.
      glfwPollEvents();

      render();
      sync(60);

      glfwSwapBuffers(window); // swap the color buffers
    }
  }

  /**
   * An accurate sync method that adapts automatically
   * to the system it runs on to provide reliable results.
   * 
   * @param fps The desired frame rate, in frames per second
   * @author kappa (On the LWJGL Forums)
   */
  private void sync(int fps) {
    if (fps <= 0) return;
      
    long sleepTime = 1000000000 / fps; // nanoseconds to sleep this frame
    // yieldTime + remainder micro & nano seconds if smaller than sleepTime
    long yieldTime = Math.min(sleepTime, variableYieldTime + sleepTime % (1000*1000));
    long overSleep = 0; // time the sync goes over by
      
    try {
      while (true) {
        long t = System.nanoTime() - lastTime;

        if (t < sleepTime - yieldTime) {
          Thread.sleep(1);
        }else if (t < sleepTime) {
          // burn the last few CPU cycles to ensure accuracy
          Thread.yield();
        }else {
          overSleep = t - sleepTime;
          break; // exit while loop
        }
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }finally{
      lastTime = System.nanoTime() - Math.min(overSleep, sleepTime);

      // auto tune the time sync should yield
      if (overSleep > variableYieldTime) {
        // increase by 200 microseconds (1/5 a ms)
        variableYieldTime = Math.min(variableYieldTime + 200*1000, sleepTime);
      }
      else if (overSleep < variableYieldTime - 200*1000) {
        // decrease by 2 microseconds
        variableYieldTime = Math.max(variableYieldTime - 2*1000, 0);
      }
    }
  }


  private void update(){
    ++count;

    if(glfwGetKey(window, GLFW_KEY_A)  == GL_TRUE ){
      Controller.keyPressed(GLFW_KEY_A);
    }
    if(glfwGetKey(window, GLFW_KEY_D)  == GL_TRUE ){
      Controller.keyPressed(GLFW_KEY_D);
    }
    if(glfwGetKey(window, GLFW_KEY_W)  == GL_TRUE ){
      Controller.keyPressed(GLFW_KEY_W);
    }
    if(glfwGetKey(window, GLFW_KEY_S)  == GL_TRUE ){
      Controller.keyPressed(GLFW_KEY_S);
    }
  }

  private void render(){
    // // Set the clear color
    // glClearColor(0.5f, 0.5f, 0.0f, 0.0f);
    
    shader.bind();
    shader.setUniform("sampler", 0);
    shader.setUniform("projection", camera.getProjection().mul(target));

    entity.draw();
  }

};
