package tango;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.simple.JSONValue;

public class CmdInterface {
	/**
	 * The external interface for Tango uses the command line.
	 * 
	 * @param args The arguments passed to the interface loader.
	 * @throws IOException               When there is an issue reading from a data
	 *                                   file.
	 * @throws NumberFormatException     When there is an issue parsing the
	 *                                   arguments or the contents of the files into
	 *                                   numbers.
	 * @throws BadArgumentsException     When the passed arguments are invalid.
	 * @throws GenreNotInCommonException When there does not exist a common genre
	 *                                   among friends.
	 */
	public static void main(String args[]) throws BadArgumentsException, NumberFormatException, GenreNotInCommonException, IOException {
		Hashtable<String, Integer> dataSetSizes = new Hashtable<String, Integer>();
		dataSetSizes.put("RO", 41773);
		dataSetSizes.put("HR", 54573);
		dataSetSizes.put("HU", 47538);
		if (args.length < 3) {
			throw new BadArgumentsException();
		} else {
			Graph g = new Graph(dataSetSizes.get(args[0]), "data/" + args[0] + "_edges.csv",
					"data/" + args[0] + "_genres.json", "data/genreSimilarity.json");

			// ConcertBuddy mode
			if (args[2].equals("ConcertBuddy")) {
				if (args.length != 4) {
					throw new BadArgumentsException();
				}
				List<String> genreStrList = Arrays.asList(args[3].split(", "));
				List<Genre> genreList = new ArrayList<Genre>();
				for (String s : genreStrList) {
					genreList.add(new Genre(s));
				}

				List L = g.searchClosest(Integer.parseInt(args[1]), genreList);
				Map output = new HashMap();
				output.put("concertBuddyID", L.get(0));
				output.put("path", L.get(1));
				System.out.println(JSONValue.toJSONString(Arrays.asList(output)));
			}

			// SearchByGenres mode
			if (args[2].equals("SearchByGenres")) {
				if (args.length != 5) {
					throw new BadArgumentsException();
				}
				List<String> genreStrList = Arrays.asList(args[4].split(", "));
				List<Genre> genreList = new ArrayList<Genre>();
				for (String s : genreStrList) {
					genreList.add(new Genre(s));
				}

				List<Integer> L = new ArrayList();
				if (args[3].equals("MatchAll")) {
					L = g.friendsWhoLikeAll(Integer.parseInt(args[1]), genreList);
				} else {
					if (args[3].equals("MatchAny")) {
						L = g.friendsWhoLikeAny(Integer.parseInt(args[1]), genreList);
					}
				}

				List<Map> people = new ArrayList<Map>();
				for (int id : L) {
					Map person = new HashMap();
					person.put("person_id", String.valueOf(id));
					String genresString = g.genres(id).toString().substring(1, g.genres(id).toString().length() - 1);
					person.put("person_genre", genresString);
					people.add(person);
				}
				Map output = new HashMap();
				output.put("people", people);
				System.out.println(JSONValue.toJSONString(output));
			}

			// SortFriends mode
			if (args[2].equals("SortFriends")) {
				if (args.length != 3) {
					throw new BadArgumentsException();
				}
				g.sortByHowSimilar(Integer.parseInt(args[1]));
			}

			if (args[2].equals("PartyGenres")) {
				if (args.length != 5) {
					throw new BadArgumentsException();
				}
				String message = "";
				if (Integer.parseInt(args[3]) == 1) {
					message = "\nThis is the top genre to play: ";
				} else {
					message = "\nThese are the " + args[3] + " top genres to play (in descending order): ";
				}

				System.out.println(message + (g.genreMostInCommon(Arrays.asList(args[4].split(", ")).stream()
						.map(s -> Integer.parseInt(s)).collect(Collectors.toList()), Integer.parseInt(args[3]))));
			}
		}
	}
}
