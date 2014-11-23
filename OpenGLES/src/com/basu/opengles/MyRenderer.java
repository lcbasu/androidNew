package com.basu.opengles;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

public class MyRenderer implements GLSurfaceView.Renderer {

	private FloatBuffer mTriangle1Vertices;
	private FloatBuffer mTriangle2Vertices;
	private FloatBuffer mTriangle3Vertices;

	/* Bytes per float */
	private final int mBytesPerFloat = 4;

	/*
	 * Store the view matrix. This can be thought of as our camera.
	 * This matrix transforms world space to eye space; it positions things relative to our eye.
	 */
	private float[] mViewMatrix = new float[16];

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
		
		/* Sets the background clear color to red. */
		GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
		
		/* Positioning the eye(camera) behind the origin. */
		final float eyeX = 0.0f;
		final float eyeY = 0.0f;
		final float eyeZ = 1.0f;
		
		/* We are looking at towards: */
		final float lookAtX = 0.0f;
		final float lookAtY = 0.0f;
		final float lookAtZ = -5.0f;
		
		/* 
		 * Setting up our UP vector. 
		 * This is the vector that points in the same direction as our head where we are holding the camera.
		 */
		final float upX = 0.0f;
		final float upY = 1.0f;
		final float upZ = 0.0f;
		
		/* 
		 * Set the View Matrix.
		 * This matrix sets the position of the camera(eye).
		 */
		Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookAtX, lookAtY, lookAtZ, upX, upY, upZ);
	}
}
