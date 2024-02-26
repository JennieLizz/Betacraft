package jlengine.model;

import jlengine.components.math.Transform;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class RawModel extends ModelManager {
    final String m_name;
    final int m_vaoID;
    final int m_vboID;
    final int m_eboID;
    public Transform transform = new Transform();
    String m_shaderLayer = "Default";
    float[] m_vertices;
    float[] m_texCoords;
    int[] m_indices = {};
    FloatBuffer m_vert;
    FloatBuffer m_tex;
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

        m_eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m_eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, m_ind, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * Float.BYTES, 0L);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * Float.BYTES, 6 * Float.BYTES);

        glBindVertexArray(0);
        glBindBuffer(0, 0);

        AddModelToManager(this);
    }

    public void Bind() {
        glBindVertexArray(m_vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glDrawElements(GL_TRIANGLES, m_vertices.length, GL_UNSIGNED_INT, 0L);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
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
