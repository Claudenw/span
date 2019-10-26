package org.xenei.span;

import java.nio.ByteBuffer;

/**
 * An interface tha defines a span.
 *
 */
public interface Span {

    /**
     * Converts the span to a LongSpan.
     * @return A LongSpan representation of the span.
     */
	default LongSpan asLongSpan() {
		if (this instanceof LongSpan) {
			return (LongSpan) this;
		} else if (this instanceof IntSpan) {
			IntSpan sp = (IntSpan) this;
			return LongSpan.fromLength(sp.getOffset(), sp.getLength());
		}
		throw new IllegalStateException("Unknown span type: " + this.getClass().getName());
	}

	/**
	 * Converts the span to an IntSpan.
	 * @return an IntSpan representation of the span.
	 * @throws IllegalArgumentException if any of the values exceed Integer.MAX_VALUE
	 */
	default IntSpan asIntSpan() {
		if (this instanceof LongSpan) {
			LongSpan sp = (LongSpan) this;
			return IntSpan.fromLength(NumberUtils.checkIntLimit("offset", sp.getOffset()),
					NumberUtils.checkIntLimit("length", sp.getLength()));
		} else if (this instanceof IntSpan) {
			return (IntSpan) this;
		}
		throw new IllegalStateException("Unknown span type: " + this.getClass().getName());
	}

	/**
	 * Converts the span values into a byte buffer.
	 * @return
	 */
	default ByteBuffer asByteBuffer() {
		if (this instanceof LongSpan) {
			LongSpan sp = (LongSpan) this;
			ByteBuffer bb = ByteBuffer.wrap(new byte[Long.BYTES*2]);
			bb.asLongBuffer().put(sp.getOffset()).put(sp.getLength());
			return bb;
		} else if (this instanceof IntSpan) {
			IntSpan sp = (IntSpan) this;
			ByteBuffer bb = ByteBuffer.wrap(new byte[Integer.BYTES*2]);
			bb.asIntBuffer().put(sp.getOffset()).put(sp.getLength());
			return bb;
		}
		throw new IllegalStateException("Unknown span type: " + this.getClass().getName());
	}

}
