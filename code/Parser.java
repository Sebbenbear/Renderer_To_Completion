import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Parser {

	//private Vector3D lightSource;
	
	//change into triangles before leaving this method.
	
	//make
	
	public static Map <Vector3D, List<Triangle>> parseFile (File file) {
		Map <Vector3D, List<Triangle>> map = new HashMap <Vector3D,List<Triangle>>();
		List<Triangle> triangles = new ArrayList<Triangle>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			Vector3D lightSource = null;
			Color col;

			while ((line = br.readLine()) != null) {
				String [] tokens = line.split(" ");
				if(tokens.length == 3){
					// Parse a light
					float x = Float.parseFloat(tokens[0]);
					float y = Float.parseFloat(tokens[1]);
					float z = Float.parseFloat(tokens[2]);
					lightSource = new Vector3D(x,y,z);
				}
				else if(tokens.length == 12) {
					// Parse a triangle

					Vector3D v1 = new Vector3D(
							Float.parseFloat(tokens[0]),
							Float.parseFloat(tokens[1]),
							Float.parseFloat(tokens[2])
							);
					
					Vector3D v2 = new Vector3D(
							Float.parseFloat(tokens[3]),
							Float.parseFloat(tokens[4]),
							Float.parseFloat(tokens[5])
							);
					
					Vector3D v3 = new Vector3D(
							Float.parseFloat(tokens[6]),
							Float.parseFloat(tokens[7]),
							Float.parseFloat(tokens[8])
							);
					
					int r = Integer.parseInt(tokens[9]);
					int g	= Integer.parseInt(tokens[10]);
					int b	= Integer.parseInt(tokens[11]);
					col = new Color(r,g,b);
					
					triangles.add(new Triangle(v1, v2, v3, col));
				}
				else {
					// dont know what this is yet...
					throw new NotImplementedException();
				}
			}
			map.put(lightSource,triangles);
			if(lightSource == null){
				System.out.println("Something went wrong - lightsource is null");
			}
			br.close();
		} catch (IOException e) {
			throw new RuntimeException("file reading failed.");
		}
		
		System.out.println("Parse complete.");
		
		return map;
	}
	
	
	
}
