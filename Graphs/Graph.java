



import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.InputMismatchException;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;


/**
 * @author Vincenzo Marconi
 * @version 1.0
 * 
 */
public class Graph {

	private static final String ANSI_RESET = "\u001B[0m";
	private static final String ANSI_BLACK = "\u001B[30m";
	private static final String ANSI_RED = "\u001B[31m";
	private static final String ANSI_GREEN = "\u001B[32m";
	private static final String ANSI_YELLOW = "\u001B[33m";
	private static final String ANSI_BLUE = "\u001B[34m";
	private static final String ANSI_PURPLE = "\u001B[35m";
	private static final String ANSI_CYAN = "\u001B[36m";
	private static final String ANSI_WHITE = "\u001B[37m";

	/**
	 * The adjacency list of vertices
	 */
	public ArrayList<Vertex> vertices;
	
	/**
	 * The adjacency list of edges
	 */
	public ArrayList<Edge> edges;
	
	/**
	 * Contains the number of edges
	 */
	public int E;
	
	/**
	 * Contains the number of vertices
	 */
	public int V;
	
	/**
	 * Contains the labels (value) for each vertex (key)
	 */
	public Hashtable<Vertex, Label> vertexLabels;
	
	/**
	 * Contains the labels (value) for each edge (key)
	 */
	public Hashtable<Edge, Label> edgeLabels;

//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
	
	/**
	 * Initializes all variables.
	 */
	public Graph(){
		this.vertices = new ArrayList<Vertex>();
		this.edges = new ArrayList<Edge>();
		V = E = 0;
		edgeLabels = new Hashtable<Edge, Label>();
		vertexLabels = new Hashtable<Vertex, Label>();
	}

//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
	
	/**
	 * Creates a given graph from the passed graph g. All values of g are passed to the given graph.
	 * Note: edge/vertex references are kept no new vertices or edges are created
	 * @param g The given graph to be duplicated`.
	 */
	public Graph(Graph g){
		if(g == null) throw new NullPointerException("Can't construct graph from null");
		
		this.V = g.V;
		this.E = g.E;
		
		this.vertices = new ArrayList<Vertex>();
		this.edges = new ArrayList<Edge>();
		edgeLabels = new Hashtable<Edge, Label>();
		vertexLabels = new Hashtable<Vertex, Label>();
		
		for(Vertex v : g.vertices){
			this.vertices.add(v);
			this.vertexLabels.put(v, g.vertexLabels.get(v));
		}

		for(Edge e : g.edges){
			this.edges.add(e);
			this.edgeLabels.put(e, g.edgeLabels.get(e));
		}
		
	}
	
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////

	/**
	 * Creates a graph from a file input.
	 * File contains the following information:
	 * Number of vertices;
	 * A number of edges E;
	 * E lines containing edges with the starting vertex, a tab, the ending vertex, a tab and the weight of the edge. 
	 * @param input
	 */
	public Graph(File input)  {
		this.vertices = new ArrayList<Vertex>();
		this.edges = new ArrayList<Edge>();
		
		if(input != null) try {
			Scanner scan = new Scanner(input);

			V = scan.nextInt();
			E = scan.nextInt();
				 
			
			for(int i = 0; i < V; i++)
				vertices.add(new Vertex(i));
				
			for(int i = 0; i < E; i++){
				int a = scan.nextInt();
				int b = scan.nextInt();
				int w = scan.nextInt();
				
				Vertex A = vertices.get(a);
				Vertex B = vertices.get(b);
				
				this.insertEdge(A, B, w);
			}
			
			if(!Graph.isWorkingGraph(this, false))
				throw new FileNotFoundException();
			
			edgeLabels = new Hashtable<Edge, Label>();
			vertexLabels = new Hashtable<Vertex, Label>();
			
			this.resetGraphLabels();
			
			
		} catch (FileNotFoundException e) { 
			System.out.println("Error reading input");
			e.printStackTrace(); 
		}
		
		
	
		
	}

//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the number of vertices in the graph
	 */
	public int numVertices(){
		return this.V;
	}
	
	/**
	 * Returns the number of edges in the graph
	 */
	public int numEdges(){
		return this.E;
	}
	
	/**
	 * Returns the reference to the vertices adjacency list of the graph
	 * @return ArrayList
	 */
	public ArrayList<Vertex> Vertices(){
		return this.vertices;
	}
	
	/**
	 * Returns the reference to the edges adjacency list of the graph
	 * @return ArrayList
	 */
	public ArrayList<Edge> Edges(){
		return this.edges;
	}
	
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
	
	/**
	 * Determines whether the graph contains any vertices or not.
	 * @return boolean
	 */
	public boolean isEmpty(){
		if(this.vertices.size() == 0) return true;
		return false;
	}
	
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a string with the number of vertices and edges boxed up in brackets.
	 * @return String
	 */
	public String toString(){
		return "Graph:"+super.toString()+" [V="+V+"][E="+E+"]";
	}

//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////

	/**
	 * Determines whether graph is a workable graph, or a graph that can be processed.
	 * That means that it is not null and has some vertices that can be worked on. 
	 * @see Vertex#aVertex(Object)
	 * @see Edge#anEdge(Object)
	 * @param g The graph to check to see if working.
	 * @param unDirectedGraph Indicated whether the graph is undirected at the discretion of the user.
	 * @return False if g is null, g has no vertices, g has none legitimate edge; True otherwise. If graph undirected and an edge has no conjugate, it returns false.
	 */
	public static boolean isWorkingGraph(Graph g, boolean unDirectedGraph){
		
		//Case: graph's existence 
		if(g == null) return false;
		
		if(g.V == 0) return false;
		
		// Checking each edge checks each vertex twice
		// Runtime: O( 2V+E )
		for(Edge e : g.edges)
			if(!Edge.anEdge(e))
				return false;
		
		if(unDirectedGraph){
			for(Edge e : g.edges){
				Edge x = Edge.conjugate(e);
				if(x == null) return false;
				if(x.weight != e.weight) return false;
			}
		}
		
		return true;
	}

//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////s
	
	/**
	 * Prints the graph object I.D. Followed by, if printEdgesVertices is true, a list of the edges and vertices of the given graph. 
	 * @param printEdgesVertices If true prints the vertices and edges of the graph.
	 */
	public void printGraph(boolean printEdgesVertices){
		
		//Prints graph name
		System.out.println(this);
		
		//Case: graph has no nodes and therefore no edges
		if(this.vertices.size() == 0) return;
		
		int i = 0;
		//Case: graph has nodes and maybe edges
		if(printEdgesVertices){

			for(Vertex v : this.vertices)
				System.out.println(v+":"+this.vertexLabels.get(v));

			for(Edge e : this.edges)
				System.out.println((i++)+")"+e+":"+this.edgeLabels.get(e));
		}
		
	}

//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////

