package org.xenei.span;

import java.nio.ByteBuffer;

public interface Span {

	public static class Util {
		public static LongSpan asLongSpan(Span span) {
			if (span instanceof LongSpan) {
				return (LongSpan) span;
			} else if (span instanceof IntSpan) {
				IntSpan sp = (IntSpan) span;
				return LongSpan.fromLength(sp.getOffset(), sp.getLength());
			}
			throw new IllegalStateException("Unknown span type: " + span.getClass().getName());
		}

		public static IntSpan asIntSpan(Span span) {
			if (span instanceof LongSpan) {
				LongSpan sp = (LongSpan) span;
				return IntSpan.fromLength(NumberUtils.checkIntLimit("offset", sp.getOffset()),
						NumberUtils.checkIntLimit("length", sp.getLength()));
			} else if (span instanceof IntSpan) {
				return (IntSpan) span;
			}
			throw new IllegalStateException("Unknown span type: " + span.getClass().getName());
		}

		public static ByteBuffer asByteBuffer(Span span) {
			if (span instanceof LongSpan) {
				LongSpan sp = (LongSpan) span;
				ByteBuffer bb = ByteBuffer.wrap(new byte[LongSpan.BYTES]);
				bb.asLongBuffer().put(sp.getOffset()).put(sp.getLength());
				return bb;
			} else if (span instanceof IntSpan) {
				IntSpan sp = (IntSpan) span;
				ByteBuffer bb = ByteBuffer.wrap(new byte[IntSpan.BYTES]);
				bb.asIntBuffer().put(sp.getOffset()).put(sp.getLength());
				return bb;
			}
			throw new IllegalStateException("Unknown span type: " + span.getClass().getName());
		}
	}
}
