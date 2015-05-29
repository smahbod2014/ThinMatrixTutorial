package Entities;

import org.lwjgl.util.vector.Matrix4f;

import RenderEngine.MasterRenderer;

public class Geode extends SceneNode {

	public Entity entity;
	
	public Geode(Entity entity) {
		super();
		this.entity = entity;
	}
	
	public void draw(MasterRenderer renderer, Matrix4f transformation) {
		if (entity == null)
			return;
		
		//entity.setTransformation(transformation);
		renderer.processEntity(entity, transformation);
		
		super.draw(renderer, transformation);
	}
	
	public void setEntity(Entity entity) {
		this.entity = entity;
	}
}
