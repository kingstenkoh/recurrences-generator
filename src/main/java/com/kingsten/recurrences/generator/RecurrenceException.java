package com.kingsten.recurrences.generator;

import lombok.Getter;

/**
 * This Exception class is used by DCP Services for handling capsule status
 *
 * Note: The default exception getMessage() should be used for error code
 * An additional field for description is added for custom messages. use getDescription() to retrieve
 */
public class RecurrenceException extends Exception{
    @Getter
    private final String dcpStatusCode;
    @Getter
    private final String dcpStatusDescription;

    public static final String GENERIC_EXCEPTION_CODE = "80000";

    public RecurrenceException(String dcpStatusCode){
        this.dcpStatusCode = dcpStatusCode;
        this.dcpStatusDescription = null;
    }

    public RecurrenceException(String dcpStatusCode, String dcpStatusDescription){
        this.dcpStatusCode = dcpStatusCode;
        this.dcpStatusDescription = dcpStatusDescription;
    }

    @Override
    public String getMessage(){
        return dcpStatusCode;
    }
}
