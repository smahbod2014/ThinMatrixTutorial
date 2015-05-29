package RenderEngine;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import Entities.Entity;
import Models.RawModel;
import Models.TexturedModel;
import Shaders.ShaderProgram;
import Textures.ModelTexture;

public class EntityRenderer {

	private ShaderProgram shader;
	
	public EntityRenderer(ShaderProgram shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.bind();
		shader.setUniformMatrix4("projectionMatrix", projectionMatrix);
		shader.unbind();
	}
	
	public void render(Map<TexturedModel, Batch> entities) {
		for (TexturedModel model : entities.keySet()) {
			prepareTexturedModel(model);
			Batch batch = entities.get(model);
			for (int i = 0; i < batch.size; i++) {
				prepareInstance(batch.entities.get(i), batch.transformations.get(i));
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
	}
	
	private void prepareTexturedModel(TexturedModel model) {
		RawModel rm = model.getRawModel();
		GL30.glBindVertexArray(rm.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		
		ModelTexture texture = model.getTexture();
		shader.setUniform1("reflectivity", texture.getReflectivity());
		shader.setUniform1("shineDamper", texture.getShineDamper());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getTextureID());
	}
	
	private void unbindTexturedModel() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void prepareInstance(Entity entity, Matrix4f transformation) {
		shader.setUniformMatrix4("transformationMatrix", transformation);
	}
}
