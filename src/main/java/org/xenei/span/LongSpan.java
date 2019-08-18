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
public interface LongSpan extends Span {

	public static final ComparatorByLength COMPARATOR_BY_LENGTH = new ComparatorByLength();
	public static final ComparatorByOffset COMPARATOR_BY_OFFSET = new ComparatorByOffset();
	public static final int BYTES = Long.BYTES * 2;

	/**
	 * Construct a span from a starting position and an endpoint.
	 *
	 * @param offset The offset position.
	 * @param end    The endpoint
	 * @return the new Span.
	 */
	static LongSpan fromEnd(final long offset, final long end) {
		return new Impl(offset, (end - offset) + 1);
	}

	/**
	 * Construct a span from a starting position and a length.
	 *
	 * @param offset The offset position.
	 * @param length The length of the span.
	 * @return the new Span.
	 */
	static LongSpan fromLength(final long offset, final long length) {
		return new Impl(offset, length);
	}

	/**
	 * A method to calculate the end of a span from the start and length. Intended
	 * to be used by span implementations that store start and length.
	 *
	 * @param span The span to calculate end for
	 * @return The end position of the span
	 */
	public static long calcEnd(final LongSpan span) {
		return (span.getOffset() + span.getLength()) - 1;
	}

	/**
	 * A method to calculate the length of a span from the start and end. Intended
	 * to be used by span implementations that stoare start and end.
	 *
	 * @param span The span to calculate end for
	 * @return The end position of the span
	 */
	public static long calcLength(final LongSpan span) {
		return (span.getEnd() - span.getOffset()) + 1;
	}

	public static IntSpan asIntSpan(LongSpan longSpan) {
		return IntSpan.fromLength(NumberUtils.checkIntLimit("offset", longSpan.getOffset()),
				NumberUtils.checkIntLimit("length", longSpan.getLength()));
	}

	/**
	 * create the default string for the span.
	 *
	 * @param span The span to get the string for
	 * @return The printable string
	 */
	public static String toString(final LongSpan span) {
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
	public static List<LongSpan> sortList(final boolean reverse, final Function<LongSpan, Long> function,
			final LongSpan... spanElements) {

		final Comparator<LongSpan> spanComparator = Comparator.comparing(function);

		final List<LongSpan> spanList = Arrays.asList(spanElements);

		if (reverse) {
			spanList.sort(spanComparator);
		} else {
			spanList.sort(spanComparator.reversed());
		}

		return spanList;
	}

	/**
	 * Starting position.
	 *
	 * @return offset position
	 */
	long getOffset();

	/**
	 * Length of the span.
	 *
	 * @return the length of the span.
	 */
	long getLength();

	/**
	 * Ending position of span.
	 *
	 * @return end position
	 */
	long getEnd();

	/**
	 * Return true if the spans share any positions.
	 *
	 * @param other The other span
	 * @return true if overlap
	 */
	public default boolean overlaps(final LongSpan other) {
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
	public default boolean contains(final long pos) {
		return (getOffset() <= pos) && (getEnd() >= pos);
	}

	/**
	 * An implementation of Span.
	 *
	 */
	public class Impl implements LongSpan {

		private final long offset;
		private final long length;

		/**
		 * Constructor using a starting position and a length. To construct using a
		 * starting position and an endpoint use fromEnd().
		 *
		 * @param offset The offset position.
		 * @param length The length.
		 */
		/* package private */ Impl(final long offset, final long length) {
			NumberUtils.checkLongAddLimit(offset, length);
			if (length < 0) {
				throw new IndexOutOfBoundsException("Length may not be less than zero: " + length);
			}
			this.offset = offset;
			this.length = length;
		}

		@Override
		public final long getOffset() {
			return offset;
		}

		@Override
		public final long getLength() {
			return length;
		}

		@Override
		public final long getEnd() {
			return LongSpan.calcEnd(this);
		}

		@Override
		public String toString() {
			return LongSpan.toString(this);
		}

	}

	/**
	 * <p>
	 * Comparator to compare Spans by length.
	 * </p>
	 *
	 */
	public static class ComparatorByLength implements Comparator<LongSpan>, Serializable {

		private static final long serialVersionUID = 3996146348610573570L;

		// Default Comparator based on length
		public static final Comparator<LongSpan> SPAN_COMPARATOR_BY_LENGTH_DESC = Comparator
				.comparingLong(LongSpan::getLength);

		@Override
		public int compare(final LongSpan a1, final LongSpan a2) {
			return ComparatorByLength.SPAN_COMPARATOR_BY_LENGTH_DESC.compare(a1, a2);
		}

		/**
		 * Retrieves the biggest Span of the list. On equality return the last of the
		 * equal spans
		 *
		 * @param spans list of spans
		 * @return Span object
		 */
		public static LongSpan longest(final LongSpan... spans) {
			LongSpan span = spans[0];
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
		public static LongSpan shortest(final LongSpan... spans) {
			LongSpan span = spans[0];
			for (int i = 1; i < spans.length; i++) {
				if (span.getLength() > spans[i].getLength()) {
					span = spans[i];
				}
			}
			return span;
		}

	}

	/**
	 * <p>
	 * Comparator to compare Spans by offset.
	 * </p>
	 *
	 */
	public class ComparatorByOffset implements Comparator<LongSpan>, Serializable {

		private static final long serialVersionUID = 3996146348610573570L;

		// Default Comparator based on length
		public static final Comparator<LongSpan> SPAN_COMPARATOR_BY_OFFSET_DESC = Comparator
				.comparingLong(LongSpan::getOffset);

		@Override
		public int compare(final LongSpan a1, final LongSpan a2) {
			return ComparatorByOffset.SPAN_COMPARATOR_BY_OFFSET_DESC.compare(a1, a2);
		}

		/**
		 * Retrieves the biggest Span of the list. On equality return the last of the
		 * equal spans
		 *
		 * @param spans list of spans
		 * @return Span object
		 */
		public static LongSpan last(final LongSpan... spans) {
			LongSpan span = spans[0];
			for (int i = 1; i < spans.length; i++) {
				if (span.getOffset() <= spans[i].getOffset()) {
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
		public static LongSpan first(final LongSpan... spans) {
			LongSpan span = spans[0];
			for (int i = 1; i < spans.length; i++) {
				if (span.getOffset() > spans[i].getOffset()) {
					span = spans[i];
				}
			}
			return span;
		}

	}

}
