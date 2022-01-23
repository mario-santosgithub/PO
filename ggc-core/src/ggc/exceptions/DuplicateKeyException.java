package ggc.exceptions;

/* This exception represents a duplicate Partner problem */
public class DuplicateKeyException extends Exception {

    /** Serial number for serialization. */
	private static final long serialVersionUID = 202110221538L;

    private String _key; 

	public DuplicateKeyException(String key) {
        _key = key;		
	}

    public String getKey() { return _key; }
}