	/**
	 * Creates a weighed edge and inserts it into the adjacency list.
	 * @see Graph#insertEdge(Vertex, Vertex)
	 * @param a The starting vertex or origin
	 * @param b The ending vertex or destination
	 * @param w The weight of the edge
	 */
	public void insertEdge(Vertex a, Vertex b, int w){

		Edge e = new Edge(a,b,w);
		
		this.edges.add(e);
		
		a.out.add(e);
		
		b.in.add(e);
	}

//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////

	/**
	 * Creates an unweighed edge and inserts it into the adjacency list.
	 * @see Graph#insertEdge(Vertex, Vertex, int)
	 * @param v The starting vertex or origin
	 * @param w The ending vertex or destination
	 */
	public void insertEdge(Vertex v, Vertex w){
		
		Edge e = new Edge(v,w);

		this.E++;
		
		this.edges.add(e);

		v.out.add(e);

		w.in.add(e);
	}
	
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
	
	/**
	 * Resets the edge/vertex labels on the Hashtable.
	 * Note: It puts in the label UNEXPLORED for everything.
	 */
	public void resetGraphLabels(){
		
		for(Edge e: this.edges)
			this.edgeLabels.put(e, Label.UNEXPLORED);
		
		for(Vertex v: this.vertices)
			this.vertexLabels.put(v, Label.UNEXPLORED);
	}

//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
	
	/**
	 *  Returns a randomly picked vertex from the adjacency list.
	 *  @return A randomly picked Vertex or null if the graph is empty.
	 */
	public Vertex getAVertex(){
		if(this.isEmpty()) return null;
		
		Random r = new Random();
		int i = r.nextInt() ;
		if(i < 0) i*= -1;
		return this.vertices.get( i % this.vertices.size());
	}
	
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
	
	/**
	 * Sees if the passed vertex is present in the graph.
	 * @return True if the adjacency list contains the passed vertex; false otherwise.
	 */
	public boolean containsVertex(Vertex v){
		if(this.vertices.contains(v)) return true;
		return false;
	}

//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
	
	/**
	 * Sees if the passed edge is present in the graph.
	 * @return True if the adjacency list contains the passed edge; false otherwise.
	 */
	public boolean containsEdge(Edge e){
		if(this.edges.contains(e)) return true;
		return false;
	}

//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
	
	/**
	 * Determines whether there is any edge that has the Label l in the graph's Hashtable 
	 * @see Graph#containsLoop()
	 * @see Graph#vertexContainsLabel(Label)
	 * @see Graph#isDirectedWeaklyConnected()
	 * @see Graph#isConnected()
	 * @see Graph#isDirectedStronglyConnected()
	 * @param l The label to be checked
	 * @return True if any edge has the label l; false otherwise
	 */
	public boolean edgeContainsLabel(Label l){
		for(Edge e : this.edges)
			if(this.edgeLabels.get(e) == l)
				return true;
		
		return false;
	}

//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
	
	/**
	 * Determines whether there is any vertex that has the Label l in the graph's Hashtable 
	 * @see Graph#containsLoop()
	 * @see Graph#edgeContainsLabel(Label)
	 * @see Graph#isDirectedWeaklyConnected()
	 * @see Graph#isConnected()
	 * @see Graph#isDirectedStronglyConnected()
	 * @param l The label to be checked
	 * @return True if any vertex has the label l; false otherwise
	 */
	public boolean vertexContainsLabel(Label l){
		for(Vertex v : this.vertices)
			if(this.vertexLabels.get(v) == l)
				return true;
		
		return false;
	}

//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
	
	/**
	 * Determines whether there are any edges that create a loop, i.e. any BACK or CROSS edges. 
	 * @see Graph#LoopFinderDFS(Vertex, boolean)
	 * @see Graph#hasLoop(boolean)
	 * @see Graph#vertexContainsLabel(Label)
	 * @see Graph#edgeContainsLabel(Label)
	 * @return True if there is any edge that has the label BACK or CROSS; false otherwise.
	 */
	public boolean containsLoop(){
		
		for(Edge e: this.edges){
			Label tmp = this.edgeLabels.get(e);	
			if(tmp == Label.BACK || tmp == Label.CROSS)
				return true;
		}
		
		return false;
	}
	
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
	
	/**
	 * Removes the passed edge from the graph if it exists.
	 * @see Graph#removeVertex(Vertex)
	 * @param del Edge to be removed.
	 */
	public void removeEdge(Edge del){
		if(del == null) throw new NullPointerException("Passed Edge is null");
		if(edges.isEmpty()) return;
		
		if(this.edges.contains(del)){
			this.edges.remove(del);
			del.a.out.remove(del);
			del.b.in.remove(del);
			this.E--;
		}
		
		
	}
	
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
	
	/**
	 * Removes the passed vertex from the graph if it exists.
	 * @see Graph#removeVertex(Vertex)
	 * @param del The vertex to be removed
	 */
	public void removeVertex(Vertex del){
		if(del == null) throw new NullPointerException("Passed Vertex is null");
		if(vertices.isEmpty()) return;
		
		if(vertices.contains(del)){
			this.V--;
			
			for(int i = 0; i < del.outDegree(); i++)
				this.removeEdge(del.out.get(i));
			
			for(int i = 0; i < del.inDegree(); i++)
				this.removeEdge(del.in.get(i));
		
			this.vertices.remove(del);
		}
		
	}
	
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
		
	/**
	 * Determines whether the given graph has any loops. Note it resets graph labels.
	 * @see Graph#LoopFinderDFS(Vertex, boolean)
	 * @see Graph#containsLoop()
	 * @param unDirectedGraph True if graph is undirected; false otherwise.
	 * @return True if LoopFinderDFS finds any back edges; false otherwise.
	 */
	public boolean hasLoop(boolean unDirectedGraph){
		
		this.resetGraphLabels();
		
		for(Vertex v : this.vertices){
			if(this.vertexLabels.get(v) == Label.UNEXPLORED)
				if(LoopFinderDFS(v,unDirectedGraph) == true){
					this.resetGraphLabels();
					return true;
				}	
		}
		
		return false;
	}

//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////

