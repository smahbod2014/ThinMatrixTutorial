package RenderEngine;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Matrix4f;

import Entities.Entity;

public class Batch {

	List<Entity> entities;
	List<Matrix4f> transformations;
	int size;
	
	public Batch() {
		size = 0;
		entities = new ArrayList<Entity>();
		transformations = new ArrayList<Matrix4f>();
	}
	
	public void add(Entity entity, Matrix4f transformation) {
		entities.add(entity);
		transformations.add(transformation);
		size++;
	}
}
