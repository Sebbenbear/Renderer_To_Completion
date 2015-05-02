import java.awt.Color;

public class Triangle {
	
	public Color color;
	public Vector3D v1;
	public Vector3D v2;
	public Vector3D v3;
	public Vector3D [] vectors = new Vector3D [3];
	
	public Triangle (Vector3D v1, Vector3D v2, Vector3D v3, Color color) {
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
		this.color = color;
		
		vectors[0] = v1;
		vectors[1] = v2;
		vectors[2] = v3;
	}
	
	public Edge[] getEdges() {
		return new Edge[] {
				new Edge(v1, v2),
				new Edge(v2, v3),
				new Edge(v3, v1)
		};
	}
	
	public double getMaxYValue(){
		return Math.max(v1.y, Math.max(v2.y, v3.y));
	}
	
	public double getMinYValue(){
		return Math.min(v1.y, Math.min(v2.y, v3.y));
	}
	
	public String toString(){
		return String.format("Triangle: " + v1 + " " + v2 + " " + v3);
	}
	
}