	/**
	 * Determines whether the connected component v is in has any loops.
	 * @see Graph#hashCode()
	 * @param v The vertex to start a DFS loop finder at.
	 * @param unDirectedGraph True if graph is undirected; false otherwise.
	 * @return True if it ever finds a BACK edge; false otherwise.
	 */
	public boolean LoopFinderDFS(Vertex v, boolean unDirectedGraph){
		
		boolean isloop = false;
		this.vertexLabels.put(v, Label.EXPLORED);

		
		if(v.outDegree() > 0){
			for(Edge e : v.unexploredOutNeighbors(this.edgeLabels)){
	
				Vertex w = e.opposite(v);
	
	
				if(this.vertexLabels.get(w) == Label.UNEXPLORED){
	
					this.edgeLabels.put(e, Label.DISCOVERY);
					if(unDirectedGraph) this.edgeLabels.put(Edge.conjugate(e), Label.DISCOVERY);
	
					if(this.LoopFinderDFS(w, unDirectedGraph)) isloop = true;
				}
				else{
					return true;
					
				}	
			}
		}

		return isloop; 
		
	}
	
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////

	/**
	 * Runs a Depth First Search (DFS) on the given graph. Only Labels are changed.
	 * @see Graph#LoopFinderDFS(Vertex, boolean)
	 * @see Graph#hasLoop(boolean)
	 * @see Graph#ccDFS(Vertex, boolean)
	 * @param unDirectedGraph True if graph is undirected; false otherwise.
	 */
	public void DFS(boolean unDirectedGraph){
		
		if(this.vertices.isEmpty()) return;
		
		for(Vertex v : this.vertices)
			if(this.vertexLabels.get(v) == Label.UNEXPLORED)
				this.ccDFS(v, unDirectedGraph);
		
		
	}
	
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////

	/**
	 * Runs a Depth First Search (DFS) on the connected component v is in. Only labels are changed.
	 * @see Graph#DFS(boolean)
	 * @see Graph#hasLoop(boolean)
	 * @see Graph#LoopFinderDFS(Vertex, boolean)
	 * @param v The vertex to start the DFS at.
	 * @param unDirectedGraph True if graph is undirected; false otherwise.
	 */
	public void ccDFS(Vertex v, boolean unDirectedGraph){

		
		this.vertexLabels.put(v, Label.EXPLORED);

		if(v.outDegree() == 0) return;
		for(Edge e : v.unexploredOutNeighbors(this.edgeLabels)){

			Vertex w = e.opposite(v);
			

			if(this.vertexLabels.get(w) == Label.UNEXPLORED){

				this.edgeLabels.put(e, Label.DISCOVERY);
				if(unDirectedGraph) this.edgeLabels.put(Edge.conjugate(e), Label.DISCOVERY);

				this.ccDFS(w,unDirectedGraph);
			}
			else{
				this.edgeLabels.put(e, Label.BACK);
				if(unDirectedGraph) this.edgeLabels.put(Edge.conjugate(e), Label.BACK); 
			}
			
		}
		
		
	}

//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
	
	/**
	 * Runs a DFS on the given graph and creates a new graph that contains only the vertices and the DISCOVERY edges. 
	 * @see Graph#DFS(boolean)
	 * @see Graph#ccDFS(Vertex, boolean)
	 * @param unDirectedGraph True if graph is undirected; false otherwise.
	 * @return A graph which is a spanning forest. Only DISCOVERY edges exist in the graph.
	 */
	public Graph spanningForest(boolean unDirectedGraph){
		
		this.resetGraphLabels();
		
		this.DFS(unDirectedGraph);
		
		Graph g = new Graph();
		
		for(Vertex v : this.vertices){
			g.vertices.add(v);
			g.vertexLabels.put(v, this.vertexLabels.get(v));
			g.V++;
		}
		
		for(Edge e : this.edges){
			if(this.edgeLabels.get(e) == Label.DISCOVERY){
				g.edges.add(e);
				g.edgeLabels.put(e, this.edgeLabels.get(e));
				g.E++;
			}
		}
		
		
		return g;
	}
	
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////

	/**
	 * Runs a reverse DFS, i.e. incoming edges are exploited instead of outgoing edges.
	 * @see Graph#isDirectedStronglyConnected()
	 * @param v Vertex to run reverse DFS on.
	 * @param unDirectedGraph True if graph is undirected; false otherwise.
	 */
	public void ccReverseDFS(Vertex v, boolean unDirectedGraph){
	
	
		this.vertexLabels.put(v, Label.EXPLORED);
		
		
		if(v.inDegree() == 0) return;
		for(Edge e : v.unexploredInNeighbors(this.edgeLabels)){
		
			Vertex w = e.opposite(v);
			
			
			if(this.vertexLabels.get(w) == Label.UNEXPLORED){
			
				this.edgeLabels.put(e, Label.DISCOVERY);
				if(unDirectedGraph) this.edgeLabels.put(Edge.conjugate(e), Label.DISCOVERY);
			
				this.ccReverseDFS(w,unDirectedGraph);
			}
			else{
				this.edgeLabels.put(e, Label.BACK);
				if(unDirectedGraph) this.edgeLabels.put(Edge.conjugate(e), Label.BACK); 
			}
		
		}
	
	
	}
	
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
	
