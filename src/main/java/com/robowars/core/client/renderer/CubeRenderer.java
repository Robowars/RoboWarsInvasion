package com.robowars.core.client.renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.util.vector.Matrix4f;

import com.sun.xml.internal.ws.message.ByteArrayAttachment;

import scala.actors.threadpool.Arrays;

/**Author: Khlorghaal
 * Public Domain, attribution preferred*/
public class CubeRenderer {
	static int program;
	static int attlocpos, ulocppos, ulocmvp, uloccolors, ulocscale;//binding points

	//using literals, sue me
	static String vshsrc= 
			"#version 150 core\n"//don't forget \n with preprocessor
			+ "uniform mat4 mvp;"
			+ "uniform samplerBuffer ppos;"
			+ "uniform samplerBuffer colors;"
			+ "uniform float scale;"
			+ ""
			+ "in vec3 posin;"
			+ "flat out vec4 color;"
			+ "void main(){"
			+ ""
			+ "int x= gl_InstanceID*3+0;"//because bufftexes cant rgb32f
			+ "int y= gl_InstanceID*3+1;"
			+ "int z= gl_InstanceID*3+2;"
			+ "vec3 offset= vec3( "
			+ " texelFetch(ppos, x).x, "
			+ " texelFetch(ppos, y).x, "
			+ " texelFetch(ppos, z).x"
			+ ");"
			+ "gl_Position= mvp*vec4(posin*scale+offset, 1);"
			+ ""
			+ "color= texelFetch(colors, gl_InstanceID);"
			+ "}";
	static String fshsrc= 
			"#version 150 core\n"
					+ "flat in vec4 color;"
					+ "out vec4 colorout;"
					+ "void main(){"
					+ ""
					+ "colorout= color;"
					+ ""
					+ "}";

