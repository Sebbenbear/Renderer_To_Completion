
public class Edge {

	public final Vector3D v1;
	public final Vector3D v2;
	public int [] col;

	public float x;
	public float z;

	/**
	 * An edge is a pair of vectors
	 * @param v1
	 * @param v2
	 */
	public Edge(Vector3D v1, Vector3D v2){
		this.v1 = v1;
		this.v2 = v2;
	}

	/**
	 * Returns the colour for the edge.
	 * @param col Colour
	 */
	public void setColor(int [] col){
		this.col = col;
	}

	public Vector3D vectorWithSmallestY(){
		return (v1.y < v2.y ? v1 : v2);
	}

	public Vector3D vectorWithLargestY(){
		return (v1.y > v2.y ? v1 : v2);
	}
}
