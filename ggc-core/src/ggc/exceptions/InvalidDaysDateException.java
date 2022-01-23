package ggc.exceptions;

public class InvalidDaysDateException extends Exception{
  
	/** Serial number for serialization. */
	private static final long serialVersionUID = 202110221538L;

    private int _date;

	public InvalidDaysDateException(int date) {
		_date=date;
	}

    public int getDate() {return _date;} 
	
}