	/**
	 * Finds the shortest unweighed path from s to v if there is one.
	 * @see Graph#BFS(boolean)
	 * @see Graph#ccBFS(Vertex, boolean)
	 * @param s The starting vertex.
	 * @param v The vertex whose distance from the starting the vertex has to be determined.
	 * @param unDirectedGraph True if graph is undirected; false otherwise.
	 * @return Cases: if s is v : 0; if s is isolated from v: -1; otherwise : distance from s to v.
	 */
	public int BFSShortestPath(Vertex s, Vertex v, boolean unDirectedGraph){

		//Case: s,v, and G do not exist in memory
		if(!Vertex.aVertex(s) || !Vertex.aVertex(v)) throw new InputMismatchException("Vertice(s) are invalid");
			
		//Sets vertex v as unexplored
		this.vertexLabels.put(s, Label.EXPLORED);
		
		//Case: If s and v are the same vertex return shortestpath of length 0
		if(s == v) return 0;
		
		//Case: s is the only vertex of the subgraph then v is unreachable
		//Shortestpath of length -1
		if(s.out.isEmpty()) return -1;
		
		//////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////
		//// Base cases check. Now to check for a shortestpath if it exists


		//ArrayList of ArrayList of Vertices: keeps track of the vertices of the current breadth level
		// and is used to relatively add vertices to next breadth level
		ArrayList< ArrayList<Vertex> >  levels = new ArrayList< ArrayList<Vertex> >();
		
		//Is used as a dummy for the syntax of java in terms of the ArrayList of ArrayList of Vertices
		//ArrayList only contains vertex s
		ArrayList<Vertex> incidence = new ArrayList<Vertex>();
		// vertex s is added to the level 0 of the breadth level
		incidence.add(s);
		
		// incidence0 is added to the ArrayList of ArrayList of Vertices to manage 
		// the breadth levels and run the BFS shortestpath algorithm
		levels.add(incidence);
		

		// Starts at level 0 and incremented by 1
		// TERMINATION CONDITION:
		// 1) i has exceeded the size of the ArrayList levels: ArrayOutOfBoundsException
		// 1) its not suppose to happen unless no incidence
		////////
		// 2) Sequence is empty: all vertices have been processed
		for(int i = 0; i < levels.size() && i < V; i++){

			//New ArrayList of vertices is created to store the i+1 breadth level vertices if any
			incidence = new ArrayList<Vertex>();
			levels.add(incidence);
			
	
			// For each loop for breadth level i: processes the vertices of breadth level i if any
			for(Vertex x : levels.get(i)){

				// Processes unexplored edges of the current iteration vertex x
				// Graph.incidentEdges returns an ArrayList of unexplored edges if any 
				for(Edge e : x.unexploredOutNeighbors(this.edgeLabels)){
					// Vertex w is connected to x through an unexplored edge
					Vertex w = e.opposite(x);

					//Checks w if unexplored: labels edges and vertices and assigns breadth level
					if(this.vertexLabels.get(w) == Label.UNEXPLORED){
						
						//Checks to see if w is v: if so return w's breadth 
						// level or shortestpath distance from s
						if(w==v){
							this.resetGraphLabels();
							return i+1;
						}
						//If w is not v continue process
						
						//Labels vertex w as explored
						this.vertexLabels.put(w, Label.EXPLORED);
						
						//Labels the incident edge of w and x as a discovery/tree edge
						this.edgeLabels.put(e, Label.DISCOVERY);
						if(unDirectedGraph) this.edgeLabels.put(Edge.conjugate(e), Label.DISCOVERY);
						
						
						
						//Add w to the next breadth level ArrayList for future processing
						incidence.add(w);
						
					}
					else{
						// label edge (x,w) as cross edge:
						// If w is already explored then edge is a cross edge
						this.edgeLabels.put(e, Label.CROSS);
						if(unDirectedGraph) this.edgeLabels.put(Edge.conjugate(e), Label.CROSS);
					}			
				}//Ends for(e) loop
				
			}//Ends for(x) loop
			
			if(levels.get(i+1).isEmpty()) break;
		}
		


		this.resetGraphLabels();
		return -1;
	}

//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
	
	/**
	 * Runs a Breadth First Search (BFS) on the given graph.
	 * @see Graph#BFS(boolean)
	 * @see Graph#BFSShortestPath(Vertex, Vertex, boolean)
	 * @param unDirectedGraph True if graph is undirected; false otherwise.
	 * @return Triple nested ArrayList of vertices for (general to specific) the connected components, the breadths of the search, the level of breadth during the search and the vertex.
	 */
	public ArrayList<ArrayList<ArrayList<Vertex>>> BFS(boolean unDirectedGraph){
		
		if(this.vertices.isEmpty()) return null;
		
		ArrayList< ArrayList< ArrayList<Vertex> > > graphs = new ArrayList<ArrayList<ArrayList<Vertex>>>(); 
		
		for(Vertex v : this.vertices)
			if(this.vertexLabels.get(v) == Label.UNEXPLORED)
				graphs.add(this.ccBFS(v, unDirectedGraph));
				
			
		return graphs;
	}

//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
	
	/**
	 * Searches the graph for any edges with negative weights if there are any.
	 * @see Graph#weightsAllEqual()
	 * @return True if any edge has a negative weight; false otherwise. 
	 */
	public boolean containsNegativeWeightEdges(){
		for(Edge e: this.edges)
			if(e.weight < 0)
				return true;
		
		return false;
	}

//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
	
	/**
	 * Determines whether all the edges have the same weight. Uses the Transitivity principle to determine this in linear runtime.
	 * @see Graph#containsNegativeWeightEdges()
	 * @return False if any edge doesn't have the same weight as its neighbor; otherwise true.
	 */
	public boolean weightsAllEqual(){
		for(int i =0; i<V-2; i++){
			if(edges.get(i) != edges.get(i+1))
				return false;
		}
		return true;
	}

//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
	
