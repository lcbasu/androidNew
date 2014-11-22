package com.basu.opengles;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;

public class MyRenderer implements GLSurfaceView.Renderer {

	private FloatBuffer mTriangle1Vertices;
	private FloatBuffer mTriangle2Vertices;
	private FloatBuffer mTriangle3Vertices;

	/* Bytes per float */
	private final int mBytesPerFloat = 4;

	public MyRenderer() {

		/* Define the coordinates of the triangles */


		/* This triangle is red, green, and blue.*/
		final float[] triangle1VerticesData = {
				/* X, Y, Z, */ 
				/* R, G, B, A */
				-0.5f, -0.25f, 0.0f, 
				1.0f, 0.0f, 0.0f, 1.0f,

				0.5f, -0.25f, 0.0f,
				0.0f, 0.0f, 1.0f, 1.0f,

				0.0f, 0.559016994f, 0.0f, 
				0.0f, 1.0f, 0.0f, 1.0f};

		/* This triangle is yellow, cyan, and magenta. */
		final float[] triangle2VerticesData = {
				/* X, Y, Z, */ 
				/* R, G, B, A */
				-0.5f, -0.25f, 0.0f, 
				1.0f, 1.0f, 0.0f, 1.0f,

				0.5f, -0.25f, 0.0f, 
				0.0f, 1.0f, 1.0f, 1.0f,

				0.0f, 0.559016994f, 0.0f, 
				1.0f, 0.0f, 1.0f, 1.0f};

		/* This triangle is white, gray, and black. */
		final float[] triangle3VerticesData = {
				/* X, Y, Z, */ 
				/* R, G, B, A */
				-0.5f, -0.25f, 0.0f, 
				1.0f, 1.0f, 1.0f, 1.0f,

				0.5f, -0.25f, 0.0f, 
				0.5f, 0.5f, 0.5f, 1.0f,

				0.0f, 0.559016994f, 0.0f, 
				0.0f, 0.0f, 0.0f, 1.0f};
		
		/* Initialize the buffers so that Java stores the bytes as native side order */
		/* This is done because the OpenGL is implemented in C so we need to do so*/
		mTriangle1Vertices = ByteBuffer.allocateDirect(triangle1VerticesData.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
		mTriangle2Vertices = ByteBuffer.allocateDirect(triangle2VerticesData.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
		mTriangle3Vertices = ByteBuffer.allocateDirect(triangle3VerticesData.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
					
		/* Here we copy our array into the buffer */
		mTriangle1Vertices.put(triangle1VerticesData).position(0);
		mTriangle2Vertices.put(triangle2VerticesData).position(0);
		mTriangle3Vertices.put(triangle3VerticesData).position(0);
	}

	@Override
	public void onDrawFrame(GL10 gl) {

	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {

	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

	}
}
