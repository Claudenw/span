/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xenei.span;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.xenei.junit.contract.Contract;
import org.xenei.junit.contract.ContractTest;
import org.xenei.junit.contract.IProducer;

@Contract(IntSpan.class)
public class IntSpanContractTest<T extends IntSpan> {

	private IProducer<T> producer;
	private IntSpan span;

	public IntSpanContractTest() {
	}

	@Before
	public void setup() {
		span = producer.newInstance();
		if (3 > span.getLength()) {
			throw new IllegalArgumentException("Testing span must be at least 3 bytes long");
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
		Assert.assertEquals("End value is incorrect", IntSpan.calcEnd(span), span.getEnd());
		Assert.assertEquals("Length value is incorrect", IntSpan.calcLength(span), span.getLength());
	}

	@ContractTest
	public void testContainsMiddle() {
		final int mid = span.getOffset() + (span.getLength() / 2);
		Assert.assertTrue("mid point should be in span", span.contains(mid));
	}

	@ContractTest
	public void testContainsStart() {
		Assert.assertTrue("start should be in span", span.contains(span.getOffset()));
	}

	@ContractTest
	public void testContainsEnd() {
		Assert.assertTrue("end should be in span", span.contains(span.getEnd()));
	}

	@ContractTest
	public void testNotContainsBeforeStart() {
		Assert.assertFalse("start-1 should not be in span", span.contains(span.getOffset() - 1));
	}

	@ContractTest
	public void testNotContainsAfterEnd() {
		Assert.assertFalse("end+1 should not be in span", span.contains(span.getEnd() + 1));
	}

	@ContractTest
	public void testSubspanOverlaps() {
		final IntSpan span2 = IntSpan.fromEnd(span.getOffset() + 1, span.getEnd() - 1);
		Assert.assertTrue("Subspan should overlap", span.overlaps(span2));
	}

	@ContractTest
	public void testSuperspanOverlaps() {
		final IntSpan span2 = IntSpan.fromEnd(span.getOffset() - 1, span.getEnd() + 1);
		Assert.assertTrue("Superspan should overlap", span.overlaps(span2));
	}

	@ContractTest
	public void testOverlapLeadingEdge() {
		final int mid = span.getOffset() + (span.getLength() / 2);
		final IntSpan span2 = IntSpan.fromLength(span.getOffset() - 1, mid);
		Assert.assertTrue("Crossing leading edge should overlap", span.overlaps(span2));
	}

	@ContractTest
	public void testOverlapTrailingEdge() {
		final int mid = span.getOffset() + (span.getLength() / 2);
		final IntSpan span2 = IntSpan.fromEnd(mid, span.getEnd() + 1);
		Assert.assertTrue("Crossing trailing edge should overlap", span.overlaps(span2));
	}

	@ContractTest
	public void testBeforeDoesNotOverlap() {
		final IntSpan span2 = IntSpan.fromLength(span.getOffset() - 2, 1);
		Assert.assertFalse("Span before should not overlap", span.overlaps(span2));
	}

	@ContractTest
	public void testAfterDoesNotOverlap() {
		final IntSpan span2 = IntSpan.fromEnd(span.getEnd() + 1, span.getEnd() + 2);
		Assert.assertFalse("Span after should not overlap", span.overlaps(span2));
	}

}
