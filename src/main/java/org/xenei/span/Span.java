

package org.xenei.span;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 * Describes a span of data. Starting point and ending point.
 */
public interface Span {

	/**
	 * Starting position.
	 *
	 * @return offset position
	 */
	long getOffset();

	/**
	 * Length of the span.
	 *
	 * @return the length of the span.
	 */
	long getLength();

	/**
	 * Ending position of span.
	 *
	 * @return end position
	 */
	long getEnd();

	/**
	 * Return true if the spans share any positions.
	 *
	 * @param other The other span
	 * @return true if overlap
	 */
	public default boolean overlaps(final Span other) {
		if ((getEnd() < other.getOffset()) || (getOffset() > other.getEnd())) {
			return false;
		}
		return true;
	}

	/**
	 * Return true if this span contains the position.
	 *
	 * @param pos the position to check for.
	 * @return true if start &lt;= pos &lt;= end
	 */
	public default boolean contains(final long pos) {
		return (getOffset() <= pos) && (getEnd() >= pos);
	}

	/**
	 * Construct a span from a starting position and an endpoint.
	 *
	 * @param offset
	 *            The offset position.
	 * @param end
	 *            The endpoint
	 * @return the new Span.
	 */
	static Span fromEnd(final long offset, final long end) {
	    return new SpanImpl( offset, (end - offset) + 1 );
	}

	/**
	 * Construct a span from a starting position and a length.
	 *
	 * @param offset
	 *            The offset position.
	 * @param length
	 *            The length of the span.
	 * @return the new Span.
	 */
	static Span fromLength(final long offset, final long length) {
	    return new SpanImpl( offset, length );
	}
	
	/**
	 * A method to calculate the end of a span from the start and length. Intended
	 * to be used by span implementations that store start and length.
	 *
	 * @param span The span to calculate end for
	 * @return The end position of the span
	 */
	public static long calcEnd(final Span span) {
		return (span.getOffset() + span.getLength()) - 1;
	}

	/**
	 * A method to calculate the length of a span from the start and end. Intended
	 * to be used by span implementations that stoare start and end.
	 *
	 * @param span The span to calculate end for
	 * @return The end position of the span
	 */
	public static long calcLength(final Span span) {
		return (span.getEnd() - span.getOffset()) + 1;
	}

	/**
	 * create the default string for the span.
	 *
	 * @param span The span to get the string for
	 * @return The printable string
	 */
	public static String toString(final Span span) {
		return String.format("%s[%s,%s]", span.getClass().getName(), span.getOffset(),
				span.getLength() > 0 ? span.getEnd() : "-empty-");

	}
	
	/**
     * Orders the list in an order defined by the function parameter.
     * <p>
     * Other than ordering the spans no guarantee for order of equivalent nodes is provided.
     * </p>
     * 
     * @param reverse
     *            should we reverse the order
     * @param function
     *            how to sort the list of spans
     * @param spanElements
     *            elements to sort
     * @return list of spans sorted according to the function and reverse flag
     */
    public static List<Span> sortList(final boolean reverse, final Function<Span, Long> function,
            final Span... spanElements) {

        final Comparator<Span> spanComparator = Comparator.comparing( function );

        final List<Span> spanList = Arrays.asList( spanElements );

        if (reverse)
        {
            spanList.sort( spanComparator );
        } else
        {
            spanList.sort( spanComparator.reversed() );
        }

        return spanList;
    }
    
	public class SpanImpl implements Span {

	    private final long offset;
	    private final long length;

	    /**
	     * Constructor using a starting position and a length. To construct using a starting position and an endpoint use
	     * fromEnd().
	     *
	     * @param offset
	     *            The offset position.
	     * @param length
	     *            The length.
	     */
	    /* package private */ SpanImpl(final long offset, final long length) {
	        NumberUtils.checkLongAddLimit( offset, length );
	        if (length < 0)
	        {
	            throw new IndexOutOfBoundsException( "Length may not be less than zero: " + length );
	        }
	        this.offset = offset;
	        this.length = length;
	    }

	    @Override
	    public final long getOffset() {
	        return offset;
	    }

	    @Override
	    public final long getLength() {
	        return length;
	    }

	    @Override
	    public final long getEnd() {
	        return Span.calcEnd( this );
	    }

	    @Override
	    public String toString() {
	        return Span.toString( this );
	    }
	}

}
