#version 450 core

out vec4 fragColor;

in vec3 aColor;
in vec2 aTexCoord;

uniform vec2 iResolution;
uniform float iTime;
uniform sampler2D image;

void main() {
    fragColor = vec4(0, 0, 1, 0.3);
}