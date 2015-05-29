package Entities;

import org.lwjgl.util.vector.Vector3f;

public class Light extends Entity {

	public Vector3f color;
	public float constant;
	public float linear;
	public float quadratic;
	
	public Light(Vector3f position, Vector3f color, float constant, float linear, float quadratic) {
		super(null, position, new Vector3f(0, 1, 0), 0, 1, 1, 1);
		this.color = color;
		this.constant = constant;
		this.linear = linear;
		this.quadratic = quadratic;
	}
}
