package com.robowars.core.client.renderer;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;

import com.google.common.eventbus.Subscribe;

/**Author: Khlorghaal
 * Public Domain, attribution preferred*/
public class CubeRenderer {
	static int program;
	static int attlocpos, ulocmvp, uloccolor, ulocscale;//binding points
	
	//using literals, sue me
	static String vshsrc= 
			"#version 150 core\n"//don't forget \n with preprocessor
			+ "uniform mat4 mvp;"
			+ ""
			+ "in vec3 posin;"
			+ "out vec3 posg;"
			+ "out float sizeg;"
			+ "void main(){"
			+ "posg= posin;"
			+ "}";
	static String gshsrc= 
			"#version 150 core\n"
			+ "vec3 corners[8]= vec3[]( "
			+ "vec3( 1, 1, 1),"//+++
			+ "vec3( 1, 1,-1),"//++-
			+ "vec3( 1,-1, 1),"//+-+
			+ "vec3( 1,-1,-1),"//+--
			+ "vec3(-1, 1, 1),"//-++
			+ "vec3(-1, 1,-1),"//-+-
			+ "vec3(-1,-1, 1),"//--+
			+ "vec3(-1,-1,-1) "//---
			+ ");"
			+ "int cornerIndicies[6*4]= int[]("
			+ "0,2,1,3,"//x-  from the matching sign of axis-columns of ^
			+ "4,5,6,7,"//x+  some rows rearranged for correct culling order, via xzyw swizzle
			+ "2,6,3,7,"//y-  
			+ "0,1,4,5,"//y+  This is one of my favorite examples of beautiful code
			+ "1,3,5,7,"//z-  
			+ "0,4,2,6"//z+
			+ ");"
			+ ""
			+ "uniform mat4 mvp;"
			+ "uniform float scale;"
			+ ""
			+ "layout(points) in;"
			+ "in vec3 posg[];"
			+ ""
			+ "layout(triangle_strip, max_vertices= 24) out;"
			+ "void main(){"
			+ "vec3 pos= vec3(posg[0]);"
			+ "for(int i=0; i!=6; i++){"//faces
			+ "for(int j=0; j!=4; j++){"//corners
			+ "gl_Position= mvp*vec4(pos+corners[cornerIndicies[i*4+j]]*scale, 1);"
			+ "EmitVertex();"
			+ "}"
			+ "EndPrimitive();"
			+ "}"
			+ ""
			+ "}";
	static String fshsrc= 
			"#version 150 core\n"
			+ "uniform vec4 color;"
			+ ""
			+ "out vec4 colorout;"
			+ "void main(){"
			+ "colorout= color;"
			+ "}";
	
	static{
		program= GL20.glCreateProgram();
		int vsh= GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		int gsh= GL20.glCreateShader(GL32.GL_GEOMETRY_SHADER);
		int fsh= GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		GL20.glShaderSource(vsh, vshsrc);
		GL20.glShaderSource(gsh, gshsrc);
		GL20.glShaderSource(fsh, fshsrc);
		GL20.glCompileShader(vsh);
		GL20.glCompileShader(gsh);
		GL20.glCompileShader(fsh);
		String verr= GL20.glGetShaderInfoLog(vsh, 512); if(!verr.equals(""))System.err.println("_____\nVSH ERROR\n"+verr);
		String gerr= GL20.glGetShaderInfoLog(gsh, 512); if(!gerr.equals(""))System.err.println("_____\nGSH ERROR\n"+gerr);
		String ferr= GL20.glGetShaderInfoLog(fsh, 512); if(!ferr.equals(""))System.err.println("_____\nFSH ERROR\n"+ferr);
		GL20.glAttachShader(program, vsh);
		GL20.glAttachShader(program, gsh);
		GL20.glAttachShader(program, fsh);
		GL20.glLinkProgram(program);
		String perr= GL20.glGetProgramInfoLog(program, 512); if(!perr.equals(""))System.err.println(perr);

		attlocpos= GL20.glGetAttribLocation(program, "posin");
		ulocmvp= GL20.glGetUniformLocation(program, "mvp");
		ulocscale= GL20.glGetUniformLocation(program, "scale");
		uloccolor= GL20.glGetUniformLocation(program, "color");
		
	}
	
	static ByteBuffer buf= BufferUtils.createByteBuffer(512*4);
	
	int vbo, vao;
	int maxcubes;
	/**Must only be constructed from render thread*/
	public CubeRenderer(int maxcubes){
		this.maxcubes= maxcubes;
		
		vbo= GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buf, GL15.GL_DYNAMIC_DRAW);
		
		vao= GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);
		//vbo already bound
		GL20.glEnableVertexAttribArray(attlocpos);
		GL20.glVertexAttribPointer(attlocpos, 3, GL11.GL_FLOAT, false, 3*4, 0);
		GL30.glBindVertexArray(0);//must unbind
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		System.err.println(GLU.gluErrorString(GL11.glGetError()));
	}
	
	public float[] color= new float[]{0,1,1,.5f};
	public float scale= .2f;
	public void render(float[] particles){
		GL11.glPushAttrib(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		
		GL20.glUseProgram(program);
		
		useLegacyMVP(ulocmvp);
		GL20.glUniform4f(uloccolor, color[0], color[1], color[2], color[3]);
		GL20.glUniform1f(ulocscale, scale/2);

//		GL30.glBindVertexArray(vao);
//		GL11.glDrawArrays(GL11.GL_POINTS, 0, maxcubes);
		
		GL11.glBegin(GL11.GL_POINTS);//IMMEDIATE MOOODEEEE DESPAAIIRRRRRRRRRR
		for(int i= 0; i!= particles.length;){
			GL11.glVertex3f(particles[i++], particles[i++], particles[i++]);
		}
		GL11.glEnd();
		
		GL20.glUseProgram(0);
		
		GL11.glPopAttrib();
	}
	
	private static FloatBuffer matbuf= BufferUtils.createFloatBuffer(16);
	/**Must be called when the matrix is in the appropriate state, generally does not work with UI rendering*/
	public static void useLegacyMVP(int uniformLocation){
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
		mvp.store(matbuf);
		matbuf.clear();
		GL20.glUniformMatrix4(uniformLocation, false, matbuf);
		
		getLightColor();
	}
	
	public static float[] getLightColor(){
		float r=0;
		float g=0;
		float b=0;
		return new float[]{r,g,b};
	}
	
	
}
