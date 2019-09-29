package tango;

import edu.princeton.cs.algs4.*;

public class BreadthFirstPaths {
	private static final int INFINITY = Integer.MAX_VALUE;
	private boolean[] marked; // marked[v] = is there an s-v path
	private int[] edgeTo; // edgeTo[v] = previous edge on shortest s-v path
	private int[] distTo; // distTo[v] = number of edges shortest s-v path
	
	/**
	 * breadth first searching alg
	 * 
	 * @param takes a graph and int s
	 */
	public BreadthFirstPaths(Graph G, int s) {
		marked = new boolean[G.V()];
		distTo = new int[G.V()];
		edgeTo = new int[G.V()];
		validateVertex(s);
		bfs(G, s);
		// assert there is a path
		assert check(G, s);
	}
	/**
	 * breadth first searching alg for multiple ints
	 * 
	 * @param takes a graph a iterable list of integers
	 */
	public BreadthFirstPaths(Graph G, Iterable<Integer> sources) {
		marked = new boolean[G.V()];
		distTo = new int[G.V()];
		edgeTo = new int[G.V()];
		// for vertexs in graph set distance to infinity
		for (int v = 0; v < G.V(); v++)
			distTo[v] = INFINITY;
		// validate the vertex
		validateVertices(sources);
		// call bfs
		bfs(G, sources);
	}
	/**
	 * breadth first searching
	 * 
	 * @param takes a graph and a iterable integer list
	 */
	private void bfs(Graph G, Iterable<Integer> sources) {
		Queue<Integer> q = new Queue<Integer>();
		// for s in sources
		for (int s : sources) {
			// set marked at s to true and distance s to 0
			marked[s] = true;
			distTo[s] = 0;
			q.enqueue(s);
		}
		//while q is not empty
		while (!q.isEmpty()) {
			int v = q.dequeue();
			// for int in graph.adjacent
			for (int w : G.adj(v)) {
				// if marked at w is not true
				if (!marked[w]) {
					// set edgeat w to q.dequeue()
					edgeTo[w] = v;
					//set distance at w to dist at v plus 1
					distTo[w] = distTo[v] + 1;
					// set marked at w to true
					marked[w] = true;
					q.enqueue(w);
				}
			}
		}
	}
	/**
	 * breadth first searching
	 * 
	 * @param takes a graph and an int
	 */
	private void bfs(Graph G, int s) {
		Queue<Integer> q = new Queue<Integer>();
		// for distnace at v set to infinity
		for (int v = 0; v < G.V(); v++)
			distTo[v] = INFINITY;
		// set distance at s to 0
		distTo[s] = 0;
		// set marked at s to true
		marked[s] = true;
		q.enqueue(s);
		// while q is not empty
		while (!q.isEmpty()) {
			int v = q.dequeue();
			// for int w
			for (int w : G.adj(v)) {
				// if marked at w is false
				if (!marked[w]) {
					// set edge at w to v
					// det distance at w to distance at v + 1
					edgeTo[w] = v;
					distTo[w] = distTo[v] + 1;
					// set marked at w to true
					marked[w] = true;
					q.enqueue(w);
				}
			}
		}
	}
	/**
	 *  returns a boolean if there is a path
	 * 
	 * @param takes an int
	 * @return a boolean
	 */
	public boolean hasPathTo(int v) {
		// calles validate vertex at v
		validateVertex(v);
		// return marked at v
		return marked[v];
	}
	/**
	 *  returns a int for distance 
	 * 
	 * @param takes an int
	 * @return an int
	 */
	public int distTo(int v) {
		// calls validate vertex at v
		validateVertex(v);
		// return distnace at v
		return distTo[v];
	}
	/**
	 *  returns multiple paths to
	 * 
	 * @param takes an int
	 * @return a list of integer
	 */
	public Iterable<Integer> pathTo(int v) {
		// if there is no path at v
		validateVertex(v);
		// return null
		if (!hasPathTo(v))
			return null;
		Stack<Integer> path = new Stack<Integer>();
		int x;
		// for x call path.push at x
		for (x = v; distTo[x] != 0; x = edgeTo[x])
			path.push(x);
		// call path.push again
		path.push(x);
		//return path
		return path;
	}
	/**
	 *  checks if theres a path
	 * 
	 * @param takes an int
	 * @param takes a graph
	 * @return a boolean
	 */
	private boolean check(Graph G, int s) {
		// if distnace at s does not equal 0
		if (distTo[s] != 0) {
			StdOut.println("distance of source " + s + " to itself = " + distTo[s]);
			//return false
			return false;
		}
		// for int v 
		for (int v = 0; v < G.V(); v++) {
			//for int w
			for (int w : G.adj(v)) {
				// if path to v does not equal path to w
				if (hasPathTo(v) != hasPathTo(w)) {
					StdOut.println("edge " + v + "-" + w);
					StdOut.println("hasPathTo(" + v + ") = " + hasPathTo(v));
					StdOut.println("hasPathTo(" + w + ") = " + hasPathTo(w));
					// return false
					return false;
				}
				// if there is a path to v and distance to w is greater than distance to v + 1
				if (hasPathTo(v) && (distTo[w] > distTo[v] + 1)) {
					StdOut.println("edge " + v + "-" + w);
					StdOut.println("distTo[" + v + "] = " + distTo[v]);
					StdOut.println("distTo[" + w + "] = " + distTo[w]);
					// return false
					return false;
				}
			}
		}
		// for int w
		for (int w = 0; w < G.V(); w++) {
			// if there is no path to w and w == s continue
			if (!hasPathTo(w) || w == s)
				continue;
			int v = edgeTo[w];
			// if distnace to w does not equal distnace to v + 1
			if (distTo[w] != distTo[v] + 1) {
				StdOut.println("shortest path edge " + v + "-" + w);
				StdOut.println("distTo[" + v + "] = " + distTo[v]);
				StdOut.println("distTo[" + w + "] = " + distTo[w]);
				// return false
				return false;
			}
		}
		// return true
		return true;
	}
	/**
	 *  validates a vertex given a v
	 * 
	 * @param takes an int
	 * @throws an IllegalArgumentException if not valid
	 */
	private void validateVertex(int v) {
		int V = marked.length;
		// throw exception
		if (v < 0 || v >= V)
			throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
	}
	/**
	 *  validates vertecies given a list
	 * 
	 * @param takes list of integer vertecies
	 * @throws an IllegalArgumentException if not valid
	 */
	private void validateVertices(Iterable<Integer> vertices) {
		// throw exception
		if (vertices == null) {
			throw new IllegalArgumentException("argument is null");
		}
		int V = marked.length;
		// throw exception
		for (int v : vertices) {
			if (v < 0 || v >= V) {
				throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
			}
		}
	}

}
