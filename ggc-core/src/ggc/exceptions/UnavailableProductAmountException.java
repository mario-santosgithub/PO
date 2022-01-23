package ggc.exceptions;

public class UnavailableProductAmountException extends Exception {

     /** Serial number for serialization. */
	private static final long serialVersionUID = 202111071025L;

    private String _id;
    private int _requested;
    private int _available;

	public UnavailableProductAmountException(String id, int requested, int available) {
        _id = id;
        _requested=requested;
        _available=available;		
	}

    public String getId() { return _id; }
    public int getRequested() {return _requested;}
    public int getAvailable() {return _available;}
}