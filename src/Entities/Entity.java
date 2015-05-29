package Entities;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import Models.TexturedModel;
import Toolbox.Maths;

public class Entity {

	private Vector3f tempPosition = new Vector3f();
	private TexturedModel model;
	private Matrix4f transformation;
	
	public Entity(TexturedModel model) {
		this(model, new Vector3f(0, 0, 0), new Vector3f(0, 1, 0), 0, 1, 1, 1);
	}
	
	public Entity(TexturedModel model, Vector3f position, Vector3f axis, float degrees, float sx, float sy, float sz) {
		this.model = model;
		transformation = new Matrix4f();
		translate(position);
		rotate(axis, degrees);
		scale(sx, sy, sz);
	}
	
	public void rotate(Vector3f axis, float degrees) {
		Matrix4f rotation = Maths.rotation(axis, degrees);
		Matrix4f.mul(transformation, rotation, transformation);
	}
	
	public void translate(Vector3f amount) {
		Matrix4f translation = Maths.translation(amount);
		Matrix4f.mul(translation, transformation, transformation);
	}
	
	public void translateLocal(Vector3f amount) {
		Matrix4f translation = Maths.translation(amount);
		Matrix4f.mul(transformation, translation, transformation);
	}
	
	public void scale(float sx, float sy, float sz) {
		Matrix4f scale = Maths.scale(sx, sy, sz);
		Matrix4f.mul(transformation, scale, transformation);
	}
	
	public void setPosition(Vector3f newPosition) {
		translate(Vector3f.sub(newPosition, getPosition(), tempPosition));
	}
	
	public Vector3f getPosition() {
		tempPosition.x = transformation.m30;
		tempPosition.y = transformation.m31;
		tempPosition.z = transformation.m32;
		return tempPosition;
	}
	
	public Matrix4f getTransformation() {
		return transformation;
	}
	
	public void setTransformation(Matrix4f transform) {
		this.transformation = transform;
	}

	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}
}
