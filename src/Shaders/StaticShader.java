package Shaders;

public class StaticShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/Shaders/VertexShader.txt";
	private static final String FRAGMENT_FILE = "src/Shaders/FragmentShader.txt";
	
	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
		bindAttribute(1, "texCoords");
		bindAttribute(2, "normal");
	}
}
