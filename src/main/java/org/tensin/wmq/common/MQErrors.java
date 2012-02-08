package org.tensin.wmq.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.BreakIterator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;


/**
 * MQErrors.
 *
 * @author Serge SIMON
 * @version $Revision: 1.1 $
 * @since 1 févr. 2012 12:41:39
 * 
 */
public class MQErrors {

	/** result */
	private static Map<Integer, MQError> mqErrors = new TreeMap<Integer, MQError>();
	
	/**
	 * Gets the errors count.
	 *
	 * @return the errors count
	 */
	public int getErrorsCount() {
		return mqErrors.size();
	}
	
	/**
	 * Method.
	 *
	 * @param classpathRessource the classpath ressource
	 * @return the mQ errors
	 * @throws PyramideException the pyramide exception
	 */
	public static MQErrors buildFromClasspathRessource(final String classpathRessource) throws Exception {
		MQErrors mqErrors = new MQErrors();

		PathMatchingResourcePatternResolver pmrspr = new PathMatchingResourcePatternResolver();
		String classpath = "classpath:" + classpathRessource;
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			Resource[] resources = pmrspr.getResources(classpath);
			for(Resource resource : resources) {
				is = resource.getInputStream();
				isr = new InputStreamReader(is);
				br = new BufferedReader(isr);
	            String line = null;
				while ((line = br.readLine()) != null) {
					mqErrors.add(MQError.buildFromString(line));
				}
			}
		} catch (IOException e) {
			throw new Exception(e);
		} catch (Exception e) {
            throw new Exception(e);
        } finally {
        	br.close();
        	isr.close();
        	is.close();
		}
		
		return mqErrors;
	}
	
	/**
	 * Method.
	 *
	 * @param mqError the mq error
	 */
	private void add(MQError mqError) {
		if ( mqError != null ) {
			mqErrors.put(new Integer(mqError.getCode()), mqError);
		}
	}

	/**
	 * Method.
	 *
	 * @param code the code
	 * @return the mQ error
	 */
	public MQError getMQError(final int code) {
		return mqErrors.get(new Integer(code));
	}
	
	/**
	 * Method.
	 *
	 * @param code
	 * @return
	 */
	public MQError getMQError(final String code) {
		return mqErrors.get(new Integer(code));
	}

	/**
	 * Method.
	 *
	 * @param code the code
	 * @return the mQ error reason
	 */
	public String getMQErrorReason(final int code) {
		MQError mqError = mqErrors.get(code);
		if ( mqError != null ){
			String description = mqError.getDescription();
			return "[" + mqError.getReason() + "]" + (StringUtils.isNotEmpty(description) ? "\n" + wordWrap(description, 140, Locale.ENGLISH) : "");
		} else {
			return "Code [" + code + "] undefined in the MQ errors code list.";
		}
	}
	

    /**
     * Reformats a string where lines that are longer than <tt>width</tt>
     * are split apart at the earliest wordbreak or at maxLength, whichever is
     * sooner. If the width specified is less than 5 or greater than the input
     * Strings length the string will be returned as is.
     * <p/>
     * Please note that this method can be lossy - trailing spaces on wrapped
     * lines may be trimmed.
     *
     * @param input the String to reformat.
     * @param width the maximum length of any one line.
     * @return a new String with reformatted as needed.
     */
    public static String wordWrap(String input, int width, Locale locale) {
        // protect ourselves
        if (input == null) {
            return "";
        }
        else if (width < 5) {
            return input;
        }
        else if (width >= input.length()) {
            return input;
        }

  

        StringBuilder buf = new StringBuilder(input);
        boolean endOfLine = false;
        int lineStart = 0;

        for (int i = 0; i < buf.length(); i++) {
            if (buf.charAt(i) == '\n') {
                lineStart = i + 1;
                endOfLine = true;
            }

            // handle splitting at width character
            if (i > lineStart + width - 1) {
                if (!endOfLine) {
                    int limit = i - lineStart - 1;
                    BreakIterator breaks = BreakIterator.getLineInstance(locale);
                    breaks.setText(buf.substring(lineStart, i));
                    int end = breaks.last();

                    // if the last character in the search string isn't a space,
                    // we can't split on it (looks bad). Search for a previous
                    // break character
                    if (end == limit + 1) {
                        if (!Character.isWhitespace(buf.charAt(lineStart + end))) {
                            end = breaks.preceding(end - 1);
                        }
                    }

                    // if the last character is a space, replace it with a \n
                    if (end != BreakIterator.DONE && end == limit + 1) {
                        buf.replace(lineStart + end, lineStart + end + 1, "\n");
                        lineStart = lineStart + end;
                    }
                    // otherwise, just insert a \n
                    else if (end != BreakIterator.DONE && end != 0) {
                        buf.insert(lineStart + end, '\n');
                        lineStart = lineStart + end + 1;
                    }
                    else {
                        buf.insert(i, '\n');
                        lineStart = i + 1;
                    }
                }
                else {
                    buf.insert(i, '\n');
                    lineStart = i + 1;
                    endOfLine = false;
                }
            }
        }

        return buf.toString();
    }
    
	/**
	 * Method.
	 *
	 * @param code the code
	 * @return the mQ error reason
	 */
	public String getMQErrorReason(final String code) {
		if (StringUtils.isEmpty(code) ) {
			return "Empty error code";
		}
		return getMQErrorReason(new Integer(code).intValue());
	}
	
	/**
	  * {@inheritDoc}
	  * 
	  * @see java.lang.Object#toString()
	  */
	@Override
    public String toString() {
		StringBuilder sb = new StringBuilder();
		for (MQError mqError : mqErrors.values()) {
			sb.append(" ").append(mqError.toString()).append("\n");
		}
		return sb.toString();
	}
}