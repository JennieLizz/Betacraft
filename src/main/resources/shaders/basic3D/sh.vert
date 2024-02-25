#version 450 core

layout (location = 0) in vec3 jl_position;
layout (location = 1) in vec3 jl_color;
layout (location = 2) in vec2 jl_texCoords;
uniform mat4 view;
uniform mat4 proj;
uniform mat4 model;

void main() {
    gl_Position = view * proj * model * vec4(jl_position, 1.0);
}