	/**
	 * Runs a BFS on the connected component containing v.
	 * @see Graph#BFS(boolean)
	 * @see Graph#BFSShortestPath(Vertex, Vertex, boolean)
	 * @param v The vertex to start the BFS at.
	 * @param unDirectedGraph True if graph is undirected; otherwise false.
	 * @return Double nested ArrayList of vertices for (general to specific) the levels of breadth, each breadth level and the vertices.
	 */
	public ArrayList< ArrayList<Vertex> > ccBFS(Vertex v, boolean unDirectedGraph){

		
		
		//Sets vertex v as unexplored
		this.vertexLabels.put(v, Label.EXPLORED);
		
		
		//////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////
		//// Base cases check. Now to check for a shortestpath if it exists

		//ArrayList of ArrayList of Vertices: keeps track of the vertices of the current breadth level
		// and is used to relatively add vertices to next breadth level
		ArrayList< ArrayList<Vertex> >  levels = new ArrayList< ArrayList<Vertex> >();
		
		//Is used as a dummy for the syntax of java in terms of the ArrayList of ArrayList of Vertices
		//ArrayList only contains vertex s
		ArrayList<Vertex> incidence = new ArrayList<Vertex>();
		// vertex s is added to the level 0 of the breadth level
		incidence.add(v);
		
		// incidence0 is added to the ArrayList of ArrayList of Vertices to manage 
		// the breadth levels and run the BFS shortestpath algorithm
		levels.add(incidence);
		
		//Case: v is makes one connected commponent
		if(v.out.isEmpty()) return levels;
		
		
		// Starts at level 0 and incremented by 1
		// TERMINATION CONDITION:
		// 1) i has exceeded the size of the ArrayList levels: ArrayOutOfBoundsException
		// 1) its not suppose to happen unless no incidence
		////////
		// 2) Sequence is empty: all vertices have been processed
		for(int i = 0; i < levels.size() && i < V; i++){

			//New ArrayList of vertices is created to store the i+1 breadth level vertices if any
			levels.add( (incidence = new ArrayList<Vertex>()) );
			
	
			// For each loop for breadth level i: processes the vertices of breadth level i if any
			for(Vertex x : levels.get(i)){

				// Processes unexplored edges of the current iteration vertex x
				// Graph.incidentEdges returns an ArrayList of unexplored edges if any 
				for(Edge e : x.unexploredOutNeighbors(this.edgeLabels)){
					// Vertex w is connected to x through an unexplored edge
					Vertex w = e.opposite(x);

					//Checks w if unexplored: labels edges and vertices and assigns breadth level
					if(this.vertexLabels.get(w) == Label.UNEXPLORED){
						
						//Labels vertex w as explored
						this.vertexLabels.put(w, Label.EXPLORED);
						
						//Labels the incident edge of w and x as a discovery/tree edge
						this.edgeLabels.put(e, Label.DISCOVERY);
						if(unDirectedGraph) this.edgeLabels.put(Edge.conjugate(e), Label.DISCOVERY);
						
						//Add w to the next breadth level ArrayList for future processing
						incidence.add(w);
						
					}
					else{
						// label edge (x,w) as cross edge:
						// If w is already explored then edge is a cross edge
						this.edgeLabels.put(e, Label.CROSS);
						if(unDirectedGraph) this.edgeLabels.put(Edge.conjugate(e), Label.CROSS);
					}			
				}//Ends for(e) loop
				
			}//Ends for(x) loop
			
			if(levels.get(i+1).isEmpty()) break;
		}
		


		return levels;
	}	

//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////

	public boolean containsMultipleEdge(){
		
		int V = this.vertices.size();
		int E = this.edges.size();
		
		
		
		Hashtable<Edge, Long> hashEL = new Hashtable<Edge, Long>();
		Hashtable<Long, Vertex> hashLV = new Hashtable<Long, Vertex>();
		
		for(Vertex v : this.vertices){
			
			String s = "";
			// Scanner scan = new Scanner();
			for(Edge e : v.out){
				
			}
			
		}
		
		return false;
	}
	
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////

	/**
	 * Checks to see if the graph has an edge that has the same origin and destination.
	 * @return True if there is an edge that creates a self loop; false otherwise.
	 */
	public boolean containsSelfLoop(){
		for(Edge e : this.edges)
			if(e.a == e.b)
				return true;
		
		return false;
	}
	
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////

	/**
	 * @see Graph#containsNegativeWeightEdges()
	 * @see Graph#containsNegativeWeightEdges()
	 * @return True if there are any edges with a weight not equal to zero; false otherwise.
	 */
	public boolean isWeighted(){
		for(Edge e :this.edges)
			if(e.weight != 0)
				return true;
		
		return false;
	}
	
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////

	/**
	 * 
	 * 
	 * @return
	 * @throws InvalidGraph
	 */
	public boolean isSimple() throws InvalidGraph{
		
		if(E > V*(V-1)/2) return false;
		if(this.isWeighted()) return false;
		if(this.isDirected()) return false;
		if(this.containsSelfLoop()) return false;

		
		
		return true;
	}

//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////

	/**
	 * Determines whether the given graph is a tree.
	 * @see Graph#hasLoop(boolean)
	 * @see Graph#isConnected()
	 * @param unDirectedGraph True if the graph is undirected; false otherwise.
	 * @return True if the given graph is connected and doesn't have any loops.
	 */
	public boolean isTree(boolean unDirectedGraph){
		if(!this.hasLoop(unDirectedGraph))
			if(this.isConnected())
				return true;
		
		return false;
	}
	
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
	
	/**
	 * Determines whether the graph is directed or one way.
	 * @return True if there is an edge that has no conjugate; false otherwise.
	 * @throws InvalidGraph If the graph does not have any edges, it can't be undirected or directed.
	 */
	public boolean isDirected() throws InvalidGraph{
		if(this.E == 0) throw new InvalidGraph("Graph does not contain any edges, can't tell if directed or undirected.");
		
		for(Edge e: this.edges)
			if(Edge.conjugate(e) == null)
				return true;
		
		return false;
	}

//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
	
	/**
	 * Determines whether the passed graph is a spanning subgraph of the given graph, i.e. they have the same set of vertices. 
	 * @see Graph#isConnected()
	 * @param g
	 * @return False if the number of vertices in g is not equal to those in the given graph or if they don't share the same set of vertices; true otherwise.
	 */
	public boolean isSpanningSubgraph(Graph g){
		
		if(this == g) return true;
		int count = 0;
		
		if(this.V == g.V)
			for(Vertex i : g.vertices)
				for(Vertex j : this.vertices)
					if(j == i) {
						count++;
						break;
					}
			
		else return false;
		
		if(count == this.V) return true;
		return false;
		
	}

//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
	
	/**
	 * Determines whether the given graph is a directed acyclic graph (DAG).
	 * @see Graph#isConnected()
	 * @see Graph#hasLoop(boolean)
	 * @return True if the given graph is directed and has no loops; false otherwise.
	 * @throws InvalidGraph If graph doesn't have any edges, it can't be directed or undirected.
	 */
	public boolean isDAG() throws InvalidGraph{
		if(this.isDirected())
			if(!this.hasLoop(false))
				return true;
		
		return false;
	}

//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
	
	/**
	 * Finds a topological order of the given graph in O(V) time.
	 * @see Graph#vertexWithNoOutgoingEdges()
	 * @return A Hashtable with a topological value (value) for each Vertex (key).
	 * @throws InvalidGraph If the graph is not a DAG it can't have a topological order.
	 */
	public Hashtable<Vertex, Integer> aTopologicalSort() throws InvalidGraph{
		
		if(!this.isDAG()) throw new InvalidGraph("Graph is either undirected or contains loop. Verify with isDag method");
		
		Graph g = new Graph(this);
		Hashtable<Vertex, Integer> topology = new Hashtable<Vertex, Integer>();
		int level = g.V;
		
		while(!g.isEmpty()){
			Vertex v = g.vertexWithNoOutgoingEdges();
			if(v == null) throw new InvalidGraph("Graph does not have a vertex with outDegree of 0");
			topology.put(v, level);
			level--;
			g.removeVertex(v);
		}
		
		
		return topology;
	}

//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////	
	
