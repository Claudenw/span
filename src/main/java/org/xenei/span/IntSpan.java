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

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 * Describes a span of data. Starting offset and ending index, or the starting
 * offset and the length.
 */
public interface IntSpan extends Span {

    /**
     * The Comparator to compare IntSpans by length.
     */
    public static final ComparatorByLength COMPARATOR_BY_LENGTH = new ComparatorByLength();
    /**
     * The Comparator to compare IntSpans by offset.
     */
    public static final ComparatorByOffset COMPARATOR_BY_OFFSET = new ComparatorByOffset();

    /**
     * An empty IntSpan.
     */
    public static final IntSpan EMPTY = new Impl(0, 0);

    /**
     * Constructs an IntSpan from a starting position and an endpoint.
     *
     * @param offset The offset position.
     * @param end    The endpoint
     * @return the new IntSpan.
     */
    static IntSpan fromEnd(final int offset, final int end) {
        return new Impl(offset, (end - offset) + 1);
    }

    /**
     * Constructs an IntSpan from a starting position and a length.
     *
     * @param offset The offset position.
     * @param length The length of the span.
     * @return the new IntSpan.
     */
    static IntSpan fromLength(final int offset, final int length) {
        return new Impl(offset, length);
    }

    /**
     * A method to calculate the end of a span from the start and length. Intended
     * to be used by span implementations that store start and length.
     *
     * @param span The span to calculate end for
     * @return The end position of the span
     */
    public static int calcEnd(final IntSpan span) {
        return (span.getOffset() + span.getLength()) - 1;
    }

    /**
     * A method to calculate the length of a span from the start and end. Intended
     * to be used by span implementations that stoare start and end.
     *
     * @param span The span to calculate end for
     * @return The end position of the span
     */
    public static int calcLength(final IntSpan span) {
        return (span.getEnd() - span.getOffset()) + 1;
    }

    /**
     * create the default string representation for the span.
     *
     * @param span The span to get the string for
     * @return The printable string
     */
    public static String toString(final IntSpan span) {
        return String.format("%s[%s,%s]", span.getClass().getName(), span.getOffset(),
                span.getLength() > 0 ? span.getEnd() : "-empty-");

    }

    /**
     * Orders the list in an order defined by the function parameter.
     * <p>
     * Other than ordering the spans no guarantee for order of equivalent nodes is
     * provided.
     * </p>
     *
     * @param reverse      should we reverse the order
     * @param function     how to sort the list of spans
     * @param spanElements elements to sort
     * @return list of spans sorted according to the function and reverse flag
     */
    public static List<IntSpan> sortList(final boolean reverse, final Function<IntSpan, Integer> function,
            final IntSpan... spanElements) {

        final Comparator<IntSpan> spanComparator = Comparator.comparing(function);

        final List<IntSpan> spanList = Arrays.asList(spanElements);

        if (reverse) {
            spanList.sort(spanComparator);
        } else {
            spanList.sort(spanComparator.reversed());
        }

        return spanList;
    }

    /**
     * Gets the starting position.
     *
     * @return offset position
     */
    int getOffset();

    /**
     * Gets the length of the span.
     *
     * @return the length of the span.
     */
    int getLength();

    /**
     * Gets the ending position of span.
     *
     * @return end position
     */
    int getEnd();

    /**
     * Return true if the spans share any positions.
     *
     * @param other The other span
     * @return true if overlap
     */
    public default boolean overlaps(final IntSpan other) {
        if ((getEnd() < other.getOffset()) || (getOffset() > other.getEnd())) {
            return false;
        }
        return true;
    }

    /**
     * Return true if this span contains the position.
     *
     * @param pos the position to check for.
     * @return true if start &lt;= pos &lt;= end
     */
    public default boolean contains(final int pos) {
        return (getOffset() <= pos) && (getEnd() >= pos);
    }

