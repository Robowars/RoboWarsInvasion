package com.robowars.core.client.renderer;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;

public class ShaderUtil {
	public static FloatBuffer matbuf= BufferUtils.createFloatBuffer(16);
	public static void uniformMatrix4f(int uloc, Matrix4f mat){
		matbuf.clear();
		mat.store(matbuf);
		matbuf.clear();
		GL20.glUniformMatrix4(uloc, false, matbuf);
		
	}
	public static Matrix4f getLegacyMVP(){
		matbuf.clear();
		Matrix4f mv= new Matrix4f();
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, matbuf);
		mv.load(matbuf);		
		matbuf.clear();
		Matrix4f p= new Matrix4f();
		GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, matbuf);
		p.load(matbuf);
		matbuf.clear();

		Matrix4f mvp= new Matrix4f();
		Matrix4f.mul(p, mv, mvp);
		return mvp;
	}
	/**Must be called when the matrix is in the appropriate state, generally does not work with UI rendering*/
	public static void useLegacyMVP(int uniformLocation){
		uniformMatrix4f(uniformLocation, getLegacyMVP());
	}

	public static float[] getLightColor(){
		float r=0;
		float g=0;
		float b=0;
		return new float[]{r,g,b};
	}
	public static void uniform4fMCLight(int uloc){
		
	}
	
	public static void fillColor(byte[] colors, int colorbase) {
		byte r= (byte)((colorbase&0xff000000)>>>24);
		byte g= (byte)((colorbase&0x00ff0000)>>>16);
		byte b= (byte)((colorbase&0x0000ff00)>>> 8);
		byte a= (byte)((colorbase&0x000000ff)>>> 0);
		for(int i=0; i!=colors.length;){
			colors[i++]= r;
			colors[i++]= g;
			colors[i++]= b;
			colors[i++]= a;
		}
	}
}
