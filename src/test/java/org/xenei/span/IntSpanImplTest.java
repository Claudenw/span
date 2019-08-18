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

import org.junit.Assert;
import org.junit.Test;

public class IntSpanImplTest {

	private IntSpan span;

	@Test
	public void testLengthIO0() {
		span = IntSpan.fromLength(0, 9);
		Assert.assertEquals(9, span.getLength());
		Assert.assertEquals(0, span.getOffset());
		Assert.assertEquals(8, span.getEnd());
	}

	@Test
	public void testLengthIO1() {
		span = IntSpan.fromLength(1, 9);
		Assert.assertEquals(9, span.getLength());
		Assert.assertEquals(1, span.getOffset());
		Assert.assertEquals(9, span.getEnd());
	}

	@Test
	public void testFromEndIO0() {
		span = IntSpan.fromEnd(0, 8);
		Assert.assertEquals(9, span.getLength());
		Assert.assertEquals(0, span.getOffset());
		Assert.assertEquals(8, span.getEnd());
	}

	@Test
	public void testFromEndIO1() {
		span = IntSpan.fromEnd(1, 9);
		Assert.assertEquals(9, span.getLength());
		Assert.assertEquals(1, span.getOffset());
		Assert.assertEquals(9, span.getEnd());
	}

	@Test
	public void testSpanOverlaps() {
		span = IntSpan.fromLength(1, 9);
		Assert.assertTrue(span.overlaps(IntSpan.fromLength(2, 3)));
		Assert.assertTrue(span.overlaps(IntSpan.fromLength(0, 3)));
		Assert.assertTrue(span.overlaps(IntSpan.fromLength(8, 3)));

		Assert.assertFalse(span.overlaps(IntSpan.fromLength(10, 2)));
		Assert.assertFalse(span.overlaps(IntSpan.fromLength(-2, 1)));
	}

	@Test
	public void testContains() {
		span = IntSpan.fromLength(1, 9);
		Assert.assertTrue(span.contains(2));
		Assert.assertTrue(span.contains(1));
		Assert.assertTrue(span.contains(9));

		Assert.assertFalse(span.contains(0));
		Assert.assertFalse(span.contains(10));
	}
}
