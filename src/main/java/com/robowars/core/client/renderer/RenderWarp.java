package com.robowars.core.client.renderer;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Queue;
import java.util.Stack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderWorldEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.robowars.core.client.renderer.entity.monster.RenderBot;

public class RenderWarp {

	static Stack<Matrix4f> stack= new Stack<Matrix4f>(); 
	public static void queue(Matrix4f mvp){
		stack.add(mvp);
	}

	static int prog, VAO, attlocpos, ulocmvp, ulocasp, tex, w,h;
	static{
		init();
	}
	static void init(){		
		String vshsrc= 
				"#version 150 core\n"
						+ "uniform mat4 mvp;"
						+ ""
						+ "in vec2 posin;"
						+ "noperspective out vec2 uvf;"
						+ "flat out vec2 center;"
						+ "flat out float r;"
						+ "void main(){"
						+ ""
						+ "gl_Position= mvp*vec4(posin,0,1);"
						+ "vec4 c= mvp*vec4(0,0,0,1);"
						+ "center= c.xy / c.w;"
						+ "r= mvp[0][0]/c.w;"
						+ "uvf= gl_Position.xy / gl_Position.w;"
						+ "}"
						;
		String fshsrc= 
				"#version 150 core\n"
						+ "uniform sampler2D backt;"
						+ "uniform vec2 asp;"
						+ ""
						+ "noperspective in vec2 uvf;"
						+ "flat in vec2 center;"
						+ "flat in float r;"
						+ "out vec4 colorout;"
						+ "void main(){"
						+ ""
						+ "vec2 uv= uvf-center;"
						+ "uv*= asp;"
						+ ""
						+ "float l= length(uv);"
						+ "if(l<r)"
						+ "  l= (l-r);"
						+ ""
						+ "uv= normalize(uv)*l;"
						+ "uv/= asp;"
						+ "uv+= center;"
						+ ""
						+ "vec3 back= textureLod(backt, uv/2+.5,0).xyz;"
						+ "colorout= vec4(back,1);"
						+ "}"
						;
		prog= GL20.glCreateProgram();
		int vsh= GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		int fsh= GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		GL20.glShaderSource(vsh, vshsrc);
		GL20.glShaderSource(fsh, fshsrc);
		GL20.glCompileShader(vsh);
		GL20.glCompileShader(fsh);
		String verr= GL20.glGetShaderInfoLog(vsh, 512); if(!verr.equals(""))System.err.println("_____\nVSH ERROR\n"+verr);
		String ferr= GL20.glGetShaderInfoLog(fsh, 512); if(!ferr.equals(""))System.err.println("_____\nFSH ERROR\n"+ferr);
		GL20.glAttachShader(prog, vsh);
		GL20.glAttachShader(prog, fsh);
		GL20.glLinkProgram(prog);
		String perr= GL20.glGetProgramInfoLog(prog, 512); if(!perr.equals(""))System.err.println(perr);

		attlocpos= GL20.glGetAttribLocation(prog, "posin");
		ulocmvp= GL20.glGetUniformLocation(prog, "mvp");
		ulocasp= GL20.glGetUniformLocation(prog, "asp");
		GL20.glUniform1i(GL20.glGetUniformLocation(prog, "backt"), 0);

		ByteBuffer buf= BufferUtils.createByteBuffer(2*2*4*4);
		buf.asFloatBuffer().put(new float[]{
				-1,-1, 0,0,
				1,-1, 1,0,
				-1, 1, 0,1,
				1, 1, 1,1
		});
		int vbo= GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buf, GL15.GL_STATIC_DRAW);

		VAO= GL30.glGenVertexArrays();
		GL30.glBindVertexArray(VAO);
		GL20.glEnableVertexAttribArray(attlocpos);
		GL20.glVertexAttribPointer(attlocpos, 2, GL11.GL_FLOAT, false, 2*2*4, 0);
		//		GL20.glEnableVertexAttribArray(attlocuv);
		//		GL20.glVertexAttribPointer(attlocuv, 2, GL11.GL_FLOAT, false, 2*2*4, 2*4);
		GL30.glBindVertexArray(0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		if(tex!=0)
			GL11.glDeleteTextures(tex);
		tex= GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		texResize();

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}

	@SubscribeEvent
	public void render(RenderWorldLastEvent e){
//		if(!e.type.equals(RenderGameOverlayEvent.ElementType.ALL))
//			return;
		if(Display.wasResized())
			texResize();

//		init();

		GL11.glPushAttrib(GL11.GL_TEXTURE_BIT);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex);
		GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, w, h);
//		
		GL11.glPushAttrib(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glPushAttrib(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPushAttrib(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_CULL_FACE);
//
		draw();
		
		GL11.glPopAttrib();
		GL11.glPopAttrib();
		GL11.glPopAttrib();
		GL11.glPopAttrib();
	}
	static void draw(){
		GL20.glUseProgram(prog);
		GL30.glBindVertexArray(VAO);

		float w= Display.getWidth();
		float h= Display.getHeight();
		float wh= w/h;
		float hw= h/w;
		if(wh>1)
			GL20.glUniform2f(ulocasp, wh, 1);
		else
			GL20.glUniform2f(ulocasp, 1, hw);
		while(!stack.empty()){
			Matrix4f mvp= stack.pop();
			Matrix4f mvpit= new Matrix4f();
			mvpit.m30= mvp.m30;
			mvpit.m31= mvp.m31;
			mvpit.m32= mvp.m32;
			mvpit.m33= mvp.m33;
			mvpit.m00= new Vector3f(mvp.m00, mvp.m10, mvp.m20).length();
			mvpit.m11= new Vector3f(mvp.m01, mvp.m11, mvp.m21).length();
			ShaderUtil.uniformMatrix4f(ulocmvp, mvpit);
//			ShaderUtil.uniformMatrix4f(ulocmvp, new Matrix4f());

			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		}

		GL30.glBindVertexArray(0);
		GL20.glUseProgram(0);
	}

	static void texResize(){
		w= Display.getWidth();
		h= Display.getHeight();
		GL11.glPushAttrib(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, w, h, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)null);
		GL11.glPopAttrib();
	}
}
