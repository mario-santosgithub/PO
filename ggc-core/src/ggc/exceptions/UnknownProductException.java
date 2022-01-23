package ggc.exceptions;

public class UnknownProductException extends Exception {

     /** Serial number for serialization. */
	private static final long serialVersionUID = 202110241728L;

    private String _id; 

	public UnknownProductException(String id) {
        _id = id;		
	}

    public String getId() { return _id; }
}