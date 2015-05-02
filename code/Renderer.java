import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Renderer extends GUI{

	private float shiftX = 1.0f;
	private float shiftY = 1.0f;

	public Vector3D lightSource;
	//public Vector3D camera = new Vector3D(0,0,1,0);
	private List<Triangle> triangleList = new ArrayList<Triangle>();

	private Map <Vector3D, List<Triangle>> triangleMap = new HashMap <Vector3D,List<Triangle>>();

	//	public Renderer(){
	//		onLoad(new File("./data/tetras.txt"));
	//	}

	@Override
	protected void onLoad(File file) {
		System.out.println("got here");
		triangleMap = Parser.parseFile(file);
		lightSource = triangleMap.keySet().iterator().next();
		triangleList = triangleMap.values().iterator().next();
		System.out.println("loaded triangles");
	}

	@Override
	protected void onKeyPress(KeyEvent ev) {
		if (ev.getKeyCode() == KeyEvent.VK_UP || Character.toUpperCase(ev.getKeyChar()) == 'W'){
			//TILT UP
			shiftY -= 0.1;
		}
		else if (ev.getKeyCode() == KeyEvent.VK_LEFT || Character.toUpperCase(ev.getKeyChar()) == 'A'){
			//LEFT
			shiftX -= 0.1;
		}
		else if (ev.getKeyCode() == KeyEvent.VK_DOWN || Character.toUpperCase(ev.getKeyChar()) == 'S'){
			//DOWN
			shiftY += 0.1;
		}
		else if (ev.getKeyCode() == KeyEvent.VK_RIGHT || Character.toUpperCase(ev.getKeyChar()) == 'D'){
			//RIGHT
			shiftX += 0.1;
		}
	}
	//http://en.wikipedia.org/wiki/Z-buffering#Algorithm

	@Override
	protected BufferedImage render() {		
		ZBuffer zBuffer = new ZBuffer();
		zBuffer.setColor(new Color [CANVAS_WIDTH][CANVAS_HEIGHT]);
		zBuffer.setDepth(new float [CANVAS_WIDTH][CANVAS_HEIGHT]);  //set this to infinity

		for(int x = 0; x < CANVAS_WIDTH; x++){
			for(int y = 0; y < CANVAS_HEIGHT; y++){
				zBuffer.depth[x][y] = Float.POSITIVE_INFINITY;
			}
		}



		for(Triangle t : triangleList){
			// -------------- BEGIN VERTEX PHASE ---------------
			// t = transform(t);
			
			Vector3D normal = (t.v1.crossProduct(t.v2));
			
			//create edge list - go through each edge in the triangle
			//if(false){ //if the normal is away from the viewer
			//}
			
			if(true){ //normal facing towards camera
				//TODO
				//check the normal

				// When lighting is per triangle
				Color shading = t.color;		//change this value
				
				// -------------- END VERTEX PHASE ---------------
				// -------------- BEGIN RASTERIZATION PHASE ---------------
				
				for(RasterizationLine line : computeEdgeLists(t)){
					if(line != null) {
						int y = line.y;

						System.out.println(line);

						if(line.start != null && line.end != null) {						
							int x = (int)line.start.x;
							float z = (int)line.start.z;
							//mz = (EL[y]. zright– EL[y]. zleft) / (EL[y]. xright– EL[y]. xleft) 
							float mz = (line.end.z - line.start.z) / (line.end.x - line.start.x);
							while(x <= (int)line.end.x){
								if(x < CANVAS_WIDTH && x >= 0 && y < CANVAS_HEIGHT && y >= 0){
									if(z < zBuffer.depth[x][y]){
										
										// -------------- END RASTERIZATION PHASE ---------------
										// -------------- BEGIN SHADING PHASE ---------------
										// if lighting is per-pixel
										// Color shading = t.color;		//change this value
										// -------------- END SHADING PHASE ---------------
										// -------------- BEING DISPLAY PHASE ---------------
										
										zBuffer.color[x][y] = shading;
										zBuffer.depth[x][y] = z;
										
										// -------------- END DISPLAY PHASE ---------------
									}
								}
								x += 1;
								z += mz;
							}
						}
					}
				}
			}
		}
		return convertBitmapToImage(zBuffer.color);
	}

	/**
	 * Should this return an array or an arraylist?
	 * @param t
	 * @param v1
	 * @param v2
	 * @param v3
	 * @return
	 */
	private RasterizationLine[] computeEdgeLists(Triangle t) {
		//Find the biggest Y value out of all the points on the triangle - this will be the size of i
		//truncate the values
		System.out.println(t.toString());

		//Gives expected value for the triangle [passes]
		int minY = (int) t.getMinYValue();
		int maxY = (int) t.getMaxYValue();

		System.out.println("MinY:" + minY);
		System.out.println("MaxY:" + maxY);

		int delta = maxY - minY;
		//Initialise the edge list
		RasterizationLine [] fullEdgeList = new RasterizationLine [delta];

		//initialise edge lists with infinity
		for(int i = 0; i < delta; i++){
			fullEdgeList[i] = new RasterizationLine(minY + i);
		}

		//Set up the edges
		Edge [] edges = t.getEdges();

		//for each edge in polygon do
		for(Edge e : edges){
			//get the biggest vertex and the smallest vertex
			Vector3D vA = e.vectorWithLargestY();
			Vector3D vB = e.vectorWithSmallestY();

			float mx = (vB.x - vA.x) / (vB.y - vA.y);
			float mz = (vB.z - vA.z) / (vB.y - vA.y);

			//starting values of x,z +vany other values to interpolate
			float x = vB.x;
			float z = vB.z;

			//initialise starting value and final value
			int i = (int) vB.y;
			int maxI = (int)vA.y;

			while(i < maxI){
				//goes until i <= maxI
				//put x and z into EdgeList[i] ( as left or right, depending on x)
				RasterizationLineInterpolationPoint point = new RasterizationLineInterpolationPoint(x, z);

				RasterizationLine line = fullEdgeList[i-minY];
				if(line.start == null) {
					line.start = point;
				}
				else {
					if(x > line.start.x){
						line.end = point;
					}
					else {
						// Push old start to be end to fit in the new start
						line.end = line.start;
						line.start = point;
					}
				}

				i++;
				x += mx;
				z += mz;
			} 
		}
		return fullEdgeList;
	}

	private BufferedImage convertBitmapToImage(Color[][] bitmap) {
		BufferedImage image = new BufferedImage(CANVAS_WIDTH, CANVAS_HEIGHT,
				BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < CANVAS_WIDTH; x++) {
			for (int y = 0; y < CANVAS_HEIGHT; y++) {
				Color color = bitmap[x][y];
				if (color == null) {
					color = Color.GRAY;
				}

				image.setRGB(x, y, color.getRGB());
			}
		}
		return image;
	}

	public static void main(String[] args) {
		new Renderer();
	}



}
