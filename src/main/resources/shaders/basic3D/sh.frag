#version 450 core

out vec4 fragColor;

in vec3 aColor;
in vec2 aTexCoord;

uniform vec2 iResolution;
uniform float iTime;
uniform sampler2D image;

void main() {
    fragColor = texture(image, aTexCoord);
}