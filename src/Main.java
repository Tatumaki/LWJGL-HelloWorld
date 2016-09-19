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
  private int WIDTH = 640;
  private int HEIGHT = 480;

  private int count = 0;
  private Window window;

  private Shader shader;
  private Camera camera;

  private World world;

  public static void main(String[] args) {
    new Main().run();
  }

  public Main(){
    Window.setCallbacks();
  }

  public void run() {
    System.out.println("Hello LWJGL " + Version.getVersion() + "!");

    try {
      init();
      loop();

      window.destroy();
    } finally {
      // Terminate GLFW and free the error callback
      glfwTerminate();
      glfwSetErrorCallback(null).free();
    }
  }

  private void initGame(){
    world  = new World(window);
    camera = new Camera(window.getWidth(), window.getHeight());

    // Setup a key callback. It will be called every time a key is pressed, repeated or released.
    glfwSetKeyCallback(window.getWindow(), (window, key, scancode, action, mods) -> {
      if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
        glfwSetWindowShouldClose(window, true); // We will detect this in our rendering loop
    });
  }

  private void init() {
    // Setup an error callback. The default implementation
    // will print the error message in System.err.
    GLFWErrorCallback.createPrint(System.err).set();

    // Initialize GLFW. Most GLFW functions will not work before doing this.
    if ( !glfwInit() ) {
      throw new IllegalStateException("Unable to initialize GLFW");
    }

    window = new Window();
    window.setSize(WIDTH, HEIGHT);
    window.setFullscreen(false);
    window.createWindow("Game");
    window.setWindowPositionCentor();

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

    // Shaderの読み込み
    shader = new Shader("shader");

    Syncer.setUp(60);

    initGame();

    // Configure our window
    // glfwDefaultWindowHints(); // optional, the current window hints are already the default
    // glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
    // glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

    // Enable v-sync
    glfwSwapInterval(1);
  }

  private void loop() {
    // Run the rendering loop until the user has attempted to close
    // the window or has pressed the ESCAPE key.
    while ( !window.shouldClose() ) {
      update();
      render();

      Syncer.sync(60);
    }
  }

  private void update(){
    ++count;

    window.update();
    camera.update(window.getInput());
    
    if(count % 60 == 0){
      System.out.println("FPS: "+ String.valueOf(Syncer.currentFps()));
    }
  }

  private void render(){
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

    world.update();
    world.render(shader, camera);

    window.swapBuffers();
  }
};
