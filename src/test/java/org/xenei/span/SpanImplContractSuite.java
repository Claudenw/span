package org.xenei.span;

import org.junit.runner.RunWith;
import org.xenei.junit.contract.Contract;
import org.xenei.junit.contract.ContractImpl;
import org.xenei.junit.contract.ContractSuite;
import org.xenei.junit.contract.IProducer;
import org.xenei.span.Span.SpanImpl;

@RunWith(ContractSuite.class)
@ContractImpl(SpanImpl.class)
public class SpanImplContractSuite {

    @Contract.Inject
    public IProducer<SpanImpl> getProducer() {
        return new IProducer<SpanImpl>() {

            @Override
            public SpanImpl newInstance() {
                return new SpanImpl( 0, 10 );
            }

            @Override
            public void cleanUp() {
                // do nothing
            }
        };
    }
}
