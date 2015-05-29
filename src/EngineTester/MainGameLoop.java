package EngineTester;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import Entities.Camera;
import Entities.Entity;
import Entities.Geode;
import Entities.Light;
import Entities.Transform;
import Models.TexturedModel;
import RenderEngine.DisplayManager;
import RenderEngine.Loader;
import RenderEngine.MasterRenderer;
import RenderEngine.MyFrameBuffer;
import Shaders.ShaderProgram;
import Textures.AssetManager;
import Textures.ModelTexture;
import Toolbox.Maths;

public class MainGameLoop {

	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	
	public static void main(String[] args) {
		DisplayManager.createDisplay(WIDTH, HEIGHT);
		
		float[] groundVertices = {
				-5, 0, -5,
				-5, 0, 5,
				5, 0, 5,
				5, 0, -5
		};
		
		float[] groundTex = {
				0, 0,
				0, 1,
				1, 1,
				1, 0
		};
		
		float[] groundNormals = {
				0, 1, 0,
				0, 1, 0,
				0, 1, 0,
				0, 1, 0
		};
		
		int[] groundIndices = {
				0, 1, 2,
				2, 3, 0
		};
		
		ShaderProgram shader = new ShaderProgram();
		Camera camera = new Camera(new Vector3f(0, 7, 7), new Vector3f(0, 0, 0), new Vector3f(0, 1, 0));
		
		Light light = new Light(new Vector3f(0, 7, 5), new Vector3f(1, 1, 1), 1, 0, .00001f);
		ShaderProgram shadowShader = new ShaderProgram("res/shaders/ShadowShaderVert.txt", "res/shaders/ShadowShaderFrag.txt");
		Camera lightCamera = new Camera(new Vector3f(0, 7, 5), new Vector3f(0, 0, 0), Maths.Y_AXIS);
		Matrix4f orthographic = Maths.createOrthographicProjection(-10, 10, -10, 10, -10, 20);
		Matrix4f bias = new Matrix4f();
		bias.m00 = bias.m11 = bias.m22 = bias.m30 = bias.m31 = bias.m32 = .5f;
		MasterRenderer shadowRenderer = new MasterRenderer(shadowShader);
		shadowRenderer.setProjectionMatrix(orthographic);
		shadowShader.bind();
		shadowShader.setUniformMatrix4("biasMatrix", bias);
		shadowShader.unbind();
		
		
		AssetManager.loadTexture("wood", "res/textures/wood.png");
		AssetManager.loadTexture("green", "res/textures/green.png");
		AssetManager.loadTexture("red", "res/textures/red.png");
		AssetManager.loadTexture("white", "res/textures/white.png");
		
		MyFrameBuffer mfb = new MyFrameBuffer(1024, 1024);
		AssetManager.addTextureID("shadowTex", mfb.texTexture);
		

		shader.bind();
		shader.setUniform3("lightPosition", light.getPosition());
		shader.setUniform3("lightColor", light.color);
		shader.setUniform1("constantAttenuation", light.constant);
		shader.setUniform1("linearAttenuation", light.linear);
		shader.setUniform1("quadraticAttenuation", light.quadratic);
		shader.unbind();
		
		
		AssetManager.loadModel("sphere", "res/models/sphere.obj");
		AssetManager.loadModel("cylinder", "res/models/cylinder.obj");
		AssetManager.loadModel("cube", "res/models/cube.obj");
		AssetManager.loadModel("ground", groundVertices, groundTex, groundNormals, groundIndices);
		
		ModelTexture green = new ModelTexture(AssetManager.getTexture("green"));
		green.setReflectivity(1);
		green.setShineDamper(10);
		ModelTexture wood = new ModelTexture(AssetManager.getTexture("wood"));
		wood.setReflectivity(1);
		wood.setShineDamper(10);
		
		Entity sphere = new Entity(new TexturedModel(AssetManager.getModel("sphere"), green));
		Entity cylinder = new Entity(new TexturedModel(AssetManager.getModel("cylinder"), green));
		Entity cube = new Entity(new TexturedModel(AssetManager.getModel("cube"), green));
		Entity ground = new Entity(new TexturedModel(AssetManager.getModel("ground"), wood));
		
		Transform root = new Transform();
		Transform propellorRoot = new Transform();
		Transform propellorBase = new Transform();
		Transform propellorBase2 = new Transform();
		Transform axisScale = new Transform(Maths.scale(.2f, .5f, .2f));
		Transform bladeScale1 = new Transform(Maths.scale(1.25f, .05f, .33f));
		Transform baseScale = new Transform(Maths.scale(.6f, .6f, .6f));
		Transform topScale = new Transform(Maths.scale(.4f, .4f, .4f));
		Transform axisOffset = new Transform(Maths.translation(0, 1, 0));
		Transform topOffset = new Transform(Maths.translation(0, .75f, 0));
		Transform bladeOffset1 = new Transform();
		Transform bladeOffset2 = new Transform(Maths.rotation(Maths.Y_AXIS, 120));
		Transform bladeOffset3 = new Transform(Maths.rotation(Maths.Y_AXIS, 240));
		Transform bladeTrans1 = new Transform(Maths.translation(1.25f, 0, 0));
		Transform spin = new Transform();
		Geode axis = new Geode(cylinder);
		Geode blade = new Geode(cube);
		Geode base = new Geode(sphere);
		Geode floor = new Geode(ground);
		
		root.addChild(floor);
		root.addChild(propellorRoot);
		
		propellorRoot.addChild(propellorBase.addChild(propellorBase2));
		propellorBase2.addChild(baseScale.addChild(base));
		propellorBase2.addChild(spin);
		spin.addChild(axisOffset);
		axisOffset.addChild(axisScale.addChild(axis));
		axisOffset.addChild(topOffset);
		topOffset.addChild(topScale.addChild(base));
		topOffset.addChild(bladeOffset1.addChild(bladeTrans1.addChild(bladeScale1.addChild(blade))));
		topOffset.addChild(bladeOffset2.addChild(bladeTrans1));
		topOffset.addChild(bladeOffset3.addChild(bladeTrans1));
		
		propellorRoot.translate(0, 2, 0);
		
		List<Vector3f> controlPoints = new ArrayList<Vector3f>();
//		controlPoints.add(new Vector3f(-7, 0, 0));
//		controlPoints.add(new Vector3f(-2, 5, 0));
//		controlPoints.add(new Vector3f(2, -5, 0));
//		controlPoints.add(new Vector3f(7, 0, 0));
		
//		int c = 0;
//		for (int i = 0; i <= 360; i+= 30) {
//			float rad = (float) Math.toRadians(i);
//			float x = (float) Math.cos(rad) * 5;
//			float z = (float) Math.sin(rad) * 5;
//			float y = 0;
//			if (c % 3 == 0)
//				y = 3;
//			else if (c % 3 == 1)
//				y = -1;
//			else
//				y = 6;
//			c++;
//			controlPoints.add(new Vector3f(x, y, z));
//		}
		
		controlPoints.add(new Vector3f(5, 0, 0));
		controlPoints.add(new Vector3f(5, 2, 5));
		controlPoints.add(new Vector3f(2, 2, 7));
		controlPoints.add(new Vector3f(0, 2, 10));
		controlPoints.add(new Vector3f(-5, 2, 10));
		controlPoints.add(new Vector3f(-5, 2, 5));
		controlPoints.add(new Vector3f(-5, 7, 0));
		
		List<Vector3f> segments = Maths.piecewiseBezier(controlPoints, 3, 200);
		List<Vector3f> tangents = Maths.bezierTangents(segments);
		
//		propellorBase2.print();
//		propellorBase2.rotateToPoint(new Vector3f(1, 0, 1));
//		propellorBase2.rotate(Maths.X_AXIS, 90);
		
		
		int dir = 1;
		int step = 0;
		float accum = 0;
		float mult = 1f;
		MasterRenderer masterRenderer = new MasterRenderer(shader);
		float t = 0;
		float secondCounter = 0;
		long lastFrameTime = System.currentTimeMillis();
		while (!Display.isCloseRequested()) {
			long currentFrameTime = System.currentTimeMillis();
			float dt = (currentFrameTime - lastFrameTime) / 1000f;
			lastFrameTime = currentFrameTime;
			
			secondCounter += dt;
			
			if (secondCounter >= 1) {
				Display.setTitle("FPS: " + 1f / dt);
				secondCounter = 0;
			}
			
			camera.input(dt);
//			if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
//				tableTexture.setTextureID(AssetManager.getTexture("white"));
//			} else if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
//				tableTexture.setTextureID(AssetManager.getTexture("green"));
//			}
			
			//----------
			accum += dt;
			if (accum >= .01f) {
				accum -= .01f;
				step += dir;
				if (step == segments.size() - 1 && dir == 1)
					dir = -1;
				if (step == 0 && dir == -1)
					dir = 1;
			}
			
			float speed = 40 * dt;

			mult = 10;
			spin.rotate(Maths.Y_AXIS, speed * mult);
			
			floor.entity.getModel().getTexture().setReflectivity(1);
			floor.entity.getModel().getTexture().setShineDamper(10);
			
			
			mfb.bindFrameBuffer();
			shadowRenderer.render(light, lightCamera);
			root.draw(shadowRenderer, Maths.identity());
			mfb.unbindFrameBuffer();
			
//			floor.entity.getModel().getTexture().setTextureID(AssetManager.getTexture("shadowTex"));
			
			
			
			
			
//			camera.setPosition(segments.get(step));
//			camera.lookAt(new Vector3f(0, 0, 0));
//			camera.setWorldUp(Maths.Y_AXIS);

			
//			if (step != segments.size() - 1) {
//				propellorBase2.rotateToPoint(Vector3f.sub(segments.get(step + 1), segments.get(step), null));
//				propellorBase2.rotate(Maths.X_AXIS, 90);
//			}
//			propellorBase.setPosition(segments.get(step));
			masterRenderer.render(light, camera);
			root.draw(masterRenderer, Maths.identity());
			
			
			
			
			DisplayManager.updateDisplay();
			t += dt;
		}
		
		mfb.cleanUp();
		masterRenderer.clean();
		Loader.clean();
		DisplayManager.closeDisplay();
	}
}
