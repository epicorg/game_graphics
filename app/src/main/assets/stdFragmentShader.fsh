precision mediump float;

varying vec3 varyingNormal;
varying vec3 varyingPosition;
varying vec3 varyingTxCoord;

uniform sampler2D textureMaterial;

void main(){
  
    vec2 texCoord = varyingTxCoord.xy;
  
    vec4 color1=texture2D(textureMaterial,texCoord);

	gl_FragColor=color1;

}