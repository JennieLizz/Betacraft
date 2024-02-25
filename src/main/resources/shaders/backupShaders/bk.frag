#version 450 core

out vec4 fragColor;

uniform vec2 iResolution;
uniform float iTime;
vec2 fragCoord = vec2(gl_FragCoord.xy);

void main() {
    fragColor = vec4(1.0, 0.0, 1.0, 1.0);
}