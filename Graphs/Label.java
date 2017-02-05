


/**
 * Used with a Hashtable to keep track whether a given vertex or edge has been visited and also to create a discovery tree.
 * @author vincenzorm117
 * @version 1.0
 * @see Graph#edgeLabels
 * @see Graph#vertexLabels
 *
 */
public enum Label {

	/**
	 * Signifies that an object has not been visited.
	 */
	UNEXPLORED{
		public String toString(){return "UNXPLRD";}
	},
	/**
	 * Signifies that an object has been visited
	 */
	EXPLORED{
		public String toString(){return "EXPLRD";}
	},
	/**
	 * Marks an edge as containing a vertex that has not been explored.
	 * Used only on edges with the BFS method in conjunction with UNEXPLORED and DISCOVERY Labels.
	 */
	CROSS{
		public String toString(){return "CRSS";}
	},
	/**
	 * Marks an edge as containing a vertex that has been explored.
	 * Used only on edges with the DFS method in conjunction with UNEXPLORED and DISCOVERY Labels.
	 */
	BACK{
		public String toString(){return "BCK";}
	},
	/**
	 * Marks an edge as containing a vertex that has not been explored.
	 * Used only on edges with the DFS or BFS.
	 */
	DISCOVERY{
		public String toString(){return "DISVR";}
	};

}

