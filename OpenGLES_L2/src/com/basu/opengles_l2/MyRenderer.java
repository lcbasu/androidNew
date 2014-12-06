package com.basu.opengles_l2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.YuvImage;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

public class MyRenderer implements GLSurfaceView.Renderer {

	/* Store our model data in a float buffer. */
	private final FloatBuffer mCubePositions;
	private final FloatBuffer mCubeColors;
	private final FloatBuffer mCubeNormals;

	/* Bytes per float */
	private final int mBytesPerFloat = 4;

	/*
	 * Store the view matrix. This can be thought of as our camera.
	 * This matrix transforms world space to eye space; it positions things relative to our eye.
	 */
	private float[] mViewMatrix = new float[16];

	/* Store the projection matrix.
	 * Used to project scene into 2D view port.
	 */
	private float[] mProjectionMatrix = new float[16];

	/* This handle is used to pass in the transformation matrix. */
	private int mMVPMatrixHandel;

	/* 
	 * Stores a copy of the model matrix specifically for the light position.
	 */
	private float[] mLightModelMatrix = new float[16];


	/* This handle is used to pass in the model position handel. */
	private int mPosotionHandle;

	/* This handle is used to pass in the model color information. */
	private int mColorHandle;


	/* Store the model matrix. 
	 * This matrix is used to move models from object space (where each model can be thought of being located at the center of the universe) to world space.
	 */
	private float[] mModelMatrix = new float[16];

