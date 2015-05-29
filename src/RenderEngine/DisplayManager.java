package RenderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {

	private static final int FPS = 120;
	
	public static void createDisplay(int width, int height) {
//		ContextAttribs attribs = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);
//		attribs.withForwardCompatible(true);
//		attribs.withProfileCore(true);
		
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
//			Display.create(new PixelFormat(), attribs);
			Display.create(new PixelFormat());
			Display.setTitle("ThinMatrixTutorial");
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(GL11.glGetString(GL11.GL_VERSION));
		
		GL11.glViewport(0, 0, width, height);
//		GL11.glViewport(400, 0, 400, 300);
	}
	
	public static void updateDisplay() {
		Display.sync(FPS);
		Display.update();
	}
	
	public static void closeDisplay() {
		Display.destroy();
	}
}
