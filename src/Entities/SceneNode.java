package Entities;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Matrix4f;

import RenderEngine.MasterRenderer;

public class SceneNode {

	List<SceneNode> children;
	
	public SceneNode() {
		children = new ArrayList<SceneNode>();
	}
	
	public void draw(MasterRenderer renderer, Matrix4f transformation) {
		for (SceneNode sn : children)
			sn.draw(renderer, transformation);
	}
	
	public SceneNode addChild(SceneNode child) {
		children.add(child);
		return this;
	}
}
