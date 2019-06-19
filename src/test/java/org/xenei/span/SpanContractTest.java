package org.xenei.span;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.xenei.junit.contract.Contract;
import org.xenei.junit.contract.ContractTest;
import org.xenei.junit.contract.IProducer;
import org.xenei.span.Span;

@Contract(Span.class)
public class SpanContractTest<T extends Span> {

    private IProducer<T> producer;
    private Span span;

    public SpanContractTest() {
    }

    @Before
    public void setup() {
        span = producer.newInstance();
        if (3 > span.getLength())
        {
            throw new IllegalArgumentException( "Testing span must be at least 3 bytes long" );
        }
    }

    @After
    public void cleanUp() {
        producer.cleanUp();
    }

    @Contract.Inject
    public final void setProducer(final IProducer<T> producer) {
        this.producer = producer;
    }

    protected final IProducer<T> getProducer() {
        return producer;
    }

    @ContractTest
    public void testStartLengthEndAgreement() {
        Assert.assertEquals( "End value is incorrect", Span.calcEnd( span ), span.getEnd() );
        Assert.assertEquals( "Length value is incorrect", Span.calcLength( span ), span.getLength() );
    }

    @ContractTest
    public void testContainsMiddle() {
        final long mid = span.getOffset() + (span.getLength() / 2);
        Assert.assertTrue( "mid point should be in span", span.contains( mid ) );
    }

    @ContractTest
    public void testContainsStart() {
        Assert.assertTrue( "start should be in span", span.contains( span.getOffset() ) );
    }

    @ContractTest
    public void testContainsEnd() {
        Assert.assertTrue( "end should be in span", span.contains( span.getEnd() ) );
    }

    @ContractTest
    public void testNotContainsBeforeStart() {
        Assert.assertFalse( "start-1 should not be in span", span.contains( span.getOffset() - 1 ) );
    }

    @ContractTest
    public void testNotContainsAfterEnd() {
        Assert.assertFalse( "end+1 should not be in span", span.contains( span.getEnd() + 1 ) );
    }

    @ContractTest
    public void testSubspanOverlaps() {
        final Span span2 = Span.fromEnd( span.getOffset() + 1, span.getEnd() - 1 );
        Assert.assertTrue( "Subspan should overlap", span.overlaps( span2 ) );
    }

    @ContractTest
    public void testSuperspanOverlaps() {
        final Span span2 = Span.fromEnd( span.getOffset() - 1, span.getEnd() + 1 );
        Assert.assertTrue( "Superspan should overlap", span.overlaps( span2 ) );
    }

    @ContractTest
    public void testOverlapLeadingEdge() {
        final long mid = span.getOffset() + (span.getLength() / 2);
        final Span span2 = Span.fromLength( span.getOffset() - 1, mid );
        Assert.assertTrue( "Crossing leading edge should overlap", span.overlaps( span2 ) );
    }

    @ContractTest
    public void testOverlapTrailingEdge() {
        final long mid = span.getOffset() + (span.getLength() / 2);
        final Span span2 = Span.fromEnd( mid, span.getEnd() + 1 );
        Assert.assertTrue( "Crossing trailing edge should overlap", span.overlaps( span2 ) );
    }

    @ContractTest
    public void testBeforeDoesNotOverlap() {
        final Span span2 = Span.fromLength( span.getOffset() - 2, 1 );
        Assert.assertFalse( "Span before should not overlap", span.overlaps( span2 ) );
    }

    @ContractTest
    public void testAfterDoesNotOverlap() {
        final Span span2 = Span.fromEnd( span.getEnd() + 1, span.getEnd() - 2 );
        Assert.assertFalse( "Span after should not overlap", span.overlaps( span2 ) );
    }

    
   
}
