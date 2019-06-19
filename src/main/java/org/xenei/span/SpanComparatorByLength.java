
package org.xenei.span;

import java.io.Serializable;
import java.util.Comparator;

/**
 * <p>
 * Comparator definition for the Span class, it also contains utility methods for sorting Spans.
 * </p>
 *
 */
public class SpanComparatorByLength implements Comparator<Span>, Serializable {

    private static final long serialVersionUID = 3996146348610573570L;

    // Default Comparator based on length
    public static final Comparator<Span> SPAN_COMPARATOR_BY_LENGTH_DESC = Comparator.comparingLong( Span::getLength );

    @Override
    public int compare(final Span a1, final Span a2) {
        return SpanComparatorByLength.SPAN_COMPARATOR_BY_LENGTH_DESC.compare( a1, a2 );
    }

    /**
     * Retrieves the biggest Span of the list. On equality return the last of the equal spans
     *
     * @param spans
     *            list of spans
     * @return Span object
     */
    public static Span longest(final Span... spans) {
        Span span = spans[0];
        for (int i = 1; i < spans.length; i++)
        {
            if (span.getLength() <= spans[i].getLength())
            {
                span = spans[i];
            }
        }
        return span;
    }

    /**
     * Retrieves the shortest Span of the list. On equality return the first of the equal spans.
     *
     * @param spans
     *            list of spans
     * @return Span object
     */
    public static Span shortest(final Span... spans) {
        Span span = spans[0];
        for (int i = 1; i < spans.length; i++)
        {
            if (span.getLength() > spans[i].getLength())
            {
                span = spans[i];
            }
        }
        return span;
    }

 

}