	/**
	 * Finds a vertex in the graph with no outgoing edges.
	 * @see Graph#aTopologicalSort()
	 * @return If there is one a Vertex with no outgoing edges.
	 */
	public Vertex vertexWithNoOutgoingEdges(){
		if(this.vertices.size() == 0) return null;
		
		for(Vertex v : this.vertices)
			if(v.outDegree() == 0) return v;
		
		return null;
	}
	
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
	
	/**
	 * 
	 * Determines whether the given graph is weakly connected.
	 * @return True if the graph is weakly connected; false otherwise.
	 * @throws InvalidGraph If the graph is not directed it can't be weakly connected.
	 */
	public boolean isDirectedWeaklyConnected() throws InvalidGraph{
		if(!this.isDirected()) throw new InvalidGraph("Graph is not directed. Use isConnected method instead.");
		
		if(this.isConnected()) return true;
		
		return false;
	}

//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
	
	/**
	 * Determines whether the directed graph is strongly connected or that it can reach all other vertices from the given vertex.
	 * @see Graph#isDirected()
	 * @see Graph#isDirectedWeaklyConnected()
	 * @see Graph#ccDFS(Vertex, boolean)
	 * @see Graph#ccReverseDFS(Vertex, boolean)
	 * @see Graph#resetGraphLabels()
	 * @return True if there is only one vertex or it is strongly connected; false otherwise.
	 * @throws InvalidGraph If the graph has no edges or vertices, or it's not directed it can't be strongly connected.
	 */
	public boolean isDirectedStronglyConnected() throws InvalidGraph{
		if(V == 1) return true;
		
		if(V == 0 || E == 0 ) throw new InvalidGraph("Graph is Empty"); 
		if(!this.isDirected()) throw new InvalidGraph("Graph is not directed. Use isConnected method instead.");
		
		Hashtable<Vertex,Label> vertexLabels = new Hashtable<Vertex,Label>();
		Hashtable<Edge,Label> edgeLabels = new Hashtable<Edge,Label>();
		
		for(Vertex v: this.vertices) vertexLabels.put(v, this.vertexLabels.get(v));
		for(Edge e: this.edges) edgeLabels.put(e, this.edgeLabels.get(e));
		
		this.resetGraphLabels();
		
		boolean tmp = true;
		
		this.ccDFS(this.vertices.get(0), false);
		
		if(this.vertexContainsLabel(Label.UNEXPLORED)) tmp = false;
		
		this.resetGraphLabels();
		
		this.ccReverseDFS(this.vertices.get(0), false);
		
		if(this.vertexContainsLabel(Label.UNEXPLORED)) tmp = false;
		
		for(Vertex v: this.vertices) this.vertexLabels.put(v, vertexLabels.get(v));
		for(Edge e: this.edges) this.edgeLabels.put(e, edgeLabels.get(e));
		
		
		return tmp;
	}

//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
	
	/**
	 * Determines whether the given graph is connected or weakly connected if the given graph is directed.
	 * @see Graph#explorer(Vertex)
	 * @see Graph#resetGraphLabels()
	 * @return False if there are no edges or vertices; True if there is one vertex or the graph is connected.
	 */
	public boolean isConnected(){
		if(V == 1) return true;
		if(V == 0 || E == 0 ) return false; 
		
		Hashtable<Vertex,Label> vertexLabels = new Hashtable<Vertex,Label>();
		Hashtable<Edge,Label> edgeLabels = new Hashtable<Edge,Label>();
		
		for(Vertex v: this.vertices) vertexLabels.put(v, this.vertexLabels.get(v));
		for(Edge e: this.edges) edgeLabels.put(e, this.edgeLabels.get(e));
		
		this.resetGraphLabels();
		
		this.explorer(this.getAVertex());
		
		boolean tmp = true;
		if(this.vertexContainsLabel(Label.UNEXPLORED)) tmp = false;
		
		for(Vertex v: this.vertices) this.vertexLabels.put(v, vertexLabels.get(v));
		for(Edge e: this.edges) this.edgeLabels.put(e, edgeLabels.get(e));
		
		return tmp;
	}

//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
	
	/**
	 * Produces a transitive closure of the given graph.
	 * @see Graph#resetGraphLabels()
	 * @param allowSelfLoop True if the method is allowed to produce self loops; otherwise false.
	 * @return A transitive closure of the given graph or null if there is less than 2 vertices or there are no edges.
	 */
	public Graph FWTransitiveClosure(boolean allowSelfLoop){
		if(this.V < 2 || this.E == 0) return null;
		
		Graph g = new Graph(this);
		
		for(Vertex v : g.vertices)
			if(v.inDegree() > 0 && v.outDegree() > 0)
				for(Edge i : v.in)
					for(Edge o : v.out)
						if(!i.a.isAdjacentTo(o.b)){
							if(i.a != o.b ) g.insertEdge(i.a, o.b);
							else if(allowSelfLoop) g.insertEdge(i.a, o.b);
						}
		
		g.resetGraphLabels();
		
		return g;
	}
	
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////

	/**
	 * Performs a DFS on all edges, incoming or outgoing, and labels all vertices and edges.
	 * @see Graph#isDirectedWeaklyConnected()
	 * @param v The vertex to explore next in the DFS
	 */
	public void explorer(Vertex v){		

		this.vertexLabels.put(v, Label.EXPLORED);

		for(Edge e : v.allUnexploredNeighbors(this.edgeLabels)){

			Vertex w = e.opposite(v);
			
			this.edgeLabels.put(e, Label.EXPLORED);

			if(this.vertexLabels.get(w) == Label.UNEXPLORED)
				this.explorer(w);
			
			
		}
		
	}

//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////

