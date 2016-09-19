
public class Image {
  public Texture texture = new Texture();
  public Model   model   = new Model();
  final private String file;

  public Image(String file){
    this.file = file;
    this.texture.load(this.file);
    this.model.load(
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
  }

  public void render(){
    this.texture.bind(0);
    this.model.render();
  }
}
