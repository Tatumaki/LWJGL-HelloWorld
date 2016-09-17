
import java.util.Arrays;

public final class Syncer {
  private static long variableYieldTime, lastTime;
  private static long[] times = new long[60];

  private static int fps = 60;
  private static int index = 0;

  public static void setUp(final int fps){
    times    = new long[fps];
    Syncer.fps = fps;
    Arrays.fill(times,0);
  }

  public static double currentFps(){
    long sum = 0;
    for(long time: times){
      sum += time;
    }
    return 1000.0 / (sum / (double)(fps) / 1000000.0);
  }

  public static void sync(int fps) {
    if (fps <= 0) return;
      
    long sleepTime = 1000000000 / fps; // nanoseconds to sleep this frame
    // yieldTime + remainder micro & nano seconds if smaller than sleepTime
    long yieldTime = Math.min(sleepTime, variableYieldTime + sleepTime % (1000*1000));
    long overSleep = 0; // time the sync goes over by
    long t;
      
    try {
      while (true) {
        t = System.nanoTime() - lastTime;

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

      times[index] = t;
      index = (index + 1) % fps;
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
}
