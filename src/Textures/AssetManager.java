package Textures;

import java.util.HashMap;

import Models.RawModel;
import RenderEngine.Loader;
import RenderEngine.OBJLoader;

public class AssetManager {

	private static HashMap<String, Integer> textures = new HashMap<String, Integer>();
	private static HashMap<String, RawModel> models = new HashMap<String, RawModel>();
	
	private AssetManager() {}
	
	public static void loadTexture(String alias, String path) {
		textures.put(alias, Loader.loadTexture(path));
	}
	
	public static void addTextureID(String alias, int id) {
		textures.put(alias, id);
	}
	
	public static int getTexture(String alias) {
		return textures.get(alias);
	}
	
	public static void loadModel(String alias, String path) {
		models.put(alias, OBJLoader.loadObjModel(path));
	}
	
	public static void loadModel(String alias, float[] vertices, float[] texcoords, float[] normals, int[] indices) {
		models.put(alias, Loader.loadToVao(vertices, texcoords, normals, indices));
	}
	
	public static RawModel getModel(String alias) {
		return models.get(alias);
	}
}