	static ByteBuffer buf= BufferUtils.createByteBuffer(512*4);
	static int VBO, EBO, VAO;//of a regular cube, particle positions are in the buffer texture
	static final int POS_SIZE= 3*4;
	static byte[] VERTS, INDICIES;
	static{
		program= GL20.glCreateProgram();
		int vsh= GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		int fsh= GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		GL20.glShaderSource(vsh, vshsrc);
		GL20.glShaderSource(fsh, fshsrc);
		GL20.glCompileShader(vsh);
		GL20.glCompileShader(fsh);
		String verr= GL20.glGetShaderInfoLog(vsh, 512); if(!verr.equals(""))System.err.println("_____\nVSH ERROR\n"+verr);
		String ferr= GL20.glGetShaderInfoLog(fsh, 512); if(!ferr.equals(""))System.err.println("_____\nFSH ERROR\n"+ferr);
		GL20.glAttachShader(program, vsh);
		GL20.glAttachShader(program, fsh);
		GL20.glLinkProgram(program);
		String perr= GL20.glGetProgramInfoLog(program, 512); if(!perr.equals(""))System.err.println(perr);

		attlocpos= GL20.glGetAttribLocation(program, "posin");
		ulocppos= GL20.glGetUniformLocation(program, "ppos");
		uloccolors= GL20.glGetUniformLocation(program, "colors");
		ulocmvp= GL20.glGetUniformLocation(program, "mvp");
		ulocscale= GL20.glGetUniformLocation(program, "scale");

		GL20.glUseProgram(program);
		GL20.glUniform1i(ulocppos, 0);//sampler unis only needs bound once
		GL20.glUniform1i(uloccolors, 1);
		GL20.glUseProgram(0);

		//setup cube vbo, ebo
		VERTS= new byte[]{//since scaled by uniforms, store as unnormalized bytes. Dat bandwidth tho.
			 	 1, 1, 1,//+++ 0
				 1, 1,-1,//++- 1
				 1,-1, 1,//+-+ 2
				 1,-1,-1,//+-- 3
				-1, 1, 1,//-++ 4
				-1, 1,-1,//-+- 5
				-1,-1, 1,//--+ 6
				-1,-1,-1 //--- 7
				//cube faces
				//-z
				//51
				//73
				//+z
				//40
				//62
		};
		INDICIES= new byte[]{//DAT BANDWIDTH THO L:
				//from the matching sign of axis-columns of ^
				//swizzled for correct cull order
				//wy  ccw zyxxyw
				//xz  cw  zxyyxw
				6,4,7,7,4,5,//x- 7465 647745
				2,3,0,0,3,1,//x+      674475 -4
				3,2,7,7,2,6,//y- 7236 327726
				1,5,0,0,5,4,//y+      372276 -2
				7,5,3,3,5,1,//z- 3571 753351
				6,2,4,4,2,0 //z+      735531 -1
				//that is fucking beautiful
		};


		VBO= GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO);
		buf.clear();
		buf.put(VERTS);
		buf.flip();
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buf, GL15.GL_STATIC_DRAW);

		EBO= GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, EBO);
		buf.clear();
		buf.put(INDICIES);
		buf.flip();
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buf, GL15.GL_STATIC_DRAW);
		
		buf.clear();

		VAO= GL30.glGenVertexArrays();
		GL30.glBindVertexArray(VAO);
		GL20.glEnableVertexAttribArray(attlocpos);
		//vbo already bound
		GL20.glVertexAttribPointer(attlocpos, 3, GL11.GL_BYTE, false, 3, 0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, EBO);//must rebind ebo because vao inits to 0
		GL30.glBindVertexArray(0);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	int postex, colortex, posbuf, colorbuf;//storing particle positions
	int maxcubes;
	/**Must only be constructed from render thread*/
	public CubeRenderer(int maxcubes){
		this.maxcubes= maxcubes;
		if(buf.capacity()<maxcubes*POS_SIZE){
			buf= BufferUtils.createByteBuffer(maxcubes*POS_SIZE);
		}

		//buffer texture buffer allocation
		posbuf= GL15.glGenBuffers();
		GL15.glBindBuffer(GL31.GL_TEXTURE_BUFFER, posbuf);
		GL15.glBufferData(GL31.GL_TEXTURE_BUFFER, maxcubes*POS_SIZE, GL15.GL_DYNAMIC_DRAW);
		
		colorbuf= GL15.glGenBuffers();
		GL15.glBindBuffer(GL31.GL_TEXTURE_BUFFER, colorbuf);
		GL15.glBufferData(GL31.GL_TEXTURE_BUFFER, maxcubes*4, GL15.GL_DYNAMIC_DRAW);

		GL15.glBindBuffer(GL31.GL_TEXTURE_BUFFER, 0);
		
		//buffer texture setup
		GL11.glPushAttrib(GL11.GL_TEXTURE_BIT);

		postex= GL11.glGenTextures();
		GL11.glBindTexture(GL31.GL_TEXTURE_BUFFER, postex);
		GL31.glTexBuffer(GL31.GL_TEXTURE_BUFFER, GL30.GL_R32F, posbuf);
		
		colortex= GL11.glGenTextures();
		GL11.glBindTexture(GL31.GL_TEXTURE_BUFFER, colortex);
		GL31.glTexBuffer(GL31.GL_TEXTURE_BUFFER, GL11.GL_RGBA8, colorbuf);
		
		GL11.glBindTexture(GL31.GL_TEXTURE_BUFFER, 0);
		
		
		
		
		GL11.glPopAttrib();
	}

	public float scale= .9f;
	public void render(float[] particles){  render(particles, null);  }
	public void render(float[] particles, byte[] colors){		
		if(particles.length/POS_SIZE>maxcubes){
			return;
		}

		GL11.glPushAttrib(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glPushAttrib(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL20.glUseProgram(program);

		ShaderUtil.useLegacyMVP(ulocmvp);
		GL20.glUniform1f(ulocscale, scale/2);

		pushPositions(particles);
		if(colors!=null)
			pushColors(colors);

		GL11.glPushAttrib(GL11.GL_TEXTURE);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL31.GL_TEXTURE_BUFFER, colortex);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL31.GL_TEXTURE_BUFFER, postex);

		GL30.glBindVertexArray(VAO);
		GL31.glDrawElementsInstanced(GL11.GL_TRIANGLES, INDICIES.length, GL11.GL_UNSIGNED_BYTE, 0, particles.length/3);
		GL30.glBindVertexArray(0);

		GL20.glUseProgram(0);

		GL11.glPopAttrib();
		GL11.glPopAttrib();
		GL11.glPopAttrib();
	}
	
	public void pushPositions(float[] positions){
		buf.clear();
		FloatBuffer fbuf= buf.asFloatBuffer();
		fbuf.put(positions);
		fbuf.flip();
		GL15.glBindBuffer(GL31.GL_TEXTURE_BUFFER, posbuf);
		GL15.glBufferSubData(GL31.GL_TEXTURE_BUFFER, 0, fbuf);
	}
	public void pushColors(byte[] colors){
		buf.clear();
		buf.put(colors);
		buf.flip();
		GL15.glBindBuffer(GL31.GL_TEXTURE_BUFFER, colorbuf);
		GL15.glBufferSubData(GL31.GL_TEXTURE_BUFFER, 0, buf);	
	}
	public void fillColor(int color){
		int r= (color&0xFF000000)>>>24;
		int g= (color&0x00FF0000)>>>16;
		int b= (color&0x0000FF00)>>>8;
		int a= (color&0x000000FF)>>>0;
		buf.clear();
		for(int i=0; i!=maxcubes; i++){
			buf.put((byte)r);
			buf.put((byte)g);
			buf.put((byte)b);
			buf.put((byte)a);
		}
		buf.flip();
		GL15.glBindBuffer(GL31.GL_TEXTURE_BUFFER, colorbuf);
		GL15.glBufferSubData(GL31.GL_TEXTURE_BUFFER, 0, buf);
	}
}