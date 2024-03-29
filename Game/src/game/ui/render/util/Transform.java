package game.ui.render.util;

import java.io.Serializable;

import game.world.dimensions.Point3D;
import game.world.dimensions.Vector3D;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;


/**
 * @author hardwiwill 300285801
 * @author pondy; pondy wrote most of this class.
 *
 * 3x4 array representing an affine transformation
 * (= a 4x4 matrix in which the bottom row is always {0 0 0 1} )
 * Note that this cannot be used for perspective projection tranformations
 * since these require a non-0 bottom row.
 *
 * The class provides static methods to construct translation, scaling, and rotation
 * matrices, and methods to multiply a translation by a vector or another matrix.
 *
 */
public class Transform implements Serializable{

    private final float[][] values;

    /** Construct a Transformation given 3x4 array of elements */
    private Transform(float[][] v){
        if (v.length != 3 || v[0].length!=4)
            throw new IllegalArgumentException("Transform: Wrong size array for argument: "+v);
        else
            values = v;
    }

    /**
     * Create a Transform from a double 2d array.
     * Converts all double values to float values.
     * @param v
     */
    private Transform(double[][] v){
    	if (v.length != 3 || v[0].length!=4)
            throw new IllegalArgumentException("Transform: Wrong size array for argument: "+v);
    	else{

    		values = new float[3][4];

    		for (int r = 0; r < values.length; r++){
        		for (int c = 0; c < values[0].length; c++){
        			values[r][c] = (float)v[r][c];
        		}
        	}
    	}

    }

    /** Construct an identity Transformation */
    public static Transform identity(){
        return new Transform(new float[][]{{1.0f, 0.0f, 0.0f, 0.0f},
					   {0.0f, 1.0f, 0.0f, 0.0f},
					   {0.0f, 0.0f, 1.0f, 0.0f}});
    }

    /** Construct a translation Transformation based on a vector */
    public static Transform newTranslation(Vector3D tr){
        return newTranslation(tr.getX(), tr.getY(), tr.getZ());
    }

    /** Construct a translation Transformation given dx, dy, dz */
    public static Transform newTranslation(float tx, float ty, float tz){
        return new Transform(new float[][]{{1.0f, 0.0f, 0.0f, tx},
					   {0.0f, 1.0f, 0.0f, ty},
					   {0.0f, 0.0f, 1.0f, tz}});
    }

    /** Construct a scaling Transformation given values in a vector */
    public static Transform newScale(Vector3D sc){
        return newScale(sc.getX(), sc.getY(), sc.getZ());
    }
    /** Construct a scaling Transformation given sx, sy, sz */
    public static Transform newScale(float sx, float sy, float sz){
        return new Transform(new float[][]{{sx,  0.0f, 0.0f, 0.0f},
					   {0.0f, sy,  0.0f, 0.0f},
					   {0.0f, 0.0f, sz,  0.0f}});
    }

    /** Construct a rotation Transformation given angle around x axis */
    public static Transform newXRotation(float th){
        float sinth = (float)Math.sin(th);
        float costh = (float)Math.cos(th);
        return new Transform(new float[][]{{1.0f, 0.0f,  0.0f,  0.0f},
					   {0.0f, costh,-sinth, 0.0f},
					   {0.0f, sinth, costh, 0.0f}});
    }

    /** Construct a rotation Transformation given angle around y axis */
    public static Transform newYRotation(float th){
        float sinth = (float)Math.sin(th);
        float costh = (float)Math.cos(th);
        return new Transform(new float[][]{{costh, 0.0f, sinth, 0.0f},
					   {0.0f,  1.0f, 0.0f,  0.0f},
					   {-sinth, 0.0f, costh, 0.0f}});
    }

    /** Construct a rotation Transformation given angle around z axis */
    public static Transform newZRotation(float th){
        float sinth = (float)Math.sin(th);
        float costh = (float)Math.cos(th);
        return new Transform(new float[][]{{costh,-sinth, 0.0f, 0.0f},
					   {sinth, costh, 0.0f, 0.0f},
					   {0.0f,  0.0f,  1.0f, 0.0f}});
    }

    /* post multiply this transform by another (this * other) */
    public Transform compose(Transform other){
        float[][] ans = new float[3][4];
        for (int row=0; row<3; row++){
            for (int col=0; col<4; col++){
                for (int i=0; i<3; i++){
                    ans[row][col] +=this.values[row][i]*other.values[i][col];
                }
            }
            ans[row][3] +=this.values[row][3];
        }
        return new Transform(ans);
    }

    /* apply this transform to a vector*/
    public Vector3D multiply(Vector3D vect){
        if (values==null || values[0]==null || values[1]==null || values[2]==null){
	    throw new IllegalStateException("Ill-formed transform");}
	if (vect==null){
	    throw new IllegalArgumentException("multiply by null vector");}
        float x = values[0][3];
        float y = values[1][3];
        float z = values[2][3];
        x += values[0][0]*vect.x + values[0][1]*vect.y + values[0][2]*vect.z;
        y += values[1][0]*vect.x + values[1][1]*vect.y + values[1][2]*vect.z;
        z += values[2][0]*vect.x + values[2][1]*vect.y + values[2][2]*vect.z;
        return new Vector3D(x, y, z);
    }

    /* apply this transform to a vector*/
    public Point3D multiply(Point3D point){
        if (values==null || values[0]==null || values[1]==null || values[2]==null){
	    throw new IllegalStateException("Ill-formed transform");}
	if (point==null){
	    throw new IllegalArgumentException("multiply by null vector");}
        float x = values[0][3];
        float y = values[1][3];
        float z = values[2][3];
        x += values[0][0]*point.x + values[0][1]*point.y + values[0][2]*point.z;
        y += values[1][0]*point.x + values[1][1]*point.y + values[1][2]*point.z;
        z += values[2][0]*point.x + values[2][1]*point.y + values[2][2]*point.z;
        return new Point3D(x, y, z);
    }

    /**
     * inverts the transform
     */
    public Transform inverse(){
    	double[][] doubleValues = new double[values.length][values[0].length-1];
    	for (int r = 0; r < values.length; r++){
    		for (int c = 0; c < values[0].length-1; c++){
    			doubleValues[r][c] = values[r][c];
    		}
    	}
    	RealMatrix matrix = MatrixUtils.createRealMatrix(doubleValues);
    	RealMatrix inverse = MatrixUtils.inverse(matrix);

    	double[][] inverse3dMatrix = inverse.getData();
    	float[][] affineInverseData = new float[values.length][values[0].length];

    	for (int r = 0; r < inverse3dMatrix.length; r++){
    		for (int c = 0; c < inverse3dMatrix[0].length; c++){
    			affineInverseData[r][c] = (float)inverse3dMatrix[r][c];
    		}
    		affineInverseData[r][values[0].length-1] = (float)values[r][values[0].length-1];
    	}

    	return new Transform(affineInverseData);

    	/*float[][] inverse = new float[values.length][values[0].length];
    	for (int r = 0; r < values.length; r++){
    		for (int c = 0; c < values[0].length; c++){
    			inverse[r][c] = -values[r][c];
    		}
    	}
    	return new Transform(inverse);*/
    }

    public float[][] getValues(){
    	return values;
    }

    public String toString(){
        StringBuilder ans = new StringBuilder();
        for (int row=0; row<3; row++){
            for (int col=0; col<4; col++){
                ans.append(String.format("%4.2f",values[row][col])).append(' ');
            }
            ans.append('\n');
        }
        return ans.toString();
    }

}
