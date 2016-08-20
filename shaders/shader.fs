#version 120

uniform sampler2D sampler; // uniform can change from java

varying vec2 tex_coords;

void main() {
  gl_FragColor =  texture2D(sampler, tex_coords);
}
