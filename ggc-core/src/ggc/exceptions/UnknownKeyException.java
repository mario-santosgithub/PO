package ggc.exceptions;

public class UnknownKeyException extends Exception {

     /** Serial number for serialization. */
	private static final long serialVersionUID = 202110241728L;

    private String _key; 

	public UnknownKeyException(String key) {
        _key = key;		
	}

    public String getKey() { return _key; }
}