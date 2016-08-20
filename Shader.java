import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class Shader {
  private int program;
  private int vs; // vertex shader
  private int fs; // fragment shader

  public Shader(String filename) {
    program = glCreateProgram();

    vs = glCreateShader(GL_VERTEX_SHADER);
    glShaderSource(vs, readFile(filename+".vs"));
    glCompileShader(vs);

    if(glGetShaderi(vs, GL_COMPILE_STATUS) != 1){
      System.err.println(glGetShaderInfoLog(vs));
      System.exit(1);
    }

    fs = glCreateShader(GL_FRAGMENT_SHADER);
    glShaderSource(fs, readFile(filename+".fs"));
    glCompileShader(fs);

    if(glGetShaderi(fs, GL_COMPILE_STATUS) != 1){
      System.err.println(glGetShaderInfoLog(fs));
      System.exit(1);
    }

    glAttachShader(program, vs);
    glAttachShader(program, fs);

    glBindAttribLocation(program, 0, "vertices"); // same name as attributes in shader.vs 
    glBindAttribLocation(program, 1, "textures"); // same name as attributes in shader.vs 
    // appling to 0. used in Model#bind

    glLinkProgram(program);
    if(glGetProgrami(program, GL_LINK_STATUS) != 1){
      System.err.println(glGetProgramInfoLog(program));
      System.exit(1);
    }
    glValidateProgram(program);
    if(glGetProgrami(program, GL_VALIDATE_STATUS) != 1){
      System.err.println(glGetProgramInfoLog(program));
      System.exit(1);
    }
  }

  public void bind(){
    glUseProgram(program);
  }

  public void setUniform(String name, float value){
    int location = glGetUniformLocation(program, name);
    if(location != -1){
      glUniform1f(location, value);
    }
  }

  private String readFile(String filename){
    StringBuilder string = new StringBuilder();
    BufferedReader br;
      
    try{
      br = new BufferedReader(new FileReader(new File("./shaders/" + filename)));
      String line;
      while(( line = br.readLine() ) != null) {
        string.append(line);
        string.append("\n");
      }
      br.close();
    }catch(IOException e){
      e.printStackTrace();
    }

    return string.toString();
  }
}
