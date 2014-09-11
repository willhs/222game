package game.ui.render;
/* Code for COMP261 Assignment
 * Author: pondy
 */

/** 3x4 array representing an affine transformation
(= a 4x4 matrix in which the bottom row is always {0 0 0 1} )
Note that this cannot be used for perspective projection tranformations
since these require a non-0 bottom row.

The class provides static methods to construct translation, scaling, and rotation
matrices, and methods to multiply a translation by a vector or another matrix.
 */


/**
 * @author hardwiwill & pondy
 *
 */
public class Transform{

    private final float[][] values;

    /** Construct a Transformation given 3x4 array of elements */
    private Transform(float[][] v){
        if (v.length != 3 || v[0].length!=4)
            throw new IllegalArgumentException("Transform: Wrong size array for argument: "+v);
        else
            values = v;
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

    public float[][] getValues(){
    	return values;
    }

    public String toString(){
        StringBuilder ans = new StringBuilder();
        for (int row=0; row<3; row++){
            for (int col=0; col<4; col++){
                ans.append(values[row][col]).append(' ');
            }
            ans.append('\n');
        }
        return ans.toString();
    }

}
