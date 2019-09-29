package tango;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Genre {
	// List of genres supported.
	public static List<String> genreList = Arrays.asList("Rock & Roll/Rockabilly", "Tropical", "Classic Blues", "Bluegrass",
			"TV Soundtracks", "Baroque", "Urban Cowboy", "Indie Rock", "Romantic", "Country", "Nursery Rhymes",
			"Classical Period", "East Coast", "Electro", "Trance", "Delta Blues", "Old school soul", "Kids & Family",
			"Contemporary R&B", "Rock", "Dance", "Brazilian Music", "Comedy", "Pop", "Electro Pop/Electro Rock",
			"Grime", "Asian Music", "Modern", "Folk", "Bollywood", "Ranchera", "Contemporary Soul", "Game Scores",
			"Ska", "TV shows & movies", "Opera", "Rap/Hip Hop", "Jazz Hip Hop", "Instrumental jazz", "Soundtracks",
			"Sports", "Films/Games", "Metal", "Reggae", "Dubstep", "International Pop", "Alternative Country",
			"Electric Blues", "Dub", "Jazz", "Dancehall/Ragga", "Hard Rock", "Indian Music", "Classical", "Indie Pop",
			"Blues", "Soul & Funk", "Early Music", "West Coast", "Bolero", "Acoustic Blues", "Indie Rock/Rock pop",
			"Indie Pop/Folk", "Disco", "Spirituality & Religion", "R&B", "African Music", "Latin Music", "Kids",
			"Country Blues", "Electro Hip Hop", "Alternative", "Film Scores", "Vocal jazz", "Dirty South",
			"Traditional Country", "Old School", "Singer & Songwriter", "Dancefloor", "Chicago Blues", "Oldschool R&B",
			"Techno/House", "Chill Out/Trip-Hop/Lounge", "Musicals", "Norteño", "Corridos", "Stories", "Renaissance");
	
	private static Map<String, Map<String, Double>> similarityTable; // Gives a score of how similar two genres are.
	private String g; // Stores the current genre.

	/**
	 * Initializes a genre from a given string
	 * 
	 * @param s String representing a genre
	 * @exception Throws IOException if the given genre is not a valid one
	 */
	public Genre(String s) throws IOException {
		if (genreList.contains(s)) {
			this.g = s;
		} else {
			throw new IOException(s + "Given string is not a valid genre.");
		}
	}

	/**
	 * Returns a string representation of a genre
	 * @return String representation of a genre
	 */
	public String toString() {
		return this.g;
	}
	

	/**
	 * Reads the similarities of genres from a given file
	 * @param fileName  A JSON file containing each genre a percentage similarity
	 * 					compared to all other genres
	 */
	public static void readGenreSimilarities(String fileName) {
		try {
			// parsing file "JSONExample.json". 
	        Object obj = new JSONParser().parse(new FileReader(fileName)); 
	          
	        // typecasting obj to JSONObject. 
	        JSONObject jo = (JSONObject) obj; 
	        
	        // typecasting JSONObject to Map of Map.
	        similarityTable = (Map<String, Map<String, Double>>) jo;
	        //System.out.println(m.get("International Pop"));
	    
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns how similar a genre is to this genre
	 * @param p The genre to compare this one to
	 * @return The decimal value of how close these genres are related
	 */
	public double howSimilar(Genre p) {
		return similarityTable.get(this.g).get(p.g);
	}
	
	/**
	 * Returns how similar two genres are to each other
	 * @param p One genre to be compared with
	 * @param x One genre to be compared to
	 * @return The decimal value of how close these genres are related
	 */
	public static double howSimilars(Genre p, Genre x) {
		return similarityTable.get(p.g).get(x.g);
	}
	
	/**
	 * Returns how similar a two lists of genres are to each other
	 * @param list1 A list of genres to be compared to
	 * @param list2 A list of genres to be compared with
	 * @return The decimal value of how close these lists of genres are related
	 */
	public static double similarityOfGenreLists(List<Genre> list1, List<Genre> list2) {
		double totalSimilarity = 0;
		
		// Iterate through the list1 (the user's genres).
		for (Genre gFromL1 : list1) {
			// Finds the highest similarity index of a current Genre from list1 with any Genre in list2 (the friend's genre).
			double mostSimilarToG = list2.stream().mapToDouble(gFromL2 -> gFromL1.howSimilar(gFromL2)).max().getAsDouble();
			totalSimilarity += mostSimilarToG;
		}
		
		// Return the average by using the user list's size.
		return totalSimilarity / list1.size();
	}
	
	
	/**
	 * Equality of genres based on string-to-string.
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Genre)
			if (this.g.equals(((Genre) o).g)) {
				return true;
			}
		return false;
	}
}
