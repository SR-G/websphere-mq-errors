package org.tensin.wmq.common;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;

/**
 * MQErrors.
 * 
 * @author Serge SIMON
 * @version $Revision: 1.1 $
 * @since 1 févr. 2012 12:41:39
 * 
 */
public class MQError {
	
	/**
	 * Builds the from string.
	 *
	 * @param s the s
	 * @return the mQ error
	 */
	public static MQError buildFromString(final String s) {
		MQError result = null;
		// Ex. # 2001 (07D1) (RC2001): MQRC_ALIAS_BASE_Q_TYPE_ERROR
		// MQRC_USER_ID_NOT_AVAILABLE;2291;08F3;RC2291;This reason should be returned by the MQZ_FIND_USERID installable service component when the user ID cannot be determined. On z/OS®, this reason code does not occur.;MQCC_FAILED;None. See the WebSphere MQ System Administration Guide book for information about installable services.;
		if ( StringUtils.isNotEmpty(s)) {
			try {
				result = new MQError();
				StringTokenizer st = new StringTokenizer(s, ";");
				result.setReason(st.nextToken());
				result.setCode(new Integer(st.nextToken()).intValue());
				if ( st.hasMoreTokens())
					result.setHexa(st.nextToken());
				if ( st.hasMoreTokens())
					result.setRc(st.nextToken());
				if ( st.hasMoreTokens())
					result.setDescription(st.nextToken());
				if ( st.hasMoreTokens())
					result.setCompletionCode(st.nextToken());
				if ( st.hasMoreTokens())
					result.setProgrammerResponse(st.nextToken());
			} catch (NoSuchElementException e) {
				System.out.println("Error line not matching pattern : " + s);
			}
		}
		return result;
	}
	
	/** The code. */
	private int code;
	
	/** The reason. */
	private String reason;
	
	/** The url. */
	private String url;
	
	/** The description. */
	private String description;
	
	/** The completion code. */
	private String completionCode;
	
	/** The programmer response. */
	private String programmerResponse;
	
	/** The hexa. */
	private String hexa;
	
	/** The rc. */
	private String rc;
	
	/**
	 * Gets the completion code.
	 *
	 * @return the completion code
	 */
	public String getCompletionCode() {
		return completionCode;
	}

	/**
	 * Sets the completion code.
	 *
	 * @param completionCode the new completion code
	 */
	public void setCompletionCode(String completionCode) {
		this.completionCode = completionCode;
	}

	/**
	 * Gets the programmer response.
	 *
	 * @return the programmer response
	 */
	public String getProgrammerResponse() {
		return programmerResponse;
	}

	/**
	 * Sets the programmer response.
	 *
	 * @param programmerResponse the new programmer response
	 */
	public void setProgrammerResponse(String programmerResponse) {
		this.programmerResponse = programmerResponse;
	}

	/**
	 * Gets the hexa.
	 *
	 * @return the hexa
	 */
	public String getHexa() {
		return hexa;
	}

	/**
	 * Sets the hexa.
	 *
	 * @param hexa the new hexa
	 */
	public void setHexa(String hexa) {
		this.hexa = hexa;
	}

	/**
	 * Gets the rc.
	 *
	 * @return the rc
	 */
	public String getRc() {
		return rc;
	}

	/**
	 * Sets the rc.
	 *
	 * @param rc the new rc
	 */
	public void setRc(String rc) {
		this.rc = rc;
	}

	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	public int getCode() {
		return code;
	}
	
	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Gets the reason.
	 *
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}
	
	/**
	 * Gets the url.
	 *
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * Sets the code.
	 *
	 * @param code the new code
	 */
	public void setCode(int code) {
		this.code = code;
	}
	
	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Sets the reason.
	 *
	 * @param reason the new reason
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	/**
	 * Sets the url.
	 *
	 * @param url the new url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	  * {@inheritDoc}
	  * 
	  * @see java.lang.Object#toString()
	  */
	@Override
    public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("").append(code).append(" | ").append(reason);
		return sb.toString();
	}
}