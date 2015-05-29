package Shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.HashMap;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import Entities.Light;

public class ShaderProgram {

	private static final String VERTEX_FILE = "src/Shaders/VertexShader.txt";
	private static final String FRAGMENT_FILE = "src/Shaders/FragmentShader.txt";
	
	private HashMap<String, Integer> uniformCache;
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	public ShaderProgram() {
		this(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public ShaderProgram(String vertexFile, String fragmentFile) {
		vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		bindAttributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		uniformCache = new HashMap<String, Integer>();
		//System.out.println(GL20.glGetProgrami(programID, GL20.GL_ACTIVE_UNIFORM_MAX_LENGTH));
	}
	
	public void bind() {
		GL20.glUseProgram(programID);
	}
	
	public void unbind() {
		GL20.glUseProgram(0);
	}
	
	public void clean() {
		unbind();
		GL20.glDeleteProgram(programID);
	}
	
	protected void bindAttributes() {
		bindAttribute(0, "position");
		bindAttribute(1, "texCoords");
		bindAttribute(2, "normal");
	}
	
	protected void bindAttribute(int attribute, String variableName) {
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}
	
	private static int loadShader(String file, int type) {
		StringBuilder shaderSource = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				shaderSource.append(line).append("\n");
			}
			
			br.close();
		} catch (IOException e) {
			System.err.println("Could not read file!");
			e.printStackTrace();
			System.exit(1);
		}
		
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		
		if (GL20.glGetShader(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.err.println("Could not compile shader.");
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.exit(1);
		}
		
		return shaderID;
	}
	
	private int getUniformLocation(String variable) {
		int location = 0;
		if (uniformCache.get(variable) == null) {
			location = GL20.glGetUniformLocation(programID, variable);
			uniformCache.put(variable, location);
		} else {
			location = uniformCache.get(variable);
		}

		return location;
	}
	
	public void setUniform1(String variable, int value) {
		GL20.glUniform1i(getUniformLocation(variable), value);
	}
	
	public void setUniform1(String variable, float value) {
		GL20.glUniform1f(getUniformLocation(variable), value);
	}
	
	public void setUniform3(String variable, Vector3f value) {
		GL20.glUniform3f(getUniformLocation(variable), value.x, value.y, value.z);
	}
	
	public void setUniformMatrix4(String variable, Matrix4f matrix) {
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4(getUniformLocation(variable), false, matrixBuffer);
	}
}
