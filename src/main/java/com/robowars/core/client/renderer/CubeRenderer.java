package com.robowars.core.client.renderer;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import net.minecraft.entity.EntityLiving;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL41;
import org.lwjgl.opengl.GL42;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;

/**Author: Khlorghaal
 * Public Domain, attribution preferred*/
public class CubeRenderer {
	static int program;
	static int attlocpos, ulocppos, ulocmvp, uloccolor, ulocscale;//binding points

	//using literals, sue me
	static String vshsrc= 
			"#version 150 core\n"//don't forget \n with preprocessor
			+ "uniform mat4 mvp;"
			+ "uniform samplerBuffer ppos;"
			+ "uniform float scale;"
			+ ""
			+ "in vec3 posin;"
			+ "void main(){"
			+ "vec3 offset= texelFetch(ppos, gl_InstanceID).xyz;"
			+ "gl_Position= mvp*vec4(posin*scale+offset, 1);"
			+ "}";
	static String fshsrc= 
			"#version 150 core\n"
					+ "uniform vec4 color;"
					+ ""
					+ "out vec4 colorout;"
					+ "void main(){"
					+ "colorout= color;"
					+ "}";

	static ByteBuffer buf= BufferUtils.createByteBuffer(512*4);
	static int VBO, EBO, VAO;//of a regular cube, particle positions are in the buffer texture
	static final int ATTRIB_SIZE= 3*4;
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
		ulocmvp= GL20.glGetUniformLocation(program, "mvp");
		ulocscale= GL20.glGetUniformLocation(program, "scale");
		uloccolor= GL20.glGetUniformLocation(program, "color");

		GL20.glUseProgram(program);
		GL20.glUniform1i(ulocppos, 0);//sampler uni only needs bound once
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
		buf.put(VERTS);
		buf.flip();
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buf, GL15.GL_STATIC_DRAW);
		buf.clear();

		EBO= GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, EBO);
		buf.put(INDICIES);
		buf.flip();
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buf, GL15.GL_STATIC_DRAW);
		buf.clear();

		//setup VAO
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


	int buftex, texbuf;//storing particle positions
	int maxcubes;
	/**Must only be constructed from render thread*/
	public CubeRenderer(int maxcubes){
		this.maxcubes= maxcubes;
		if(buf.capacity()<maxcubes*ATTRIB_SIZE){
			buf= BufferUtils.createByteBuffer(maxcubes*ATTRIB_SIZE);
		}

		texbuf= GL15.glGenBuffers();
		GL15.glBindBuffer(GL31.GL_TEXTURE_BUFFER, texbuf);
		buf.position(maxcubes*ATTRIB_SIZE);
		buf.flip();
		GL15.glBufferData(GL31.GL_TEXTURE_BUFFER, buf, GL15.GL_DYNAMIC_DRAW);
		buf.clear();

		GL11.glPushAttrib(GL11.GL_TEXTURE_BIT);
		buftex= GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL31.GL_TEXTURE_BUFFER, buftex);
		GL31.glTexBuffer(GL31.GL_TEXTURE_BUFFER, GL30.GL_RGB32F, texbuf);
		GL11.glPopAttrib();

		GL15.glBindBuffer(GL31.GL_TEXTURE_BUFFER, 0);
		GL11.glBindTexture(GL31.GL_TEXTURE_BUFFER, 0);
	}

	public float[] color= new float[]{0,0,1,.7f};
	public float scale= .9f;
	public void render(float[] particles){		
		if(particles.length/ATTRIB_SIZE>maxcubes){
			return;
		}

		GL11.glPushAttrib(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);

		GL20.glUseProgram(program);

		useLegacyMVP(ulocmvp);
		GL20.glUniform4f(uloccolor, color[0], color[1], color[2], color[3]);
		GL20.glUniform1f(ulocscale, scale/2);

		FloatBuffer fbuf= buf.asFloatBuffer();
		fbuf.put(particles);
		fbuf.flip();
		GL15.glBindBuffer(GL31.GL_TEXTURE_BUFFER, texbuf);
		GL15.glBufferSubData(GL31.GL_TEXTURE_BUFFER, 0, fbuf);
		buf.clear();

		GL11.glPushAttrib(GL11.GL_TEXTURE);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL31.GL_TEXTURE_BUFFER, buftex);

		GL30.glBindVertexArray(VAO);
		GL31.glDrawElementsInstanced(GL11.GL_TRIANGLES, INDICIES.length, GL11.GL_UNSIGNED_BYTE, 0, particles.length/3);
		GL30.glBindVertexArray(0);


		GL11.glBindTexture(GL31.GL_TEXTURE_BUFFER, 0);
		GL20.glUseProgram(0);

		GL11.glPopAttrib();
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
