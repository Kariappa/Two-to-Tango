package tango;

@SuppressWarnings("serial")
public class BadArgumentsException extends Exception {
	private static String message = "Invalid arguments passed. I was expecting:\nCountryCode NodeID Mode Args\n\twhere:\n\t\tCountry Code: 2-letter string\n\t\t\tHU \t(Hungary)\n\t\t\tHR \t(Croatia)\n\t\t\tRO \t(Romania)\n\n\t\tNodeID: natural number\n\t\t\tHU Max NodeID: 47537\n\t\t\tHR Max NodeID: 54572\n\t\t\tRO Max NodeID: 41772\n\n\t\tMode:\n\t\t\tSortFriends\n\t\t\tConcertBuddy GenreListString\n\t\t\tSearchByGenres MatchMode GenreListString\n\t\t\tPartyGenres TopNumber NodeIDListString\n\t\t\t\twhere:\n\t\t\t\tMatchMode: MatchAll/MatchAny\n\t\t\t\tTopNumber: int > 0\n\t\t\t\tGenreListString: Genre strings separated by comma and space (must wrap in quotes if more than 1 genre)\n\t\t\t\tGenreListString: NodeID strings separated by comma and space (must wrap in quotes if more than 1 NodeID)";

	public BadArgumentsException() {
		super(message);
	}
}