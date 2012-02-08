package org.tensin.wmq.common;

import com.ibm.mq.MQException;
import com.ibm.mq.headers.MQDataException;


/**
 * The Class RocadeException.
 * 
 * @author Serge SIMON
 * @version $Revision: 1.1 $
 * @since 1 févr. 2012 08:52:42
 * 
 */
public class WMQEnhancedException extends Exception {

	/** The error code. */
	private int errorCode = 0;
	
	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** MQ_ERRORS_FILE_NAME. */
	public static final String MQ_ERRORS_FILE_NAME = "org/tensin/wmq/common/wmq_error_codes.csv";

	/** mqErrors. */
	private static MQErrors mqErrors;

	/**
	 * Method.
	 *
	 * @param message the message
	 * @param e the e
	 * @return the string
	 */
	private static String buildExplicitMessage(final String message, final MQException e) {
		String result = message;
		try {
			result += " " + getMQErrorsMessages().getMQErrorReason(e.getReason()) + "";
		} catch (Exception ex) {
			// LOGGER.error(ex);
		}
		return result;
	}

    /**
     * Method.
     *
     * @param message the message
     * @param e the e
     * @return the string
     */
    private static String buildExplicitMessage(final String message, final MQDataException e) {
        String result = message;
        try {
            result += " [" + getMQErrorsMessages().getMQErrorReason(e.getReason()) + "]";
        } catch (Exception ex) {
            // LOGGER.error(ex);
        }
        return result;
    }

	/**
	 * Method.
	 *
	 * @return the mQ errors messages
	 * @throws PyramideException the pyramide exception
	 */
	public static MQErrors getMQErrorsMessages() throws Exception {
		if ( mqErrors == null ) {
			mqErrors = MQErrors.buildFromClasspathRessource(MQ_ERRORS_FILE_NAME);
		}
		
		return mqErrors;
	}
	
	/**
	 * Instantiates a new rocade exception.
	 *
	 * @param e the e
	 */
	public WMQEnhancedException(final MQException e) {
		super(buildExplicitMessage(e.getMessage(), e), e);
		setErrorCode(e.getReason());
	}
	
    /**
     * Instantiates a new rocade exception.
     *
     * @param e the e
     */
    public WMQEnhancedException(final MQDataException e) {
        super(buildExplicitMessage(e.getMessage(), e), e);
        setErrorCode(e.getReason());
    }

	/**
	 * Instantiates a new rocade exception.
	 *
	 * @param message the message
	 */
	public WMQEnhancedException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new rocade exception.
	 *
	 * @param message the message
	 * @param e the e
	 */
	public WMQEnhancedException(final String message, final MQException e) {
		super(buildExplicitMessage(message, e), e);
	}

    /**
     * Instantiates a new rocade exception.
     *
     * @param message the message
     * @param e the e
     */
    public WMQEnhancedException(final String message, final MQDataException e) {
        super(buildExplicitMessage(message, e), e);
    }

	/**
	 * Instantiates a new rocade exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public WMQEnhancedException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Instantiates a new rocade exception.
	 *
	 * @param cause the cause
	 */
	public WMQEnhancedException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Gets the error code.
	 *
	 * @return the error code
	 */
	public int getErrorCode() {
		return errorCode;
	}
	
	/**
	 * Sets the error code.
	 *
	 * @param errorCode the new error code
	 */
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	
}