package RenderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import EngineTester.MainGameLoop;
import Entities.Camera;
import Entities.Entity;
import Entities.Light;
import Models.TexturedModel;
import Shaders.ShaderProgram;
import Toolbox.Maths;

public class MasterRenderer {

	private Matrix4f projectionMatrix;
	private ShaderProgram shader;
	public EntityRenderer renderer;
	private Map<TexturedModel, Batch> entities = new HashMap<TexturedModel, Batch>();
	
	public MasterRenderer(ShaderProgram shader) {
		projectionMatrix = Maths.createPerspectiveProjection(70, (float) MainGameLoop.WIDTH / MainGameLoop.HEIGHT, .1f, 1000f);
		renderer = new EntityRenderer(shader, projectionMatrix);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClearColor(0, 0, 0, 1);
		this.shader = shader;
	}
	
	public void setProjectionMatrix(Matrix4f proj) {
		this.projectionMatrix = proj;
		shader.bind();
		shader.setUniformMatrix4("projectionMatrix", proj);
		shader.unbind();
	}
	
	public void clean() {
		shader.clean();
	}
	
	public void prepare() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public void render(Light light, Camera camera) {
		prepare();
		shader.bind();
		shader.setUniform3("lightPosition", light.getPosition());
		shader.setUniform3("lightColor", light.color);
		shader.setUniformMatrix4("viewMatrix", camera.getInverseCameraMatrix());
		shader.setUniform3("cameraPosition", camera.getPosition());
		renderer.render(entities);
		shader.unbind();
		entities.clear();
	}
	
	public void processEntity(Entity entity, Matrix4f transformation) {
		TexturedModel entityModel = entity.getModel();
		Batch batch = entities.get(entityModel);
		if (batch != null) {
			batch.add(entity, transformation);
		} else {
			Batch newBatch = new Batch();
			newBatch.add(entity, transformation);
			entities.put(entityModel, newBatch);
		}
	}
}
