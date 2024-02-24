package jlengine.model;

import jlengine.components.math.Transform;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class RawModel extends ModelManager {
    final String m_name;
    final int m_vaoID;
    final int m_vboID;
    final int m_eboID;
    public Transform transform = new Transform();
    String m_shaderLayer = "Default";
    float[] m_vertices;
    int[] m_indices = {};
    FloatBuffer m_vert;
    IntBuffer m_ind;

    public RawModel(String name, float[] vertices, int[] indices) {
        m_name = name;
        m_vertices = vertices;
        m_vert = StoreDataInFloatBuffer(vertices);
        m_indices = indices;
        m_ind = StoreDataInIntBuffer(indices);

        m_vaoID = glGenVertexArrays();
        glBindVertexArray(m_vaoID);

        m_vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, m_vboID);

        glBufferData(GL_ARRAY_BUFFER, m_vert, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        m_eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m_eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, m_ind, GL_STATIC_DRAW);

        glBindVertexArray(0);

        AddModelToManager(this);
    }

    public void Bind() {
        glBindVertexArray(m_vaoID);
        glEnableVertexAttribArray(0);
        glDrawElements(GL_TRIANGLES, m_vertices.length, GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
    }

    public void SetLayer(String layer) {
        m_shaderLayer = layer;
    }

    public String GetLayer() {
        return m_shaderLayer;
    }

    public int GetVaoID() {
        return m_vaoID;
    }

    public int GetVboID() {
        return m_vboID;
    }

    public int GetEboID() {
        return m_eboID;
    }

    public void SetVertices(float[] vertices) {
        m_vertices = vertices;
        m_vert = StoreDataInFloatBuffer(vertices);
    }

    public void SetIndices(int[] indices) {
        m_indices = indices;
        m_ind = StoreDataInIntBuffer(indices);
    }

    private FloatBuffer StoreDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private IntBuffer StoreDataInIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    public void Delete() {
        glDeleteBuffers(m_vboID);
        glDeleteBuffers(m_eboID);
        glDeleteVertexArrays(m_vaoID);

        RemoveModelFromManager(this);
    }

    void SDelete() {
        glDeleteBuffers(m_vboID);
        glDeleteBuffers(m_eboID);
        glDeleteVertexArrays(m_vaoID);
    }
}
