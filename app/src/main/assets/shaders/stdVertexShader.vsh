precision mediump float;
uniform mat4 transform;
uniform mat4 projection;
attribute vec3 position;
attribute vec3 normal;
attribute vec3 txCoord;
varying vec3 varyingPosition;
varying vec3 varyingNormal;
varying vec3 varyingTxCoord;
void main(){
	gl_Position=projection*transform*vec4(position,1.0);
    varyingNormal=normalize((transform*vec4(normal,0)).xyz);
    varyingPosition=gl_Position.xyz;
    varyingTxCoord=txCoord;
}
