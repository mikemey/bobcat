package uk.mm.bobcat.service;

/**
 * @author michael
 *
 */
public class BobcatServiceException extends RuntimeException {
	private static final long serialVersionUID = -657468672212471938L;
	private final BobcatError reason;

	public BobcatServiceException(BobcatError reason) {
		this("no description", reason);
	}

	public BobcatServiceException(String msg, BobcatError reason) {
		super(msg);
		this.reason = reason;
	}

	public BobcatServiceException(String msg) {
		this(msg, BobcatError.UNKNOWN);
	}

	public BobcatError getReason() {
		return reason;
	}

	public enum BobcatError {
		UNKNOWN,
		NAME_ALREADY_IN_USE;
	}
}