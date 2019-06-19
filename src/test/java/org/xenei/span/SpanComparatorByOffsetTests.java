package org.xenei.span;

import org.junit.Assert;
import org.junit.Test;
import org.xenei.span.Span;

public class SpanComparatorByOffsetTests {

    @Test
    public void firstTest() {
        final Span one = Span.fromLength( 1, 5 );
        final Span two = Span.fromLength( 2, 5 );
        final Span three = Span.fromLength( 3, 5 );

        final Span s = SpanComparatorByOffset.first( one, two, three );
        Assert.assertTrue( one == s );
    }

    @Test
    public void lastTest() {
        final Span one = Span.fromLength( 1, 5 );
        final Span two = Span.fromLength( 2, 5 );
        final Span three = Span.fromLength( 3, 5 );

        final Span s = SpanComparatorByOffset.last( one, two, three );
        Assert.assertTrue( three == s );
    }

    @Test
    public void compareEqualityTest() {
        final Span one = Span.fromLength( 0, 5 );
        final Span two = Span.fromLength( 0, 5 );

        Assert.assertEquals( 0, SpanComparatorByOffset.SPAN_COMPARATOR_BY_OFFSET_DESC.compare( one, two ) );

    }

    @Test
    public void compareOneGreaterTest() {
        final Span one = Span.fromLength( 10, 10 );
        final Span two = Span.fromLength( 5, 10 );

        Assert.assertEquals( 1, SpanComparatorByOffset.SPAN_COMPARATOR_BY_OFFSET_DESC.compare( one, two ) );

    }

    @Test
    public void compareOneLesserTest() {
        final Span one = Span.fromLength( 5, 5 );
        final Span two = Span.fromLength( 10, 5 );

        Assert.assertEquals( -1, SpanComparatorByOffset.SPAN_COMPARATOR_BY_OFFSET_DESC.compare( one, two ) );

    }

    
}