    /**
     * Return true if this span contains teh enther other span.
     *
     * @param oher the other span.
     * @return true if this span contains both the offset and the end of the other span.
     */
    public default boolean contains(final IntSpan other) {
        return contains(other.getOffset()) && contains(other.getEnd());
    }

    /**
     * An implementation of and IntSpan.
     *
     */
    public class Impl implements IntSpan {

        /**
         * The offset for the span
         */
        private final int offset;
        /**
         * The length of the span
         */
        private final int length;

        /**
         * Constructor using a starting position and a length. To construct using a
         * starting position and an endpoint use fromEnd().
         *
         * @param offset The offset position.
         * @param length The length.
         */
        /* package private */ Impl(final int offset, final int length) {
            NumberUtils.checkLongAddLimit(offset, length);
            if (length < 0) {
                throw new IndexOutOfBoundsException("Length may not be less than zero: " + length);
            }
            this.offset = offset;
            this.length = length;
        }

        @Override
        public final int getOffset() {
            return offset;
        }

        @Override
        public final int getLength() {
            return length;
        }

        @Override
        public final int getEnd() {
            return IntSpan.calcEnd(this);
        }

        @Override
        public String toString() {
            return IntSpan.toString(this);
        }

    }

    /**
     * Comparator to compare Spans by length.
     */
    public static class ComparatorByLength implements Comparator<IntSpan> {

        // Default Comparator based on length
        public static final Comparator<IntSpan> SPAN_COMPARATOR_BY_LENGTH_DESC = Comparator
                .comparingInt(IntSpan::getLength);

        @Override
        public int compare(final IntSpan a1, final IntSpan a2) {
            return ComparatorByLength.SPAN_COMPARATOR_BY_LENGTH_DESC.compare(a1, a2);
        }

        /**
         * Retrieves the longest Span of the list. On equality return the last of the
         * equal spans
         *
         * @param spans list of spans
         * @return Span object
         */
        public static IntSpan longest(final IntSpan... spans) {
            IntSpan span = spans[0];
            for (int i = 1; i < spans.length; i++) {
                if (span.getLength() <= spans[i].getLength()) {
                    span = spans[i];
                }
            }
            return span;
        }

        /**
         * Retrieves the shortest Span of the list. On equality return the first of the
         * equal spans.
         *
         * @param spans list of spans
         * @return Span object
         */
        public static IntSpan shortest(final IntSpan... spans) {
            IntSpan span = spans[0];
            for (int i = 1; i < spans.length; i++) {
                if (span.getLength() > spans[i].getLength()) {
                    span = spans[i];
                }
            }
            return span;
        }

    }

    /**
     * Comparator to compare Spans by offset.
     */
    public class ComparatorByOffset implements Comparator<IntSpan>, Serializable {

        private static final long serialVersionUID = 3996146348610573570L;

        // Default Comparator based on length
        public static final Comparator<IntSpan> SPAN_COMPARATOR_BY_OFFSET_DESC = Comparator
                .comparingInt(IntSpan::getOffset);

        @Override
        public int compare(final IntSpan a1, final IntSpan a2) {
            return ComparatorByOffset.SPAN_COMPARATOR_BY_OFFSET_DESC.compare(a1, a2);
        }

        /**
         * Retrieves the span with the highest offset. On equality return the last of the
         * equal spans
         *
         * @param spans list of spans
         * @return Span object
         */
        public static IntSpan last(final IntSpan... spans) {
            IntSpan span = spans[0];
            for (int i = 1; i < spans.length; i++) {
                if (span.getOffset() <= spans[i].getOffset()) {
                    span = spans[i];
                }
            }
            return span;
        }

        /**
         * Retrieves the Span with the lowest offset. On equality return the first of the
         * equal spans.
         *
         * @param spans list of spans
         * @return Span object
         */
        public static IntSpan first(final IntSpan... spans) {
            IntSpan span = spans[0];
            for (int i = 1; i < spans.length; i++) {
                if (span.getOffset() > spans[i].getOffset()) {
                    span = spans[i];
                }
            }
            return span;
        }

    }

}
