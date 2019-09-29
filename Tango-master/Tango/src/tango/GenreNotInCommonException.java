package tango;
// suppress warning for genre not in common exception
@SuppressWarnings("serial")
public class GenreNotInCommonException extends Exception {
	public GenreNotInCommonException(String errorMessage) {
		super(errorMessage);
	}
}
