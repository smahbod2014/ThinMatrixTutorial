package Entities;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import RenderEngine.MasterRenderer;
import Toolbox.Maths;

public class Transform extends SceneNode {

	Matrix4f transform;
	
	public Transform() {
		this(Maths.identity());
	}
	
	public Transform(Matrix4f transform) {
		this.transform = transform;
	}
	
	public void draw(MasterRenderer renderer, Matrix4f transformation) {
		Matrix4f result = new Matrix4f();
		Matrix4f.mul(transformation, transform, result);
		super.draw(renderer, result);
	}
	
	public void setTransform(Matrix4f transform) {
		this.transform = transform;
	}
	
	public void rotate(Vector3f axis, float degrees) {
		Matrix4f rotation = Maths.rotation(axis, degrees);
		Matrix4f.mul(transform, rotation, transform);
	}
	
	public void rotateWorld(Vector3f axis, float degrees) {
		Matrix4f rotation = Maths.rotation(axis, degrees);
		Matrix4f.mul(rotation, transform, transform);
	}
	
	public void translate(float x, float y, float z) {
		Matrix4f translation = Maths.translation(x, y, z);
		Matrix4f.mul(translation, transform, transform);
	}
	
	public void translate(Vector3f amount) {
		translate(amount.x, amount.y, amount.z);
	}
	
	public void setPosition(float x, float y, float z) {
		Vector3f curr = new Vector3f(x, y, z);
		Vector3f.sub(curr, getPosition(), curr);
		translate(curr);
	}
	
	public void setPosition(Vector3f pos) {
		setPosition(pos.x, pos.y, pos.z);
	}
	
	public Vector3f getPosition() {
		return new Vector3f(transform.m30, transform.m31, transform.m32);
	}
	
	public void rotateToPoint(Vector3f point) {
		Vector3f forward = new Vector3f(transform.m20, transform.m21, transform.m22);
//		System.out.println(forward);
		Vector3f toPoint = new Vector3f();
		Vector3f.sub(point, getPosition(), toPoint);
		float angle = Vector3f.angle(forward, toPoint);
		Vector3f axis = new Vector3f();
		Vector3f.cross(forward, toPoint, axis);
		if (axis.lengthSquared() != 0)
			axis.normalise();
		rotate(axis, (float) Math.toDegrees(angle));
	}
	
	public void print() {
		System.out.println(transform);
	}
}
