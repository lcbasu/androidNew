package com.basu.opengles_l2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

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

	/* This is a handle to our light point program. */
	private int mPointProgramHandle;

	/* This is a handle to our per-vertex cube shading program. */
	private int mPerVertexProgramHandle;


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
				+ 	"   v_Color = a_Color * diffuse * 0.8;                                       \n" 	// 0.8 for opacity 
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
		/* Set the background clear color to black. */
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		/* Use culling to remove back faces. */
		GLES20.glEnable(GLES20.GL_CULL_FACE);

		/* Enable depth testing. */
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);

		/* Position the eye in front of the origin. */
		final float eyeX = 0.0f;
		final float eyeY = 0.0f;
		final float eyeZ = -1.0f;

		/* We are looking toward the distance. */
		final float lookX = 0.0f;
		final float lookY = 0.0f;
		final float lookZ = -5.0f;

		/* Set our up vector. This is where our head would be pointing were we holding the camera. */
		final float upX = 0.0f;
		final float upY = 1.0f;
		final float upZ = 0.0f;

		/* Set the view matrix. This matrix can be said to represent the camera position.
		 * NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
		 * view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
		 */
		Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);		

		final String vertexShader = getVertexShader();   		
		final String fragmentShader = getFragmentShader();			

		final int vertexShaderHandle = compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);		
		final int fragmentShaderHandle = compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);		

		mPerVertexProgramHandle = createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle, 
				new String[] {"a_Position",  "a_Color", "a_Normal"});								                                							       

		/* Define a simple shader program for our point. */
		final String pointVertexShader =
				 			"uniform mat4 u_MVPMatrix;      \n"		
						 +	"attribute vec4 a_Position;     \n"		
						 + "void main()                    \n"
						 + "{                              \n"
						 + "   gl_Position = u_MVPMatrix   \n"
						 + "               * a_Position;   \n"
						 + "   gl_PointSize = 8.0;         \n"
						 + "}                              \n";

		 final String pointFragmentShader = 
				 		   "precision mediump float;       \n"					          
						 + "void main()                    \n"
						 + "{                              \n"
						 + "   gl_FragColor = vec4(1.0,    \n" 
						 + "   1.0, 1.0, 1.0);             \n"
						 + "}                              \n";

		 final int pointVertexShaderHandle = compileShader(GLES20.GL_VERTEX_SHADER, pointVertexShader);
		 final int pointFragmentShaderHandle = compileShader(GLES20.GL_FRAGMENT_SHADER, pointFragmentShader);
		 mPointProgramHandle = createAndLinkProgram(pointVertexShaderHandle, pointFragmentShaderHandle, new String[] {"a_Position"}); 
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

	}

	/** 
	 * Helper function to compile a shader.
	 * 
	 * @param shaderType The shader type.
	 * @param shaderSource The shader source code.
	 * @return An OpenGL handle to the shader.
	 */
	private int compileShader(final int shaderType, final String shaderSource) 
	{
		int shaderHandle = GLES20.glCreateShader(shaderType);

		if (shaderHandle != 0) 
		{
			// Pass in the shader source.
			GLES20.glShaderSource(shaderHandle, shaderSource);

			// Compile the shader.
			GLES20.glCompileShader(shaderHandle);

			// Get the compilation status.
			final int[] compileStatus = new int[1];
			GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

			// If the compilation failed, delete the shader.
			if (compileStatus[0] == 0) 
			{
				Log.e("Compile Status", "Error compiling shader: " + GLES20.glGetShaderInfoLog(shaderHandle));
				GLES20.glDeleteShader(shaderHandle);
				shaderHandle = 0;
			}
		}

		if (shaderHandle == 0)
		{			
			throw new RuntimeException("Error creating shader.");
		}

		return shaderHandle;
	}	

	/**
	 * Helper function to compile and link a program.
	 * 
	 * @param vertexShaderHandle An OpenGL handle to an already-compiled vertex shader.
	 * @param fragmentShaderHandle An OpenGL handle to an already-compiled fragment shader.
	 * @param attributes Attributes that need to be bound to the program.
	 * @return An OpenGL handle to the program.
	 */
	private int createAndLinkProgram(final int vertexShaderHandle, final int fragmentShaderHandle, final String[] attributes) 
	{
		int programHandle = GLES20.glCreateProgram();

		if (programHandle != 0) 
		{
			// Bind the vertex shader to the program.
			GLES20.glAttachShader(programHandle, vertexShaderHandle);			

			// Bind the fragment shader to the program.
			GLES20.glAttachShader(programHandle, fragmentShaderHandle);

			// Bind attributes
			if (attributes != null)
			{
				final int size = attributes.length;
				for (int i = 0; i < size; i++)
				{
					GLES20.glBindAttribLocation(programHandle, i, attributes[i]);
				}						
			}

			// Link the two shaders together into a program.
			GLES20.glLinkProgram(programHandle);

			// Get the link status.
			final int[] linkStatus = new int[1];
			GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

			// If the link failed, delete the program.
			if (linkStatus[0] == 0) 
			{				
				Log.e("Link Status", "Error compiling program: " + GLES20.glGetProgramInfoLog(programHandle));
				GLES20.glDeleteProgram(programHandle);
				programHandle = 0;
			}
		}

		if (programHandle == 0)
		{
			throw new RuntimeException("Error creating program.");
		}

		return programHandle;
	}

}
