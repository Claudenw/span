package org.xenei.span;

import org.junit.Assert;
import org.junit.Test;
import org.xenei.span.Span;

public class SpanImplTest {

    private Span span;

    @Test
    public void testLengthIO0() {
        span = Span.fromLength( 0, 9 );
        Assert.assertEquals( 9, span.getLength() );
        Assert.assertEquals( 0, span.getOffset() );
        Assert.assertEquals( 8, span.getEnd() );
    }

    @Test
    public void testLengthIO1() {
        span = Span.fromLength( 1, 9 );
        Assert.assertEquals( 9, span.getLength() );
        Assert.assertEquals( 1, span.getOffset() );
        Assert.assertEquals( 9, span.getEnd() );
    }

    @Test
    public void testFromEndIO0() {
        span = Span.fromEnd( 0, 8 );
        Assert.assertEquals( 9, span.getLength() );
        Assert.assertEquals( 0, span.getOffset() );
        Assert.assertEquals( 8, span.getEnd() );
    }

    @Test
    public void testFromEndIO1() {
        span = Span.fromEnd( 1, 9 );
        Assert.assertEquals( 9, span.getLength() );
        Assert.assertEquals( 1, span.getOffset() );
        Assert.assertEquals( 9, span.getEnd() );
    }

    @Test
    public void testSpanOverlaps() {
        span = Span.fromLength( 1, 9 );
        Assert.assertTrue( span.overlaps( Span.fromLength( 2, 3 ) ) );
        Assert.assertTrue( span.overlaps( Span.fromLength( 0, 3 ) ) );
        Assert.assertTrue( span.overlaps( Span.fromLength( 8, 3 ) ) );

        Assert.assertFalse( span.overlaps( Span.fromLength( 10, 2 ) ) );
        Assert.assertFalse( span.overlaps( Span.fromLength( -2, 1 ) ) );
    }

    @Test
    public void testContains() {
        span = Span.fromLength( 1, 9 );
        Assert.assertTrue( span.contains( 2 ) );
        Assert.assertTrue( span.contains( 1 ) );
        Assert.assertTrue( span.contains( 9 ) );

        Assert.assertFalse( span.contains( 0 ) );
        Assert.assertFalse( span.contains( 10 ) );
    }
}
