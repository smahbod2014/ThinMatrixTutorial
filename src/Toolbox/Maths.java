package Toolbox;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class Maths {

	public static final Vector3f X_AXIS = new Vector3f(1, 0, 0);
	public static final Vector3f Y_AXIS = new Vector3f(0, 1, 0);
	public static final Vector3f Z_AXIS = new Vector3f(0, 0, 1);
	public static final Matrix4f IDENTITY = new Matrix4f();
	
	private static final Vector3f tempScale = new Vector3f(1, 1, 1);
	
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
		tempScale.set(scale, scale, scale);
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(rx), X_AXIS, matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(ry), Y_AXIS, matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(rz), Z_AXIS, matrix, matrix);
		Matrix4f.scale(tempScale, matrix, matrix);
		return matrix;
	}
	
	public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f axis, float degrees, float scale) {
		tempScale.set(scale, scale, scale);
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.mul(matrix, rotation(axis, degrees), matrix);
		Matrix4f.scale(tempScale, matrix, matrix);
		return matrix;
	}
	
	public static Matrix4f createOrthographicProjection(float left, float right, float bottom, float top, float near, float far) {
		Matrix4f matrix = new Matrix4f();
//		matrix.m00 = 2f / (right - left);
//		matrix.m11 = 2f / (top - bottom);
//		matrix.m22 = -2f / (far - near);
//		matrix.m30 = -(right + left) / (right - left);
//		matrix.m31 = -(top + bottom) / (top - bottom);
//		matrix.m32 = -(far + near) / (far - near);

		matrix.m00 = 2f / (right - left);
		matrix.m11 = 2f / (top - bottom);
		matrix.m22 = 2f / (far - near);
		matrix.m30 = -(right + left) / (right - left);
		matrix.m31 = -(top + bottom) / (top - bottom);
		matrix.m32 = -(far + near) / (far - near);
		return matrix;
	}
	
	public static Matrix4f createPerspectiveProjection(float fov, float aspect, float near, float far) {
		Matrix4f matrix = new Matrix4f();
		float tan = (float) Math.tan(Math.toRadians(fov / 2f));
		
		matrix.m00 = 1f / (aspect * tan);
		matrix.m11 = 1f / tan;
		matrix.m22 = (-far - near) / (far - near);
		matrix.m23 = -1f;
		matrix.m32 = (-2f * near * far) / (far - near);
		matrix.m33 = 0;
		return matrix;
	}
	
	public static Matrix4f rotation(Vector3f axis, float degrees) {
		Matrix4f matrix = new Matrix4f();
		
		if (axis.lengthSquared() != 0)
			axis.normalise();
		
		float c = (float) Math.cos(Math.toRadians(degrees));
		float s = (float) Math.sin(Math.toRadians(degrees));
		float t = 1f - c;

		matrix.m00 = t*axis.getX()*axis.getX() + c;
		matrix.m01 = t*axis.getX()*axis.getY() + s*axis.getZ();
		matrix.m02 = t*axis.getX()*axis.getZ() - s*axis.getY();

		matrix.m10 = t*axis.getX()*axis.getY() - s*axis.getZ();
		matrix.m11 = t*axis.getY()*axis.getY() + c;
		matrix.m12 = t*axis.getY()*axis.getZ() + s*axis.getX();

		matrix.m20 = t*axis.getX()*axis.getZ() + s*axis.getY();
		matrix.m21 = t*axis.getY()*axis.getZ() - s*axis.getX();
		matrix.m22 = t*axis.getZ()*axis.getZ() + c;
		
		return matrix;
	}
	
	public static Matrix4f translation(float x, float y, float z) {
		Matrix4f matrix = new Matrix4f();
		
		matrix.m30 = x;
		matrix.m31 = y;
		matrix.m32 = z;
		
		return matrix;
	}
	
	public static Matrix4f translation(Vector3f amount) {
		return translation(amount.x, amount.y, amount.z);
	}
	
	public static Matrix4f scale(float sx, float sy, float sz) {
		Matrix4f matrix = new Matrix4f();
		
		matrix.m00 = sx;
		matrix.m11 = sy;
		matrix.m22 = sz;
		
		return matrix;
	}
	
	public static Matrix4f identity() {
		return new Matrix4f();
	}
	
	public static Vector4f toVec40(Vector3f vec) {
		return new Vector4f(vec.x, vec.y, vec.z, 0);
	}
	
	public static Vector4f toVec41(Vector3f vec) {
		return new Vector4f(vec.x, vec.y, vec.z, 1);
	}
	
	public static Vector3f toVec3(Vector4f vec) {
		return new Vector3f(vec.x, vec.y, vec.z);
	}
	
	public static List<Vector3f> piecewiseBezier(List<Vector3f> cps, int degree, int segmentsPerCurve) {
		if ((cps.size() - 1) % degree != 0) {
			System.out.println("Invalid number of control points: " + (cps.size() - 1));
			return null;
		}
		
		List<Vector3f> segments = new ArrayList<Vector3f>();
		for (int i = 0; i < cps.size() - 1; i += degree)
			segments.addAll(bezier(cps, degree, i, segmentsPerCurve));
		
		while (!checkC1Continuity(cps, degree)) {
			segments.clear();
			segments.addAll(piecewiseBezier(cps, degree, segmentsPerCurve));
		}
		
		return segments;
	}
	
	public static List<Vector3f> bezier(List<Vector3f> cps, int degree, int offset, int segmentsPerCurve) {
		float granularity = 1f / segmentsPerCurve;
		
		List<Vector3f> segments = new ArrayList<Vector3f>();
		for (float t = 0; t <= 1; t += granularity) {
			Vector3f x = new Vector3f();
			for (int i = 0; i <= degree; i++) {
				float B = B(degree, i, t);
				Vector3f cp = new Vector3f(cps.get(i + offset));
				Vector3f.add(x, (Vector3f) cp.scale(B), x);
			}
			
			segments.add(x);
		}
		
		return segments;
	}
	
	private static float B(int n, int i, float t) {
		return choose(n, i) * (float) Math.pow(1f - t, n - i) * (float) Math.pow(t, i);
	}
	
	public static int factorial(int n) {
		if (n <= 1)
			return 1;
		return n * factorial(n - 1);
	}
	
	public static int choose(int n, int i) {
		return factorial(n) / (factorial(i) * factorial(n - i));
	}
	
	private static boolean checkC1Continuity(List<Vector3f> cps, int degree) {
		boolean ok = true;
		Vector3f tangent1 = new Vector3f();
		Vector3f tangent2 = new Vector3f();
		
		for (int i = degree; i <= cps.size() - 1 - degree; i += degree) {
			Vector3f p1 = cps.get(i - 1);
			Vector3f p2 = cps.get(i);
			Vector3f p3 = cps.get(i + 1);
			
			Vector3f.sub(p2, p1, tangent1);
			Vector3f.sub(p3, p2, tangent2);
			
			float dist1 = tangent1.length();
			float dist2 = tangent2.length();
			
			tangent1.normalise();
			tangent2.normalise();
			
//			if (selected == i) {
//				Vector2 displacement = p2.cpy().sub(oldControlPoints.get(i));
//				p1.add(displacement);
//				p3.add(displacement);
//				oldControlPoints.get(i).set(p2);
//			}
			
			if (Vector3f.dot(tangent1, tangent2) < .99999f) {
				ok = false;
				int selected = i - 1;
				if (selected == i - 1) {
					Vector3f.add(p2, (Vector3f) tangent1.scale(dist2), p3);
				} else if (selected == i + 1) {
					Vector3f.sub(p2, (Vector3f) tangent2.scale(dist1), p1);
				}
			}
		}
		
		return ok;
	}
	
	public static List<Vector3f> bezierTangents(List<Vector3f> segments) {
		List<Vector3f> tangents = new ArrayList<Vector3f>();
		
		for (int i = 0; i < segments.size() - 1; i++) {
			Vector3f tangent = new Vector3f();
			Vector3f.sub(segments.get(i + 1), segments.get(i), tangent);
			tangent.normalise();
			tangents.add(tangent);
		}
		
		//temporary
		Vector3f last = new Vector3f(tangents.get(tangents.size() - 1));
		tangents.add(last);
		
		return tangents;
	}
}
