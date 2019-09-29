package tango;
@SuppressWarnings("serial")
public class NoFriendInCommonException extends Exception {
	public NoFriendInCommonException(String errorMessage) {
		super(errorMessage);
	}
}
