package ggc.exceptions;

public class UnknownTransactionException extends Exception {

     /** Serial number for serialization. */
	private static final long serialVersionUID = 20211061728L;

    private int _key; 

	public UnknownTransactionException(int key) {
        _key = key;		
	}

    public int getKey() { return _key; }
}