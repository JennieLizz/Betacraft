#version 450 core

layout (location = 0) in vec3 jl_position;
layout (location = 1) in vec3 jl_color;
layout (location = 2) in vec2 jl_texCoords;
uniform mat4 view;
uniform mat4 proj;
uniform mat4 model;

out vec3 aColor;
out vec2 aTexCoord;

void main() {
    gl_Position = proj * view * model * vec4(jl_position, 1.0);
    aColor = jl_color;
    aTexCoord = jl_texCoords;
}