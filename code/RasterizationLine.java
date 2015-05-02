
public class RasterizationLine {
	public RasterizationLineInterpolationPoint start;
	public RasterizationLineInterpolationPoint end;
	
	public final int y;

	public RasterizationLine(int y){
		this.y = y;
	}
	
	@Override
	public String toString() {
		return "Line { " + start + " => " + end + " }";
	}
}