	/** 
	 * Runs Dijsktra's shortestpath on the given graph and determines the shortest path from s to all other vertices.
	 * @see Graph#BFSShortestPath(Vertex, Vertex, boolean)
	 * @see Graph#BellmanFordShortestPaths(Vertex)
	 * @see Graph#aBellmanFordShortestPathTree(Vertex)
	 * @see Graph#aDijkstraShortestPathTree(Vertex)
	 * @see Graph#containsNegativeWeightEdges()
	 * @see Graph#weightsAllEqual()
	 * @param s The shortestpath length is found from s to all other vertices.
	 * @return A Hashtable containing the length of shortest paths (value) from s to each other vertex (key).
	 * @throws DijkstraNegativeWeightEdge Dijkstra's Algorithm can't process negative weight edges because it violates the Triangle Inequality which is the basis for the Algorithm.
	 */
	public Hashtable<Vertex, Integer> DijkstraShortestPaths(Vertex s) throws DijkstraNegativeWeightEdge{
		if(s == null) return null;
		
		if(this.weightsAllEqual()) System.out.println("Better to run BFS-ShortestPath");
		if(this.containsNegativeWeightEdges()) throw new DijkstraNegativeWeightEdge("Graph contains negative weight edge");
		
		PriorityQueue<Vertex> qp = new PriorityQueue<Vertex>(10, new Comparator<Vertex>() {
			 
            public int compare(Vertex v1, Vertex v2) {
            	int i1 = v1.name;
            	int i2 = v2.name;
            	
            	
            	if(i1 > i2) return 1;
            	if(i1 < i2) return -1;
            	
                return 0;
            }
        });	
		
		
		Hashtable<Vertex,Integer> names = new Hashtable<Vertex, Integer>();
		
		for(Vertex v : this.vertices) names.put(v, v.name);
		
		for(Vertex v : this.vertices) v.name = Integer.MAX_VALUE;
		s.name = 0;
		
		for(Vertex v : this.vertices) qp.add(v);
		
		
		while(!qp.isEmpty()){
			Vertex u = qp.poll();
			
			for(Edge e : u.incidentOutEdges()){
				Vertex z = e.opposite(u);
				int r;
				
				if(u.name == Integer.MAX_VALUE) r = e.weight;
				else r = u.name + e.weight;
				
				
				if(r < z.name) z.name = r;
				
			}
		} // End While
		
		Hashtable<Vertex, Integer> distancesFromS = new  Hashtable<Vertex, Integer>();
		
		for(Vertex v : this.vertices) distancesFromS.put(v, v.name);
		
		for(Vertex v : this.vertices) v.name = names.get(v);
		
		return distancesFromS;
	}
	
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////

	/**
	 * Runs Dijkstra's shortestpath on the given graph and produces a shortestpath tree.
	 * @see Graph#containsNegativeWeightEdges()
	 * @see Graph#weightsAllEqual()
	 * @see Graph#DijkstraShortestPaths(Vertex)
	 * @see Graph#BellmanFordShortestPaths(Vertex)
	 * @see Graph#aBellmanFordShortestPathTree(Vertex)
	 * @param s The vertex used to produce its shortestpath tree to all other vertices.
	 * @return A shortestpaths tree from s.
	 * @throws DijkstraNegativeWeightEdge Dijkstra's Algorithm can't process negative weight edges because it violates the Triangle Inequality which is the basis for the Algorithm.
	 */
	public Graph aDijkstraShortestPathTree(Vertex s) {
		if(s == null) throw new NullPointerException("The passed vertex is null");
		
		if(this.weightsAllEqual()) System.out.println("Better to run BFS-ShortestPath or BellmanFordShortestPath");
		// if(this.containsNegativeWeightEdges()) throw new DijkstraNegativeWeightEdge("Graph contains negative weight edge");
		
		PriorityQueue<Vertex> qp = new PriorityQueue<Vertex>(10, new Comparator<Vertex>() {
		
			public int compare(Vertex v1, Vertex v2) {
				int i1 = v1.name;
				int i2 = v2.name;
				
				
				if(i1 > i2) return 1;
				if(i1 < i2) return -1;
				
				return 0;
			}
		});	
		
		Hashtable<Vertex,Edge> parents = new Hashtable<Vertex, Edge>();
		Hashtable<Vertex,Integer> names = new Hashtable<Vertex, Integer>();
		for(Vertex v : this.vertices) names.put(v, v.name);
		
		for(Vertex v : this.vertices) v.name = Integer.MAX_VALUE;
		s.name = 0;
		
		for(Vertex v : this.vertices) qp.add(v);
		
		
		while(!qp.isEmpty()){
			Vertex u = qp.poll();
			
			for(Edge e : u.incidentOutEdges()){
				Vertex z = e.opposite(u);
				int r;
				
				if(u.name == Integer.MAX_VALUE) r = e.weight;
				else r = u.name + e.weight;
				
				
				if(r < z.name) {
					z.name = r;
					parents.put(z, e);
				}
				
			}
		} // End While
		
		Graph g = new Graph();
		g.V = this.V;
		g.E = g.V-1;
		
		for(Vertex v : this.vertices) v.name = names.get(v);
		
		for(Vertex v : this.vertices) {
			g.vertices.add(v);
			g.vertexLabels.put(v, Label.UNEXPLORED);
			Edge e = parents.get(v);
			if(e != null) {
				g.edges.add(e);
				g.edgeLabels.put(e, Label.UNEXPLORED);
			}	
		}
		
		return g;
	}
	
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
	
	/**
	 * Runs BellmanFords shortestpath on the given graph and determines the shortest path from s to all other vertices.
	 * @see Graph#aBellmanFordShortestPathTree(Vertex)
	 * @see Graph#aDijkstraShortestPathTree(Vertex)
	 * @see Graph#DijkstraShortestPaths(Vertex)
	 * @param s The shortestpath length is found from s to all other vertices.
	 * @return A Hashtable containing the length of shortest paths (value) from s to each other vertex (key).
	 * @throws NullPointerException There is no shortestpath for a graph with no vertices.
	 */
	public Hashtable<Vertex,Integer> BellmanFordShortestPaths(Vertex s){
		if(s == null || this.V < 1) throw new NullPointerException("Either there are no vertices or s is null");
		
		Hashtable<Vertex,Integer> distances = new Hashtable<Vertex, Integer>();
		
		for(Vertex v : this.vertices) distances.put(v, Integer.MAX_VALUE);
		distances.put(s, 0);
		
		for(int i=1; i<this.V-1; i++){
			for(Edge e : this.edges){
				Vertex u = e.a;
				Vertex z = e.b;
				int r;
				if(distances.get(u) == Integer.MAX_VALUE) r = e.weight;
				else r = distances.get(u) + e.weight;
				
				if(r < distances.get(z)) distances.put(z, r);
			}
		}
		
		return distances;
	}
	
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
	
