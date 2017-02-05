
import java.io.File;


/**
 * 
 * Used to run Methods
 *
 */
public class Sandbox {

	public static void main(String[] args) {
		
		File in = new File("dijks.txt");
		
		Graph g = new Graph(in);

		g.printGraph(true);

		Vertex a = g.getAVertex();

		Graph h = g.aDijkstraShortestPathTree(a);


		System.out.println("PRINTING GRAPH:\n---------------------------------------------------------------");
		h.printGraph(true);

		
	}

}
