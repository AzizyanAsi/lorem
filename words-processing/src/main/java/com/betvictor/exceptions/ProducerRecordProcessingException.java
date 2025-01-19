
package com.betvictor.exceptions;

import com.betvictor.exceptions.error.ErrorCode;

public class ProducerRecordProcessingException extends BaseException {

    public ProducerRecordProcessingException() {
        super(ErrorCode.PRODUCE_MESSAGE_TO_KAFKA_FAILED);
    }
}
