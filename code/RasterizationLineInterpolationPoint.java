
public class RasterizationLineInterpolationPoint {
	
	public final float x;
	public final float z;
	
	public RasterizationLineInterpolationPoint(float x, float z){
		this.x = x;
		this.z = z;
	}
	
	@Override
	public String toString() {
		return "IPoint { x:" + x + ", z:" + z + " }";
	}
}
