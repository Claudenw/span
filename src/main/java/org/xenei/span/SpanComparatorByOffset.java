
package org.xenei.span;

import java.io.Serializable;
import java.util.Comparator;

/**
 * <p>
 * Comparator definition for the Span class, it also contains utility methods for sorting Spans.
 * </p>
 *
 */
public class SpanComparatorByOffset implements Comparator<Span>, Serializable {

    private static final long serialVersionUID = 3996146348610573570L;

    // Default Comparator based on length
    public static final Comparator<Span> SPAN_COMPARATOR_BY_OFFSET_DESC = Comparator.comparingLong( Span::getOffset );

    @Override
    public int compare(final Span a1, final Span a2) {
        return SpanComparatorByOffset.SPAN_COMPARATOR_BY_OFFSET_DESC.compare( a1, a2 );
    }

    /**
     * Retrieves the biggest Span of the list. On equality return the last of the equal spans
     *
     * @param spans
     *            list of spans
     * @return Span object
     */
    public static Span last(final Span... spans) {
        Span span = spans[0];
        for (int i = 1; i < spans.length; i++)
        {
            if (span.getOffset() <= spans[i].getOffset())
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
    public static Span first(final Span... spans) {
        Span span = spans[0];
        for (int i = 1; i < spans.length; i++)
        {
            if (span.getOffset() > spans[i].getOffset())
            {
                span = spans[i];
            }
        }
        return span;
    }

    

}
