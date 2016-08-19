import static org.lwjgl.opengl.GL11.*;

public class Drawable extends Texture {
  float scale = 0.1f;

  public void draw(int x, int y){
    this.bind();

    int scaled_width  = (int)( this.width() * scale );
    int scaled_height = (int)( this.height() * scale );

    //  次に指定する４つの座標を、描く四角形の頂点として認識させる
    glBegin(GL_QUADS);
      glTexCoord2f(0,0); // 左上
      glVertex2f(x - scaled_width, y + scaled_height);

      glTexCoord2f(0,1); // 左下
      glVertex2f(x - scaled_width, y - scaled_height);

      glTexCoord2f(1,1); // 右下
      glVertex2f(x + scaled_width, y - scaled_height);

      glTexCoord2f(1,0); // 右上
      glVertex2f(x + scaled_width, y + scaled_height);
    glEnd();
  }
}
