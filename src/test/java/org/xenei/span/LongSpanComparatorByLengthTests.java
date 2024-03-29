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
import org.xenei.span.LongSpan.ComparatorByLength;

public class LongSpanComparatorByLengthTests {

    @Test
    public void shortestTest() {
        final LongSpan one = LongSpan.fromLength(0, 5);
        final LongSpan two = LongSpan.fromLength(0, 5);
        final LongSpan three = LongSpan.fromLength(0, 5);

        final LongSpan s = ComparatorByLength.shortest(one, two, three);
        Assert.assertTrue(one == s);
    }

    @Test
    public void longestTest() {
        final LongSpan one = LongSpan.fromLength(0, 5);
        final LongSpan two = LongSpan.fromLength(0, 5);
        final LongSpan three = LongSpan.fromLength(0, 5);

        final LongSpan s = ComparatorByLength.longest(one, two, three);
        Assert.assertTrue(three == s);
    }

    @Test
    public void compareEqualityTest() {
        final LongSpan one = LongSpan.fromLength(0, 5);
        final LongSpan two = LongSpan.fromLength(0, 5);

        Assert.assertEquals(0, LongSpan.COMPARATOR_BY_LENGTH.compare(one, two));

    }

    @Test
    public void compareOneGreaterTest() {
        final LongSpan one = LongSpan.fromLength(0, 10);
        final LongSpan two = LongSpan.fromLength(0, 5);

        Assert.assertEquals(1, LongSpan.COMPARATOR_BY_LENGTH.compare(one, two));

    }

    @Test
    public void compareOneLesserTest() {
        final LongSpan one = LongSpan.fromLength(0, 5);
        final LongSpan two = LongSpan.fromLength(0, 10);

        Assert.assertEquals(-1, LongSpan.COMPARATOR_BY_LENGTH.compare(one, two));

    }

}
