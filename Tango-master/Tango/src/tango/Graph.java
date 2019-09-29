package tango;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

public class Graph {
	private final int V; // number of vertices
	private int E; // number of edges
	private List<Integer>[] adj; // adjacency lists
	public List<Genre>[] likedGenres;

	public Graph(int V) {
		this.V = V;
		this.E = 0;
		adj = (List<Integer>[]) new List[V]; // Create array of adjacency lists.
		likedGenres = (List<Genre>[]) new List[V]; // Create array of liked genre lists.
		for (int v = 0; v < V; v++) // Initialize all lists
			adj[v] = new ArrayList<Integer>(); // to empty.
	}

	/**
	 * Creates a Graph when you give it the number of vertices, a file with all the
	 * edges, and a file of the genres.
	 * 
	 * @param V                   The number of vertices in the graph.
	 * @param edgesFile           A String representing the file with all the
	 *                            Graph's edges.
	 * @param genresFile          A String representing the file with all the
	 *                            Graph's users' liked genres.
	 * @param genreSimilarityFile A String representing the JSON file with the
	 *                            similarity indices between individual Genres.
	 */
	public Graph(int V, String edgesFile, String genresFile, String genreSimilarityFile) {
		this(V);
		this.readGenreInfo(genresFile);
		Genre.readGenreSimilarities(genreSimilarityFile);
		File file = new File(edgesFile);
		BufferedReader br;

		try {
			FileReader fr = new FileReader(file);
			br = new BufferedReader(fr);
			String line = br.readLine();

			// Skip first line (header info)
			line = br.readLine();
			while (line != null) {
				String[] contents = line.split(",");
				this.addEdge(Integer.parseInt(contents[0]), Integer.parseInt(contents[1]));
				line = br.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads the liked genres for each user from a JSON file.
	 * 
	 * @param fileName A String representing the JSON file.
	 */
	private void readGenreInfo(String fileName) {
		try {
			// parsing file "JSONExample.json".
			Object obj = new JSONParser().parse(new FileReader(fileName));

			// typecasting obj to JSONObject.
			JSONObject jo = (JSONObject) obj;

			// Go through all vertices in the Graph.
			for (int i = 0; i < adj.length; i++) {
				// Read list of genres as ArrayList of String from file.
				ArrayList<String> strList = (ArrayList<String>) jo.get(String.valueOf(i));

				// Create new ArrayList of Genre.
				ArrayList<Genre> genreList = new ArrayList<Genre>();

				// Convert each String to Genre representation.
				for (String genreStr : strList) {
					genreList.add(new Genre(genreStr));
				}

				// Save genreList for vertex.
				this.likedGenres[i] = genreList;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns a list of a given user's liked genres.
	 * 
	 * @param nodeID The integer that is the user's ID
	 * @return A list of the user's liked genres.
	 */

	/**
	 * Returns a list of a given user's liked genres.
	 * 
	 * @param nodeID The integer that is the user's ID
	 * @return A list of the user's liked genres.
	 */
	public List<Genre> genres(int nodeID) {
		return this.likedGenres[nodeID];
	}

	/**
	 * Returns the amount of vertices in the undirected graph
	 * 
	 * @return V the amount of vertices in the graph
	 */
	public int V() {
		return V;
	}

	/**
	 * Returns the amount of edges in the undirected graph
	 * 
	 * @return E the number of edges in the graph
	 */
	public int E() {
		return E;
	}

	/**
	 * Adds an undirected edge between two nodes into the graph
	 * 
	 * @param v A node whose edge is to be added
	 * @param w A node whose edge is to be added
	 */

	public void addEdge(int v, int w) {
		adj[v].add(w); // Add w to vâ€™s list.
		adj[w].add(v); // Add v to wâ€™s list.
		E++;
	}

	/**
	 * Returns the nodes adjacent to a given node
	 * 
	 * @param v The node who's adjacent nodes are to be returned
	 * @return A list of the user's liked genres.
	 */
	public List<Integer> adj(int v) {
		return adj[v];
	}

	/**
	 * Used to sort friends by how similar their liked genres are to the user.
	 * 
	 * @param user    A node integer of the user.
	 * @param friend1 A node integer of the first friend.
	 * @param friend2 A node integer of the second friend.
	 * @return An integer -1 if friend1's musical tastes are less similar to the
	 *         user's than friend2's; 0 if friend1's and friend2's musical tastes
	 *         are equally similar to the user's; 1 if friend2's musical tastes are
	 *         more similar to the user's than friend1's.
	 */
	public int compareUserAndFriends(int user, int friend1, int friend2) {
		// Compare the user's genres against both friends.
		if (Genre.similarityOfGenreLists(likedGenres[user], likedGenres[friend1]) < Genre
				.similarityOfGenreLists(likedGenres[user], likedGenres[friend2])) {
			return -1;
		} else if (Genre.similarityOfGenreLists(likedGenres[user], likedGenres[friend1]) > Genre
				.similarityOfGenreLists(likedGenres[user], likedGenres[friend2])) {
			return 1;

			// If both friends' tastes are equally similar to the user's return the friend
			// the shorter genre list.
		} else {
			if (likedGenres[friend1].size() < likedGenres[friend2].size()) {
				return 1;
			} else if (likedGenres[friend1].size() == likedGenres[friend2].size()) {
				return 0;
			} else {
				return -1;
			}
		}

	}

	/**
	 * Returns in ascending order how similar the genres of a user is to their
	 * friends
	 * 
	 * @param user The integer that is the user's ID
	 * @return Each friend of a user and percentage of how similar their liked
	 *         genres are
	 */
	public List<Integer> sortByHowSimilar(int user) {
		List<Integer> friends = new ArrayList();
		friends.addAll(adj[user]);

		System.out.println("User " + user + "\'s liked genres:");
		System.out.println(likedGenres[user]);
		System.out.println("User " + user + "'s friends:");
		System.out.println(adj(user));

		System.out.println("\n----FRIENDS BEFORE SORT----");
		for (int i : friends) {
			System.out.println("\nFriend " + i + "\'s liked genres:");
			System.out.println(likedGenres[i]);
		}

		friends.sort((friend1, friend2) -> compareUserAndFriends(user, friend1, friend2));

		System.out.println("\n----FRIENDS AFTER SORT----");
		for (int i : friends) {
			System.out.println("\nFriend " + i + "\'s liked genres:");
			System.out.println(likedGenres[i]);
			System.out.println("Total similarity to user " + user + "\'s musical tastes: "
					+ Genre.similarityOfGenreLists(likedGenres[user], likedGenres[i]) * 100 + "%");
		}

		System.out.println("\nSorted Friends: " + friends);
		return friends;

	}

	class genrenum {
		double rollno;
		Genre name;

		// Constructor
		public genrenum(double rollno, Genre name) {
			this.rollno = rollno;
			this.name = name;

		}

		// Used to print student details in main()
		public String toString() {
			return this.rollno + " " + this.name;

		}

	}

	class Sortbyroll implements Comparator<genrenum> {
		// Used for sorting in ascending order of
		// roll number
		public int compare(genrenum a, genrenum b) {
			if (a.rollno < b.rollno) {
				return -1;
			} else if (a.rollno == b.rollno) {
				return 0;
			} else {
				return 1;
			}
		}
	}

	/**
	 * Sorts friends by genre similarity
	 * 
	 * @param user A list of genres.
	 * @param n    The index of the sublist
	 * @return A sort in descending order.
	 */
	private List<genrenum> sortByHowSimilarGenre(List<Integer> user, int n) {
		List<Genre> friends = new ArrayList();
		for (int i = 0; i < user.size(); i++) {
			friends.addAll(likedGenres[user.get(i)]);
			System.out.println("User " + user.get(i) + "\'s liked genres: " + likedGenres[user.get(i)]);
		}

		System.out.println("\nGenres ranked by how much all friends like them:");
		double num = 0;
		ArrayList<genrenum> ar = new ArrayList<genrenum>();
		for (int j = 0; j < friends.size(); j++) {
			for (int i = 0; i < friends.size(); i++) {
				num = num + (Genre.howSimilars(friends.get(i), friends.get(j)));
			}
			ar.add(new genrenum(num, friends.get(j)));
			num = 0;
		}
		ArrayList<genrenum> newList = new ArrayList<genrenum>();

		ArrayList<Genre> nn = new ArrayList<Genre>();

		Collections.sort(ar, new Sortbyroll());
		Collections.reverse(ar);

		for (int j = 0; j < ar.size(); j++) {

			if (!(check(nn, ar.get(j).name))) {

				nn.add((ar.get(j)).name);
				newList.add(ar.get(j));
			}

		}

		List<genrenum> rr = newList.subList(0, n);
		return (rr);
	}

	/**
	 * Compares one genre against a list of other genres
	 * 
	 * @param nn A list of genres.
	 * @param n  A genre
	 * @return Whether they are the same
	 */
	public Boolean check(ArrayList<Genre> nn, Genre n) {
		for (int i = 0; i < nn.size(); i++) {

			if (String.valueOf(n).equals(String.valueOf((nn.get(i))))) {

				return true;
			}
		}
		return false;
	}

	/**
	 * Finds the friend with the most similar music tastes to the user's.
	 * 
	 * @param user The user's NodeID.
	 * @return The NodeID of the friend most in common.
	 */
	public int friendMostInCommon(int user) {
		List<Integer> sortedFriends = sortByHowSimilar(user);
		return sortedFriends.get(sortedFriends.size() - 1);
	}

	/**
	 * Finds n-most genres in common between a group of users.
	 * 
	 * @param user A list of users.
	 * @param n    How many top genres to return.
	 * @return The top genres in descending order.
	 */
	public List<Genre> genreMostInCommon(List<Integer> user, int n) {
		List<genrenum> genresRanked = sortByHowSimilarGenre(user, n);
		System.out.println(genresRanked);
		return genresRanked.stream().map(genre -> genre.name).collect(Collectors.toList());
	}

	/**
	 * Returns the closest person in the users friends network that likes a given
	 * genre
	 * 
	 * @param nodeID The integer that is the user's ID
	 * @return A closest user that likes the given genre
	 */
	public int searchClosest(int nodeID, Genre genre) throws GenreNotInCommonException {
		int s = nodeID;
		BreadthFirstPaths bfs = new BreadthFirstPaths(this, nodeID);
		int lowestDepth = this.V() + 1;
		int closestFriend = this.V() + 1;
		for (int v = 0; v < this.V(); v++) { // Iterates through all vertices, excludes itself
			if (bfs.hasPathTo(v)) { // If a path to another node exists
				for (Genre a : this.genres(v)) {
					if (a.toString().contentEquals((genre.toString()))) {
						// Checks if the found user likes the given
						// genre
						if (bfs.distTo(v) < lowestDepth && bfs.distTo(v) > 1) {
							// If the depth to the found user is
							// less than the current, they become
							// the closest friend

							closestFriend = v;
							lowestDepth = bfs.distTo(v);
						}
					}
				}
			}
		}
		// If the initialized value of closestFriend remains unchanged throw an
		// exception
		if (closestFriend == this.V() + 1) {
			throw new GenreNotInCommonException("No one in your friend circle likes this genre");

		}
		// Prints out every node in the path
		for (int x : bfs.pathTo(closestFriend)) {
			if (x == s)
				System.out.print(x);
			else
				System.out.print("-" + x);
		}
		System.out.println();
		System.out.printf("The closest person in your friend network who likes %s is  %d", genre, closestFriend);
		System.out.println();
		// Returns the ID of the closest friend
		return closestFriend;

	}

	/**
	 * Returns the closest person in the users friends network that likes all the
	 * given genres
	 * 
	 * @param nodeID The integer that is the user's ID
	 * @return A list of the closest friend's NodeID and a string representing a
	 *         path to the user.
	 */
	public List searchClosest(int nodeID, List<Genre> genres) throws GenreNotInCommonException {
		int s = nodeID;

		// Adds every genre in the given list to a set
		Set<String> genreSet = new HashSet<String>();
		for (Genre x : genres) {
			genreSet.add(x.toString());
		}
		BreadthFirstPaths bfs = new BreadthFirstPaths(this, nodeID);
		int lowestDepth = this.V() + 1;
		int closestFriend = this.V() + 1;

		// Iterates through all vertices, excludes itself
		for (int v = 0; v < this.V(); v++) {
			if (bfs.hasPathTo(v)) {

				// Creates a set of liked genres for each connected node and compares it to the
				// given genres
				Set<String> set1 = new HashSet<String>();
				for (Genre a : this.genres(v)) {
					if (genreSet.contains(a.toString())) {
						set1.add(a.toString());
					}
					// If a person likes all genres from the given list
					if (set1.containsAll(genreSet)) {
						if (bfs.distTo(v) < lowestDepth && bfs.distTo(v) > 1) {
							// If the depth to the found user is
							// less than the current, they become
							// the closest friend
							closestFriend = v;
							lowestDepth = bfs.distTo(v);
						}
					}
				}
			}
		}
		// If no other connected node likes the given genres throw exception
		if (closestFriend == this.V() + 1) {
			throw new GenreNotInCommonException("No one in your friend circle likes this genre");

		}

		// Constructing path
		String path = "";
		for (int x : bfs.pathTo(closestFriend)) {
			if (x == s)
				// System.out.print(x);
				path += x;
			else
				// System.out.print("-" + x);
				path += "-" + x;
		}

		// Return NodeID of closest friend and path to user.
		return Arrays.asList(closestFriend, path);

	}

	/**
	 * Finds friends who like a particular genre.
	 * 
	 * @param user The user's NodeID.
	 * @param g    A particular genre.
	 * @return A list of the friends' NodeIDs.
	 */
	public List<Integer> friendsWhoLike(int user, Genre g) {
		List<Integer> res = new ArrayList<Integer>();
		for (int f : adj(user)) {
			if (genres(f).contains(g)) {
				res.add(f);
			}
		}
		return res;
	}

	/**
	 * Returns a list of NodeIDs of the friends who like all genres in gs.
	 * 
	 * @param user The user's NodeID.
	 * @param gs   The list of genres.
	 * @return A list of the friends' NodeIDs.
	 */
	public List<Integer> friendsWhoLikeAll(int user, List<Genre> gs) {
		// Create duplicate of friends
		List<Integer> res = new ArrayList();
		res.addAll(adj(user));

		// Go through each genre
		for (Genre g : gs) {
			// Get the friends who like this genre
			List<Integer> friendsWhoLikeG = friendsWhoLike(user, g);

			// Take the intersection with remaining friends
			res.retainAll(friendsWhoLikeG);
		}

		// The final intersection will be the friends who like all genres
		return res;
	}

	/**
	 * Returns a list of NodeIDs of the friends who like any genre in gs.
	 * 
	 * @param user The user's NodeID.
	 * @param gs   The list of genres.
	 * @return A list of the friends' NodeIDs.
	 */
	public List<Integer> friendsWhoLikeAny(int user, List<Genre> gs) {
		Set<Integer> res = new HashSet<Integer>();

		// Go through each genre
		for (Genre g : gs) {
			// Get the friends who like this genre
			List<Integer> friendsWhoLikeG = friendsWhoLike(user, g);

			// Take the union
			res.addAll(friendsWhoLikeG);
		}

		// The final union will be the friends who like any genre
		return (List<Integer>) res.stream().collect(Collectors.toList());
	}
}
