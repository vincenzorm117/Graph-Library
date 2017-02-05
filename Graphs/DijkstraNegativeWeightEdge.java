
/**
 * 
 * @author Vincenzo Marconi
 * 
 * Used only on Dijkstra's shortestpath methods to indicate that all edges are no negative.
 *
 */
public class DijkstraNegativeWeightEdge extends Exception{

	public DijkstraNegativeWeightEdge(String msg) {
		super(msg);
	}

	public DijkstraNegativeWeightEdge() {
		super();
	}

}
