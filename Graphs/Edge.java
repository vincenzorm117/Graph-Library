


/**
 * @author Vincenzo Marconi
 * @version 1.0
 */
public class Edge {

	
	
	/**
	 * The starting vertex or origin.
	 */
	public Vertex a;
	
	/**
	 * The finishing vertex or destination.
	 */
	public Vertex b;
	
	/**
	 * If graph is weighted this variable would hold the weight of the edge.
	 */
	public int weight;
	
	/**
	 * Constructs an unweighed edge
	 * @param a The origin vertex
	 * @param b The destination vertex
	 */
	public Edge(Vertex a, Vertex b){
		this.a = a;
		this.b = b;
		weight = 0;
	}
	
	/**
	 * Constructs a weighed edge.
	 * @param a The origin vertex
	 * @param b The destination vertex
	 * @param weight The weight of the edge
	 */
	public Edge(Vertex a, Vertex b, int weight){
		this.a = a;
		this.b = b;
		this.weight = weight;
	}

	
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////

	/**
	 * Determines whether the passed object is an Edge.
	 * @see Graph#isWorkingGraph(Graph, boolean)
	 * @see Vertex#aVertex(Object)
	 * @param o The object to check to see if it is an Edge
	 * @return False if null, if not an instance of an Edge, if vertices are not vertices; True otherwise.
	 */
	public static boolean anEdge(Object o){
	
		//Case: Object exist and is an Edge?
		if(o instanceof Edge){	
			Edge e = (Edge)o;
			if(Vertex.aVertex(e.a) && Vertex.aVertex(e.b))
				return true;
		}
			
		// If no conditions describing an edge are met return false
		return false;
	}

	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////

	/**
	 * Creates and returns an edge that starts from the given edge's destination and ends in its origin.
	 * If a conjugate already exists it returns that conjugate.
	 * @return Edge
	 * @see Edge#conjugate(Edge) 
	 */
	public Edge createConjugate(){
			
		Edge c = Edge.conjugate(this);
		if(c != null) return c;
		
		return new Edge(this.b, this.a);
		
		
	}
	
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////

	/**
	 * If it exists, it returns an edge that starts from the given edge's destination and ends in its origin.
	 * @return Edge
	 * @param x The edge to find the conjugate of.
	 * @see Edge#createConjugate()
	 */
	public static Edge conjugate(Edge x){
			
		//NOTE: The method is static because of the NullPointerException. 
		//      The nonstatic method was being called from a null edge.
		
			for(Edge e : x.b.out)  // For all the edges coming out of the destination
				if(e.b == x.a)	  // If the destination of one of those edges is the origin
					return e;		  // of this edge then return that edge
		
		// Default: If the an edge isn't found return null
		return null;
	}
	
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the vertex of the edge that is not the passed one.
	 * @return Vertex
	 * @param v Usually it is the origin of the given edge.
	 * @return Vertex
	 */
	public  Vertex opposite(Vertex v){
		
		if(this.a == v) return this.b;
		if(this.b == v) return this.a;
		
		return null;
	}
	
	
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a string containing an ordered triple being the origin, destination, and the weight all boxed up in parentheses.
	 * @return String
	 */
	public String toString(){
		return ( "(" + this.a.name +","+ this.b.name +","+this.weight+")");
	}
	
}

	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////