	/*
	 * Initialize the model data.
	 */
	public MyRenderer()
	{	
		// Define points for a cube.		

		// X, Y, Z
		final float[] cubePositionData =
			{
				/* In OpenGL counter-clockwise winding is default. This means that when we look at a triangle, 
				 *  if the points are counter-clockwise we are looking at the "front". If not we are looking at
				 * the back. OpenGL has an optimization where all back-facing triangles are culled, since they
				 * usually represent the back side of an object and aren't visible anyways.
				 */

				/* Front face */
				-1.0f, 1.0f, 1.0f,				
				-1.0f, -1.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 
				-1.0f, -1.0f, 1.0f, 				
				1.0f, -1.0f, 1.0f,
				1.0f, 1.0f, 1.0f,

				/* Right face */
				1.0f, 1.0f, 1.0f,				
				1.0f, -1.0f, 1.0f,
				1.0f, 1.0f, -1.0f,
				1.0f, -1.0f, 1.0f,				
				1.0f, -1.0f, -1.0f,
				1.0f, 1.0f, -1.0f,

				/* Back face */
				1.0f, 1.0f, -1.0f,				
				1.0f, -1.0f, -1.0f,
				-1.0f, 1.0f, -1.0f,
				1.0f, -1.0f, -1.0f,				
				-1.0f, -1.0f, -1.0f,
				-1.0f, 1.0f, -1.0f,

				/* Left face */
				-1.0f, 1.0f, -1.0f,				
				-1.0f, -1.0f, -1.0f,
				-1.0f, 1.0f, 1.0f, 
				-1.0f, -1.0f, -1.0f,				
				-1.0f, -1.0f, 1.0f, 
				-1.0f, 1.0f, 1.0f, 

				/* Top face */
				-1.0f, 1.0f, -1.0f,				
				-1.0f, 1.0f, 1.0f, 
				1.0f, 1.0f, -1.0f, 
				-1.0f, 1.0f, 1.0f, 				
				1.0f, 1.0f, 1.0f, 
				1.0f, 1.0f, -1.0f,

				/* Bottom face */
				1.0f, -1.0f, -1.0f,				
				1.0f, -1.0f, 1.0f, 
				-1.0f, -1.0f, -1.0f,
				1.0f, -1.0f, 1.0f, 				
				-1.0f, -1.0f, 1.0f,
				-1.0f, -1.0f, -1.0f,
			};	

		/* R, G, B, A */
		final float[] cubeColorData =
			{				
				/* Front face (red) */
				1.0f, 0.0f, 0.0f, 1.0f,				
				1.0f, 0.0f, 0.0f, 1.0f,
				1.0f, 0.0f, 0.0f, 1.0f,
				1.0f, 0.0f, 0.0f, 1.0f,				
				1.0f, 0.0f, 0.0f, 1.0f,
				1.0f, 0.0f, 0.0f, 1.0f,

				/* Right face (green) */
				0.0f, 1.0f, 0.0f, 1.0f,				
				0.0f, 1.0f, 0.0f, 1.0f,
				0.0f, 1.0f, 0.0f, 1.0f,
				0.0f, 1.0f, 0.0f, 1.0f,				
				0.0f, 1.0f, 0.0f, 1.0f,
				0.0f, 1.0f, 0.0f, 1.0f,

				/* Back face (blue) */
				0.0f, 0.0f, 1.0f, 1.0f,				
				0.0f, 0.0f, 1.0f, 1.0f,
				0.0f, 0.0f, 1.0f, 1.0f,
				0.0f, 0.0f, 1.0f, 1.0f,				
				0.0f, 0.0f, 1.0f, 1.0f,
				0.0f, 0.0f, 1.0f, 1.0f,

				/* Left face (yellow) */
				1.0f, 1.0f, 0.0f, 1.0f,				
				1.0f, 1.0f, 0.0f, 1.0f,
				1.0f, 1.0f, 0.0f, 1.0f,
				1.0f, 1.0f, 0.0f, 1.0f,				
				1.0f, 1.0f, 0.0f, 1.0f,
				1.0f, 1.0f, 0.0f, 1.0f,

				/* Top face (cyan) */
				0.0f, 1.0f, 1.0f, 1.0f,				
				0.0f, 1.0f, 1.0f, 1.0f,
				0.0f, 1.0f, 1.0f, 1.0f,
				0.0f, 1.0f, 1.0f, 1.0f,				
				0.0f, 1.0f, 1.0f, 1.0f,
				0.0f, 1.0f, 1.0f, 1.0f,

				/* Bottom face (magenta) */
				1.0f, 0.0f, 1.0f, 1.0f,				
				1.0f, 0.0f, 1.0f, 1.0f,
				1.0f, 0.0f, 1.0f, 1.0f,
				1.0f, 0.0f, 1.0f, 1.0f,				
				1.0f, 0.0f, 1.0f, 1.0f,
				1.0f, 0.0f, 1.0f, 1.0f
			};

		/* X, Y, Z */
		/* The normal is used in light calculations and is a vector which points
		 * 
		 * orthogonal to the plane of the surface. For a cube model, the normals
		 * should be orthogonal to the points of each face. 
		 */
		final float[] cubeNormalData =
			{												
				/* Front face */
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,				
				0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f,

				/* Right face */
				1.0f, 0.0f, 0.0f,				
				1.0f, 0.0f, 0.0f,
				1.0f, 0.0f, 0.0f,
				1.0f, 0.0f, 0.0f,				
				1.0f, 0.0f, 0.0f,
				1.0f, 0.0f, 0.0f,

				/* Back face */ 
				0.0f, 0.0f, -1.0f,				
				0.0f, 0.0f, -1.0f,
				0.0f, 0.0f, -1.0f,
				0.0f, 0.0f, -1.0f,				
				0.0f, 0.0f, -1.0f,
				0.0f, 0.0f, -1.0f,

				/* Left face */
				-1.0f, 0.0f, 0.0f,				
				-1.0f, 0.0f, 0.0f,
				-1.0f, 0.0f, 0.0f,
				-1.0f, 0.0f, 0.0f,				
				-1.0f, 0.0f, 0.0f,
				-1.0f, 0.0f, 0.0f,

				/* Top face */ 
				0.0f, 1.0f, 0.0f,			
				0.0f, 1.0f, 0.0f,
				0.0f, 1.0f, 0.0f,
				0.0f, 1.0f, 0.0f,				
				0.0f, 1.0f, 0.0f,
				0.0f, 1.0f, 0.0f,

				/* Bottom face */ 
				0.0f, -1.0f, 0.0f,			
				0.0f, -1.0f, 0.0f,
				0.0f, -1.0f, 0.0f,
				0.0f, -1.0f, 0.0f,				
				0.0f, -1.0f, 0.0f,
				0.0f, -1.0f, 0.0f
			};

		/* Initialize the buffers. */
		mCubePositions = ByteBuffer.allocateDirect(cubePositionData.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
		mCubePositions.put(cubePositionData).position(0);		

		mCubeColors = ByteBuffer.allocateDirect(cubeColorData.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mCubeColors.put(cubeColorData).position(0);

		mCubeNormals = ByteBuffer.allocateDirect(cubeNormalData.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mCubeNormals.put(cubeNormalData).position(0);
	}


	/* Vertex shader. */
	protected String getVertexShader()
	{
		final String vertexShader =
					"uniform mat4 u_MVPMatrix;      \n"		/* A constant representing the combined model/view/projection matrix. */				+ 	"uniform mat4 u_MVMatrix;       \n"		// A constant representing the combined model/view matrix.	
				+ 	"uniform vec3 u_LightPos;       \n"	    /* The position of the light in eye space. */
				+ 	"attribute vec4 a_Position;     \n"		/* Per-vertex position information we will pass in. */
				+ 	"attribute vec4 a_Color;        \n"		/* Per-vertex color information we will pass in. */
				+ 	"attribute vec3 a_Normal;       \n"		/* Per-vertex normal information we will pass in. */
				+ 	"varying vec4 v_Color;          \n"		/* This will be passed into the fragment shader. */
				+ 	"void main()                    \n" 	/* The entry point for our vertex shader. */
				+ 	"{                              \n"		
				/* Transform the vertex into eye space. */
				+ 	"   vec3 modelViewVertex = vec3(u_MVMatrix * a_Position);              \n"
				/* Transform the normal's orientation into eye space.*/
				+ 	"   vec3 modelViewNormal = vec3(u_MVMatrix * vec4(a_Normal, 0.0));     \n"
				/* Will be used for attenuation.*/
				+ 	"   float distance = length(u_LightPos - modelViewVertex);             \n"
				/* Get a lighting direction vector from the light to the vertex.*/
				+ 	"   vec3 lightVector = normalize(u_LightPos - modelViewVertex);        \n"
				/* Calculate the dot product of the light vector and vertex normal. 
				 * If the normal and light vector are pointing in the same direction then it will get max illumination.
				 */
				+ 	"   float diffuse = max(dot(modelViewNormal, lightVector), 0.1);       \n" 	  		  													  
				/* Attenuate the light based on distance.*/
				+ 	"   diffuse = diffuse * (1.0 / (1.0 + (0.25 * distance * distance)));  \n"
				/* Multiply the color by the illumination level. It will be interpolated across the triangle.*/
				+ 	"   v_Color = a_Color * diffuse;                                       \n" 	 
				/* gl_Position is a special variable used to store the final position. 
				 * Multiply the vertex by the matrix to get the final point in normalized screen coordinates.
				 */
				+ 	"   gl_Position = u_MVPMatrix * a_Position;                            \n"     
				+ 	"}                                                                     \n"; 

		return vertexShader;
	}

	
	/* Fragment shader. */
	protected String getFragmentShader()
	{
		final String fragmentShader =
					"precision mediump float;       \n"		/* Set the default precision to medium. We don't need as high of a precision in the fragment shader. */
				+ 	"varying vec4 v_Color;          \n"		/* This is the color from the vertex shader interpolated across the triangle per fragment. */
				+ 	"void main()                    \n"		/* The entry point for our fragment shader. */
				+ 	"{                              \n"
				+ 	"   gl_FragColor = v_Color;     \n"		/* Pass the color directly through the pipeline. */		  
				+ 	"}                              \n";

		return fragmentShader;
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
