package justuswalterhelk.kyuubiforge.UI;

import justuswalterhelk.kyuubiforge.Input.Key;
import justuswalterhelk.kyuubiforge.Input.KeyListener;
import justuswalterhelk.kyuubiforge.Renderer.EditorCamera;
import justuswalterhelk.kyuubiforge.Renderer.Shader;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;


/*
    This class needs to get reworked at some point.
    This class will not be included in the production release of the engine,
    it´s currently used for testing purposes only!
 */
public class TestContainer extends Container {

    private Shader shader = null;

    private EditorCamera camera;

    //Normalized Device Coordinates!
    private float[] vertexArray =
            {
                //position                  //color
                100.5f, -100.5f, 0.0f,           0.0f,1.0f, 1.0f, 1.0f,
                -100.5f, 100.5f, 0.0f,            0.0f, 1.0f,0.0f, 1.0f,
                100.5f, 0.5f, 0.0f,             0.0f, 0.0f, 1.0f, 1.0f,
                -100.5f, -0.5f, 0.0f,          1.0f, 1.0f, 0.0f, 1.0f,
            };

    //COUNTER CLOCKWISE!
    private int[] elementArray =
    {
            2,1,0,
            0,1,3
    };

    public TestContainer()
    {
        this.containerName = "TestContainer";
        this.containerDescription = "Container for testing basic rendering and texture bashing";
    }

    private  int vaoID, vboID, eboID;

    @Override
    public void init()
    {
        this.camera = new EditorCamera(new Vector3f(-600.0f,-300.0f, 0.1f));
        System.out.println("Initializing " + containerName);

        shader = new Shader("assets/shaders/default.glsl");

        shader.compile();

        //Generate VAO VBO EBO buffers
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //Create float buffer with vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();
        //VBO
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
        //Indices
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();
        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);
        //vertex attribute pointers
        int positionSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionSize + colorSize) * floatSizeBytes;
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * floatSizeBytes);
        glEnableVertexAttribArray(1);
    }

     float cameraSpeed = 2f;

    @Override
    public void update(float deltaTime) {

        //System.out.println("[KyuubiForge] Container updated with " + deltaTime);
        if(KeyListener.isKeyPressed(Key.R.getValue()))
        {
            //Reload Shader
            shader.reload();
        }

        if(KeyListener.isKeyPressed(Key.A.getValue()))
        {
            camera.position.x -= cameraSpeed * deltaTime * 20f;
        }
        if(KeyListener.isKeyPressed(Key.D.getValue()))
        {
            camera.position.x += cameraSpeed * deltaTime * 20f;
        }
        if(KeyListener.isKeyPressed(Key.W.getValue()))
        {
            camera.position.y -= cameraSpeed * deltaTime * 20f;
        }
        if(KeyListener.isKeyPressed(Key.S.getValue()))
        {
            camera.position.y += cameraSpeed * deltaTime * 20f;
        }

        shader.use();
        shader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        shader.uploadMat4f("uView", camera.getViewMatrix());
        //Bind vao
        glBindVertexArray(vaoID);

        //Enable vertex attr
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        //glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        //Unbind clear up
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        shader.detach();
    }
}
