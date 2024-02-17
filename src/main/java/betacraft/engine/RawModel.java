package betacraft.engine;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class RawModel extends ModelManager {
  int m_vaoID, m_vboID, m_vboID2;

  float[] m_vertices = {};

  int[] m_indices = {};

  FloatBuffer m_vert;
  IntBuffer m_ind;

  public RawModel() {
    m_vaoID = glGenVertexArrays();
    glBindVertexArray(m_vaoID);

    m_vboID = glGenBuffers();
    glBindBuffer(GL_ARRAY_BUFFER, m_vboID);

    glBufferData(GL_ARRAY_BUFFER, m_vert, GL_STATIC_DRAW);
    glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
    glBindBuffer(GL_ARRAY_BUFFER, 0);

    m_vboID2 = glGenBuffers();
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m_vboID2);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, m_ind, GL_STATIC_DRAW);

    glBindVertexArray(0);
  }

  public void Bind() {
    glBindVertexArray(m_vaoID);
    glEnableVertexAttribArray(0);
    glDrawElements(GL_TRIANGLES, m_vertices.length, GL_UNSIGNED_INT, 0);
    glDisableVertexAttribArray(0);
    glBindVertexArray(0);
  }

  public int GetVaoID() {
    return m_vaoID;
  }

  public int GetVboID(boolean element) {
    if (element)
      return m_vboID2;
    else
      return m_vboID;
  }

  public void SetVertices(float[] vertices) {
    this.m_vertices = vertices;
    this.m_vert = StoreDataInFloatBuffer(vertices);
  }

  public void SetIndices(int[] indices) {
    this.m_indices = indices;
    this.m_ind = StoreDataInIntBuffer(indices);
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
    glDeleteBuffers(m_vboID2);
    glDeleteVertexArrays(m_vaoID);
  }
}
