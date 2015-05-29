package Entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import Toolbox.Maths;

public class Camera {

	private static Vector3f temp = new Vector3f();
	private static Vector4f tempv4 = new Vector4f();
	
	private Matrix4f cameraMatrix = new Matrix4f();
	private Matrix4f cameraInverseMatrix = new Matrix4f();
	
	private Vector3f position;
	private Vector3f right = new Vector3f();
	private Vector3f up = new Vector3f();
	private Vector3f forward = new Vector3f();
	
	private Vector3f lookAt;
	private Vector3f worldUp;
	
	public Camera(Vector3f pos, Vector3f lookAt, Vector3f worldUp) {
		this.position = pos;
		this.lookAt = lookAt;
		this.worldUp = worldUp;
		update();
	}
	
	private void update() {
		forward = Vector3f.sub(position, lookAt, forward).normalise(forward);
		right = Vector3f.cross(worldUp, forward, right).normalise(right);
		up = Vector3f.cross(forward, right, up).normalise(up);
		
		cameraMatrix.m00 = right.x;
		cameraMatrix.m01 = right.y;
		cameraMatrix.m02 = right.z;
		cameraMatrix.m10 = up.x;
		cameraMatrix.m11 = up.y;
		cameraMatrix.m12 = up.z;
		cameraMatrix.m20 = forward.x;
		cameraMatrix.m21 = forward.y;
		cameraMatrix.m22 = forward.z;
		cameraMatrix.m30 = position.x;
		cameraMatrix.m31 = position.y;
		cameraMatrix.m32 = position.z;
		cameraMatrix.m33 = 1;
		
		Matrix4f.invert(cameraMatrix, cameraInverseMatrix);
		
		forward = forward.negate(temp);
	}
	
	public Matrix4f getInverseCameraMatrix() {
		return cameraInverseMatrix;
	}
	
	public Vector3f getForward() {
		return forward;
	}
	
	public Vector3f getRight() {
		return right;
	}
	
	public Vector3f getUp() {
		return up;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public void translate(Vector3f trans) {
		Vector3f.add(position, trans, position);
		Vector3f.add(lookAt, trans, lookAt);
		update();
	}
	
	public void setPosition(Vector3f pos) {
		Vector3f curr = new Vector3f(pos.x, pos.y, pos.z);
		Vector3f.sub(curr, getPosition(), curr);
		translate(curr);
	}
	
	public void lookAt(Vector3f point) {
		lookAt = new Vector3f(point);
		update();
	}
	
	public void setWorldUp(Vector3f wu) {
		worldUp = new Vector3f(wu);
		update();
	}
	
	public void input(float dt) {
		float speed = 15f;
		float rotspeed = 2f;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			Vector3f newVec = new Vector3f(forward.x * speed * dt, forward.y * speed * dt, forward.z * speed * dt);
			translate(newVec);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			Vector3f newVec = new Vector3f(forward.x * -speed * dt, forward.y * -speed * dt, forward.z * -speed * dt);
			translate(newVec);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			Vector3f newVec = new Vector3f(right.x * -speed * dt, right.y * -speed * dt, right.z * -speed * dt);
			translate(newVec);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			Vector3f newVec = new Vector3f(right.x * speed * dt, right.y * speed * dt, right.z * speed * dt);
			translate(newVec);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			Matrix4f rotation = new Matrix4f();
			Matrix4f.translate(position, rotation, rotation);
			Matrix4f.rotate(rotspeed * dt, Maths.Y_AXIS, rotation, rotation);
			Matrix4f.translate(new Vector3f(-position.x, -position.y, -position.z), rotation, rotation);
			Matrix4f.transform(rotation, new Vector4f(lookAt.x, lookAt.y, lookAt.z, 1), tempv4);
			lookAt.set(tempv4.x, tempv4.y, tempv4.z);
			Matrix4f.transform(rotation, new Vector4f(worldUp.x, worldUp.y, worldUp.z, 0), tempv4);
			worldUp.set(tempv4.x, tempv4.y, tempv4.z);
			update();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			Matrix4f rotation = new Matrix4f();
			Matrix4f.translate(position, rotation, rotation);
			Matrix4f.rotate(-rotspeed * dt, Maths.Y_AXIS, rotation, rotation);
			Matrix4f.translate(new Vector3f(-position.x, -position.y, -position.z), rotation, rotation);
			Matrix4f.transform(rotation, new Vector4f(lookAt.x, lookAt.y, lookAt.z, 1), tempv4);
			lookAt.set(tempv4.x, tempv4.y, tempv4.z);
			Matrix4f.transform(rotation, new Vector4f(worldUp.x, worldUp.y, worldUp.z, 0), tempv4);
			worldUp.set(tempv4.x, tempv4.y, tempv4.z);
			update();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			Matrix4f rotation = new Matrix4f();
			Matrix4f.translate(position, rotation, rotation);
			Matrix4f.rotate(rotspeed * dt, right, rotation, rotation);
			Matrix4f.translate(new Vector3f(-position.x, -position.y, -position.z), rotation, rotation);
			Matrix4f.transform(rotation, new Vector4f(lookAt.x, lookAt.y, lookAt.z, 1), tempv4);
			lookAt.set(tempv4.x, tempv4.y, tempv4.z);
			Matrix4f.transform(rotation, new Vector4f(worldUp.x, worldUp.y, worldUp.z, 0), tempv4);
			worldUp.set(tempv4.x, tempv4.y, tempv4.z);
			update();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			Matrix4f rotation = new Matrix4f();
			Matrix4f.translate(position, rotation, rotation);
			Matrix4f.rotate(-rotspeed * dt, right, rotation, rotation);
			Matrix4f.translate(new Vector3f(-position.x, -position.y, -position.z), rotation, rotation);
			Matrix4f.transform(rotation, new Vector4f(lookAt.x, lookAt.y, lookAt.z, 1), tempv4);
			lookAt.set(tempv4.x, tempv4.y, tempv4.z);
			Matrix4f.transform(rotation, new Vector4f(worldUp.x, worldUp.y, worldUp.z, 0), tempv4);
			worldUp.set(tempv4.x, tempv4.y, tempv4.z);
			update();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_V)) {
			Vector3f newVec = new Vector3f(up.x * speed * dt, up.y * speed * dt, up.z * speed * dt);
			translate(newVec);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_X)) {
			Vector3f newVec = new Vector3f(up.x * -speed * dt, up.y * -speed * dt, up.z * -speed * dt);
			translate(newVec);
		}
	}
}
