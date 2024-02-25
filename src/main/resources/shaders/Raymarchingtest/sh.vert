#version 450 core

layout (location = 0) in vec3 aPos;
uniform mat4 view;
uniform mat4 proj;
uniform mat4 model;

out vec3 camPos;

void main() {
    gl_Position = view * proj * model * vec4(aPos, 1.0);
    vec4 camPosB = view * vec4(aPos, 1.0);
    camPos = vec3(0);
}