	/**
	 * Runs BellmanFords shortestpath on the given graph and produces a shortestpath tree.
	 * @see Graph#BellmanFordShortestPaths(Vertex)
	 * @see Graph#aDijkstraShortestPathTree(Vertex)
	 * @see Graph#DijkstraShortestPaths(Vertex)
	 * @param s The vertex used to produce its shortestpath tree to all other vertices.
	 * @return A shortestpaths tree from s.
	 */
	public Graph aBellmanFordShortestPathTree(Vertex s){
		if(s == null || this.V < 1) throw new NullPointerException("Either there are no vertices or s is null");
		
		Hashtable<Vertex,Integer> distances = new Hashtable<Vertex, Integer>();
		Hashtable<Vertex,Edge> parents = new Hashtable<Vertex, Edge>();
		
		for(Vertex v : this.vertices) distances.put(v, Integer.MAX_VALUE);
		distances.put(s, 0);
		
		for(int i=1; i<this.V-1; i++){
			for(Edge e : this.edges){
				Vertex u = e.a;
				Vertex z = e.b;
				int r;
				if(distances.get(u) == Integer.MAX_VALUE) r = e.weight;
				else r = distances.get(u) + e.weight;
				
				if(r < distances.get(z)) {
					distances.put(z, r);
					parents.put(z, e);
				}
			}
		}
		
		Graph g = new Graph();
		g.V = this.V;
		g.E = g.V-1;
		
		for(Vertex v : this.vertices) {
			g.vertices.add(v);
			g.vertexLabels.put(v, Label.UNEXPLORED);
			Edge e = parents.get(v);
			if(e != null) {
				g.edges.add(e);
				g.edgeLabels.put(e, Label.UNEXPLORED);
			}	
		}
		
		return g;
	}
	
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////

	/**
	 * Produces a Prim Jarnick Minimum Spanning Tree.
	 * @see Graph#aKruskalMinimumSpanningTree(boolean)
	 * @param unDirectedGraph True if the graph is undirected; false otherwise.
	 * @return A minimum spanning tree.
	 */
	public Graph aPJMinimumSpanningTree(boolean unDirectedGraph){
		if(this.E < 2 || this.V < 2) return null;
		
		Vertex s = this.getAVertex();
		
		
		Hashtable<Vertex,Integer> names = new Hashtable<Vertex, Integer>();
		for(Vertex v : this.vertices) names.put(v, v.name);
		for(Vertex v : this.vertices) v.name = Integer.MAX_VALUE;
		s.name = 0;
		
		
		PriorityQueue<Vertex> qp = new PriorityQueue<Vertex>(10, new Comparator<Vertex>() {
            public int compare(Vertex v1, Vertex v2) {
            	int i1 = v1.name;
            	int i2 = v2.name;
            	
            	if(i1 > i2) return 1;
            	if(i1 < i2) return -1;
            	
                return 0;
            }
        });	
		
		Hashtable<Vertex,Edge> parents = new Hashtable<Vertex, Edge>();
		this.resetGraphLabels();

		
		for(Vertex v : this.vertices) qp.offer(v);
		
		
		
		while(!qp.isEmpty()){
			
			Vertex u = qp.poll();
			
			for(Edge e : u.incidentOutEdges()){
				if(this.edgeLabels.get(e) == Label.UNEXPLORED){
					Vertex z = e.opposite(u);
					this.edgeLabels.put(e, Label.EXPLORED);
					if(unDirectedGraph) this.edgeLabels.put(Edge.conjugate(e), Label.EXPLORED);
					int r = e.weight;
					if(r < z.name){
						z.name = r;
						qp.remove(z);
						qp.add(z);
						parents.put(z, e);
					}
				}
			}
		}
		
		
		for(Vertex v : this.vertices) v.name = names.get(v);
		
		Graph g = new Graph();
		g.V = this.V;
		if(unDirectedGraph) g.E = (g.V-1)*2;
		else g.E = g.V-1;
		
		for(Vertex v : this.vertices) {
			g.vertices.add(v);
			g.vertexLabels.put(v, Label.UNEXPLORED);
			Edge e = parents.get(v);
			if(e != null) {
				g.edges.add(e);
				g.edgeLabels.put(e, Label.UNEXPLORED);
				if(unDirectedGraph) {
					Edge y = Edge.conjugate(e);
					g.edges.add(y);
					g.edgeLabels.put(y, Label.UNEXPLORED);
				}
			}
		}//End For
		
		return g;
	}
	
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
	
	/**
	 * Produces a Kruskal Minimum Spanning Tree.
	 * @see Graph#aPJMinimumSpanningTree(boolean)
	 * @param unDirectedGraph  True if the graph is undirected; false otherwise.
	 * @return A minimum spanning tree.
	 */
	public Graph aKruskalMinimumSpanningTree(boolean unDirectedGraph){
		if(this.E < 2 || this.V < 2) return null;
		
		Graph g = new Graph();
		g.V = this.V;
		if(unDirectedGraph) g.E = (g.V-1)*2; 
		else g.E = g.V-1;
		
		for(Vertex v : this.vertices) g.vertices.add(v);
		
		Hashtable<Vertex, ArrayList<Vertex>> clouds = new Hashtable<Vertex, ArrayList<Vertex>>();
		
		for(Vertex v : this.vertices){
			ArrayList<Vertex> cloud = new ArrayList<Vertex>();
			cloud.add(v);
			clouds.put(v, cloud);
		}
		
		PriorityQueue<Edge> qp = new PriorityQueue<Edge>(10, new Comparator<Edge>() {
			 
            public int compare(Edge e1, Edge e2) {
            	
            	
            	if(e1.weight > e2.weight) return 1;
            	if(e1.weight < e2.weight) return -1;
            	
                return 0;
            }
        });	
		
		for(Edge e : this.edges) qp.offer(e);

		
		while( g.edges.size() < g.E && !qp.isEmpty()){
			Edge e = qp.poll();
			
			Edge c = Edge.conjugate(e);
			if(unDirectedGraph) qp.remove(c);
			
			if(clouds.get(e.a) != clouds.get(e.b)){
				
				g.edges.add(e);
				if(unDirectedGraph) g.edges.add(c);
				
				ArrayList<Vertex> a = clouds.get(e.a);
				ArrayList<Vertex> b = clouds.get(e.b);
				
				
				for(int i = 0; i < a.size(); i++){
					Vertex v = a.get(i);
					b.add(v);
					clouds.put(v, b);
				}
				for(int i = 0; i< a.size(); i++) a.remove(i);
				
			}
		}// End While
		
		g.resetGraphLabels();
		return g;
	}
	
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
	
}//END CLASS
