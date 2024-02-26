#version 450 core

out vec4 fragColor;

in vec3 aColor;
in vec2 aTexCoord;

uniform vec2 iResolution;
uniform float iTime;
uniform sampler2D image;

void main() {
    vec4 col = texture2D(image, aTexCoord);
    if (col.a < 0.8)
    discard;
    fragColor = col;
}