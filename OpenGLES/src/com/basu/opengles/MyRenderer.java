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
	
	/* This handle is used to pass in the transformation matrix. */
	private int mMVPMatrixHandel;
	
	/* This handle is used to pass in the model position handel. */
	private int mPosotionHandle;
	
	/* This handle is used to pass in the model color information. */
	private int mColorHandle;
	
	

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
		
		/* Shader */
		
		final String vertexShader = 
					"uniform mat4 u_MVPMatrix					\n"		/* A constant representing the combined Model-View-Projection matrix. */
				+	"attribute vec4 a_Position					\n"		/* Per-vertex position information that will be passed in by us. */
				+	"attribute vec4 a_Color						\n"		/* Per-vertex color information that will be passed in by us. */
				+	"varying vec4 v_Color						\n"		/* This variable will be passed into the fragment shader.*/
				+	"void main()								\n"		/* Entry point for the vertex shader. */
				+	"{											\n"		
				+	"	v_Color = a_Color						\n"		/* Passing our color information to the fragment shader. It will be interpolated across the triangle.*/
				+	"	gl_Position = u_MVPMatrix * a_Posotion;	\n"		/* gl_position is an inbuilt variable which is used to store the actual position. */
				+	"}											\n";	/* We obtain the final position by multiplying the vertex by the matrix to get the final point in normalized screen coordinates. */
		
		/*
		 * The fragment shader actually put stuff on the screen. 
		 * In this shader, we grab the varying color from the vertex shader, and just pass it straight through to OpenGL. 
		 * The point is already interpolated per pixel since the fragment shader runs for each pixel that will be drawn.
		 */
		
		final String fragmentShader = 
					"uniform mat4 u_MVPMatrix					\n"		/* Set the default precision to medium. We don't need as high of a precision in the fragment shader. */
				+	"varying vec4 v_Color;						\n"		/* Color from the vertex shader interpolated across the triangle per fragment. */
				+	"void main()								\n"		/* Entry point for the fragment shader. */
				+	"{											\n"		
				+	"	gl_FragColor = v_Color;					\n"		/* gl_FragColor is an inbuilt variable which is used to store the actual color and pass the color directly through the pipeline.*/
				+	"}											\n";
		
		/* Loading the vertex shader. */
		int vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
		
		if (vertexShaderHandle != 0) {
			
			/* Passing the shader source. */
			GLES20.glShaderSource(vertexShaderHandle, vertexShader);
			
			/* Compiling the shader. */
			GLES20.glCompileShader(vertexShaderHandle);
			
			/* Get the compilation status. */
			final int compileStatus[] = new int[1];
			GLES20.glGetShaderiv(vertexShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
			
			/* Delete the shader if the compilation fails. */
			if (compileStatus[0] == 0) {
				GLES20.glDeleteShader(vertexShaderHandle);
				vertexShaderHandle = 0;
			}			
		}
		
		if (vertexShaderHandle == 0) {
			throw new RuntimeException("Error while creating vertex shader.");
		}
		
		/* Loading the fragment shader. */
		int fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
		
		if (fragmentShaderHandle != 0) {
			
			/* Passing the shader source. */
			GLES20.glShaderSource(fragmentShaderHandle, fragmentShader);
			
			/* Compiling the shader. */
			GLES20.glCompileShader(fragmentShaderHandle);
			
			/* Get the compilation status. */
			final int compileStatus[] = new int[1];
			GLES20.glGetShaderiv(fragmentShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
			
			/* Delete the shader if the compilation fails. */
			if (compileStatus[0] == 0) {
				GLES20.glDeleteShader(fragmentShaderHandle);
				fragmentShaderHandle = 0;
			}			
		}
		
		if (fragmentShaderHandle == 0) {
			throw new RuntimeException("Error while creating fragment shader.");
		}
		
		/* Creates a program object and stores its handle. */
		int programHandle = GLES20.glCreateProgram();
		
		if (programHandle != 0) {
			
			/* Bind the vertex shader to the program. */
			GLES20.glAttachShader(programHandle, vertexShaderHandle);
			
			/* Bind the fragment shader to the program. */
			GLES20.glAttachShader(programHandle, fragmentShaderHandle);
			
			/* Binds the attributes. */
			GLES20.glBindAttribLocation(programHandle, 0, "a_Position");
			GLES20.glBindAttribLocation(programHandle, 1, "a_Color");
			
			/* Links the two shaders together into a program. */
			GLES20.glLinkProgram(programHandle);
			
			/* Retrieve the link status. */
			final int[] linkStatus = new int[1];
			GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);
			
			if (linkStatus[0] == 0) {
				GLES20.glDeleteProgram(programHandle);
				programHandle = 0;
			}
		}
		if (programHandle == 0) {
			throw new RuntimeException("Error while creating program.");
		}
		
		
		/* Set the program handles. 
		 * These are used to pass in the values to the program.
		 */
		mMVPMatrixHandel = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
		mPosotionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
		mColorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color");
		

		
	}
}
