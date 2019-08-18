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
import org.xenei.span.IntSpan;
import org.xenei.span.IntSpan.ComparatorByOffset;

public class IntSpanComparatorByOffsetTests {

	@Test
	public void firstTest() {
		final IntSpan one = IntSpan.fromLength(1, 5);
		final IntSpan two = IntSpan.fromLength(2, 5);
		final IntSpan three = IntSpan.fromLength(3, 5);

		final IntSpan s = ComparatorByOffset.first(one, two, three);
		Assert.assertTrue(one == s);
	}

	@Test
	public void lastTest() {
		final IntSpan one = IntSpan.fromLength(1, 5);
		final IntSpan two = IntSpan.fromLength(2, 5);
		final IntSpan three = IntSpan.fromLength(3, 5);

		final IntSpan s = ComparatorByOffset.last(one, two, three);
		Assert.assertTrue(three == s);
	}

	@Test
	public void compareEqualityTest() {
		final IntSpan one = IntSpan.fromLength(0, 5);
		final IntSpan two = IntSpan.fromLength(0, 5);

		Assert.assertEquals(0, ComparatorByOffset.SPAN_COMPARATOR_BY_OFFSET_DESC.compare(one, two));

	}

	@Test
	public void compareOneGreaterTest() {
		final IntSpan one = IntSpan.fromLength(10, 10);
		final IntSpan two = IntSpan.fromLength(5, 10);

		Assert.assertEquals(1, ComparatorByOffset.SPAN_COMPARATOR_BY_OFFSET_DESC.compare(one, two));

	}

	@Test
	public void compareOneLesserTest() {
		final IntSpan one = IntSpan.fromLength(5, 5);
		final IntSpan two = IntSpan.fromLength(10, 5);

		Assert.assertEquals(-1, ComparatorByOffset.SPAN_COMPARATOR_BY_OFFSET_DESC.compare(one, two));

	}

}
