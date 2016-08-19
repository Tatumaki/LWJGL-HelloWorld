import java.util.ArrayList;

public final class Controller {
  private Controller(){};

  private static ArrayList<Controllable> entries = new ArrayList<Controllable>();

  public static void register(Controllable controllable){
    entries.add(controllable);
  }

  public static void keyPressed(int key){
    for(Controllable controllable: entries){
      controllable.onKeyPress(key);
    }
  }
} 
