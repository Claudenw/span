package org.xenei.span;

import org.junit.Assert;
import org.junit.Test;
import org.xenei.span.Span;
import org.xenei.span.SpanComparatorByLength;

public class SpanComparatorByLengthTests {

    @Test
    public void shortestTest() {
        final Span one = Span.fromLength( 0, 5 );
        final Span two = Span.fromLength( 0, 5 );
        final Span three = Span.fromLength( 0, 5 );

        final Span s = SpanComparatorByLength.shortest( one, two, three );
        Assert.assertTrue( one == s );
    }

    @Test
    public void longestTest() {
        final Span one = Span.fromLength( 0, 5 );
        final Span two = Span.fromLength( 0, 5 );
        final Span three = Span.fromLength( 0, 5 );

        final Span s = SpanComparatorByLength.longest( one, two, three );
        Assert.assertTrue( three == s );
    }

    @Test
    public void compareEqualityTest() {
        final Span one = Span.fromLength( 0, 5 );
        final Span two = Span.fromLength( 0, 5 );

        Assert.assertEquals( 0, SpanComparatorByLength.SPAN_COMPARATOR_BY_LENGTH_DESC.compare( one, two ) );

    }

    @Test
    public void compareOneGreaterTest() {
        final Span one = Span.fromLength( 0, 10 );
        final Span two = Span.fromLength( 0, 5 );

        Assert.assertEquals( 1, SpanComparatorByLength.SPAN_COMPARATOR_BY_LENGTH_DESC.compare( one, two ) );

    }

    @Test
    public void compareOneLesserTest() {
        final Span one = Span.fromLength( 0, 5 );
        final Span two = Span.fromLength( 0, 10 );

        Assert.assertEquals( -1, SpanComparatorByLength.SPAN_COMPARATOR_BY_LENGTH_DESC.compare( one, two ) );

    }

    
}
