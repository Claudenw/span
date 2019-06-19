package org.xenei.span;

public class NumberUtils {

	/**
	 * Verify that the long value fits into an int.
	 *
	 * @param paramName
	 *            The name of the parameter, used for reporting an error.
	 * @param checkLong
	 *            the long to check
	 * @return an int version of the long.
	 * @throws IllegalArgumentException
	 *             if the long will not fit into the int.
	 */
	public static int checkIntLimit(final String paramName, final long checkLong) {
	    if ((checkLong <= Integer.MAX_VALUE) && (checkLong >= Integer.MIN_VALUE))
	    {
	        return (int) checkLong;
	    }
	    throw new IllegalArgumentException( String.format( "Parameter %s must fall between %s and %s", paramName,
	            Integer.MIN_VALUE, Integer.MAX_VALUE ) );
	}

	/**
	 * Check for over flow when calculating end position.
	 *
	 * @param start
	 *            The starting position
	 * @param increment
	 *            the length
	 * @return the long value created by adding start and increment.
	 * @throws IllegalArgumentException
	 *             if the result if not within [Long.MIN_VALUE, Long.MAX_VALUE]
	 */
	public static long checkLongAddLimit(final long start, final long increment) {
	    if (increment == 0)
	    {
	        return start;
	    }
	    if (increment < 0)
	    {
	        // this really subtracts
	        if ((Long.MIN_VALUE - increment) > start)
	        {
	            throw new IllegalArgumentException( String.format( "Start (%s) - length (%s) < Long.MIN_VALUE (%s)",
	                    start, increment, Long.MIN_VALUE ) );
	
	        }
	    } else
	    {
	        if ((Long.MAX_VALUE - increment) < start)
	        {
	            throw new IllegalArgumentException( String.format( "Length (%s) + Start (%s) > Long.MAX_VALUE (%s)",
	                    increment, start, Long.MAX_VALUE ) );
	        }
	    }
	    return start + increment;
	}

}
