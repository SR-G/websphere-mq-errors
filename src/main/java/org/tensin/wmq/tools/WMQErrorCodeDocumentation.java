package org.tensin.wmq.tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.StringTokenizer;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.tensin.wmq.common.WMQEnhancedException;

/**
 * Extract errors codes from a whole bunch of .html files (frm*_.html). 
 * The files have to be downloaded from the IBM Websphere MQ website (one can use src/script/down.sh script, or the downloadFiles java method included in this class).
 * 
 * @author Serge SIMON
 * @version $Revision: 1.1 $
 * @since 4 févr. 2012 19:14:47
 */
public class WMQErrorCodeDocumentation {

    /** The Constant HTML_DOCUMENT_ENCODING. */
    private static final String HTML_DOCUMENT_ENCODING = "UTF-8";

    /** The Constant HTML_DOCUMENT_ERROR_CODE_IDENTIFICATION. */
    private static final String HTML_DOCUMENT_ERROR_CODE_IDENTIFICATION = "MQRC";

    /** The Constant HTML_DOCUMENT_TITLE. */
    private static final String HTML_DOCUMENT_TITLE = "title";

    /** The Constant HTML_DOCUMENT_PARAGRAPH. */
    private static final String HTML_DOCUMENT_PARAGRAPH = "p";

    /** The Constant HTML_DOCUMENT_SECTION. */
    private static final String HTML_DOCUMENT_SECTION = "section";

    /** DESTINATION_FILE */
    private static final String DESTINATION_FILE = "src/main/java/" + WMQEnhancedException.MQ_ERRORS_FILE_NAME;

    /** FOLDER_CONTAINING_HTML_FILES */
    private static final String FOLDER_CONTAINING_HTML_FILES = "tmp/wmq_error_codes/";

    /** Logger. */
    private static final Log LOGGER = LogFactory.getLog(WMQErrorCodeDocumentation.class);

    /** The Constant WEBSPHERE_DOCUMENTATION_URL_START. */
    private static final String WEBSPHERE_DOCUMENTATION_URL_START = "http://publib.boulder.ibm.com/infocenter/wmqv7/v7r0/topic/com.ibm.mq.amqzao.doc/fm";
    
    /** The Constant WEBSPHERE_DOCUMENTATION_URL_END. */
    private static final String WEBSPHERE_DOCUMENTATION_URL_END = "_.htm"; 
    
    /** The Constant WEBSPHERE_DOCUMENTATION_SEED_START. */
    private static final int WEBSPHERE_DOCUMENTATION_SEED_START = 10000;
    
    /** The Constant WEBSPHERE_DOCUMENTATION_SEED_END. */
    private static final int WEBSPHERE_DOCUMENTATION_SEED_END = 30000;
    
    /** The Constant WEBSPHERE_DOCUMENTATION_SEED_STEP. */
    private static final int WEBSPHERE_DOCUMENTATION_SEED_STEP = 10;
    
    /**
     * The main method.
     *
     * @param args the arguments
     * @throws WMQEnhancedException the rocade exception
     */
    public static void main(final String[] args) throws WMQEnhancedException {
        BasicConfigurator.configure();
        WMQErrorCodeDocumentation wmqErrorCodeDocumentation = new WMQErrorCodeDocumentation();
        wmqErrorCodeDocumentation.downloadFiles(WEBSPHERE_DOCUMENTATION_URL_START,
                WEBSPHERE_DOCUMENTATION_URL_END, 
                WEBSPHERE_DOCUMENTATION_SEED_START,
                WEBSPHERE_DOCUMENTATION_SEED_END,
                WEBSPHERE_DOCUMENTATION_SEED_STEP,
                FOLDER_CONTAINING_HTML_FILES);
        wmqErrorCodeDocumentation.process(FOLDER_CONTAINING_HTML_FILES, DESTINATION_FILE);
    }

    /**
     * Download files.
     *
     * @param websphereDocumentationUrlStart the websphere documentation url start
     * @param websphereDocumentationUrlEnd the websphere documentation url end
     * @param websphereDocumentationSeedStart the websphere documentation seed start
     * @param websphereDocumentationSeedEnd the websphere documentation seed end
     * @param websphereDocumentationSeedStep the websphere documentation seed step
     * @param folderContainingHtmlFiles the folder containing html files
     */
    private void downloadFiles(String websphereDocumentationUrlStart, String websphereDocumentationUrlEnd, int websphereDocumentationSeedStart,
            int websphereDocumentationSeedEnd, int websphereDocumentationSeedStep, String folderContainingHtmlFiles) {
        LOGGER.info("Download files to [" + folderContainingHtmlFiles + "]");
        String url;
        String destinationFileName;
        String fullDestinationFileName;
        for (int i = websphereDocumentationSeedStart ; i < websphereDocumentationSeedEnd ; i = i + websphereDocumentationSeedStep) {
            url = websphereDocumentationUrlStart + String.valueOf(i) + websphereDocumentationUrlEnd;
            destinationFileName = String.valueOf(i) + ".html";
            fullDestinationFileName = folderContainingHtmlFiles + File.separator + destinationFileName;
            if (new File(fullDestinationFileName).exists()) {
                System.out.println("[" + destinationFileName  + "] already downloaded");
            } else {
                downloadFile(url, fullDestinationFileName);
            }
        }
    }

    /**
     * Download file.
     *
     * @param url the url
     * @param fullDestinationFileName the full destination file name
     */
    private void downloadFile(final String url, final String destinationFileName) {
        try {
            File file = new File(destinationFileName);
            File parent = new File(file.getParent());

            if (!parent.exists()) {
                parent.mkdirs();
            }

            URL destURL = new URL(url);
            InputStream in = destURL.openStream();
            if ( in != null ) {
                System.out.println("Downloading [" + url + "]");
                BufferedInputStream bis = new BufferedInputStream(in);
                byte[] buffer = new byte[16384];
                int c = 0;
                FileOutputStream out = new FileOutputStream(file.getAbsolutePath());
                while ((c = bis.read(buffer)) != -1) {
                    out.write(buffer, 0, c);
                    out.flush();
                }
                out.close();
                bis.close();
                in.close();
            }
        } catch (UnknownHostException e) {
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        } finally {
        }
        
    }

    /**
     * Append blank fields.
     * 
     * @param start
     *            the start
     * @param max
     *            the max
     * @return the string
     */
    private String appendBlankFields(final int start, final int max) {
        String result = "";
        for (int j = start; j < max; j++) {
            result += ";";
        }
        return result;
    }

    /**
     * Extract descriptions from document.
     * 
     * @param doc
     *            the doc
     * @return the string
     */
    private String extractDescriptionsFromDocument(final Document doc) {
        String result = "";
        int i = 0;
        for (Element element : doc.getElementsByClass(HTML_DOCUMENT_SECTION)) {
            i++;
            for (Element paragraph : element.getElementsByClass(HTML_DOCUMENT_PARAGRAPH)) {
                result += paragraph.text().trim() + ";";
            }
        }
        result += appendBlankFields(i, 3);
        return result;
    }

    /**
     * Extract fields from title.
     * 
     * @param subTitle
     *            the sub title
     * @return the string
     */
    private String extractFieldsFromTitle(final String subTitle) {
        String result = "";
        StringTokenizer st = new StringTokenizer(subTitle, " ");
        int i = 0;
        while (st.hasMoreTokens()) {
            i++;
            String s = st.nextToken().trim().replaceAll("\\(", "").replaceAll("\\)", "");
            result += s + ";";
        }
        result += appendBlankFields(i, 3);
        return result;
    }

    /**
     * Extract title from document.
     * 
     * @param doc
     *            the doc
     * @return the string
     */
    private String extractTitleFromDocument(final Document doc) {
        for (Element element : doc.getElementsByTag(HTML_DOCUMENT_TITLE)) {
            return element.text();
        }
        return null;
    }

    /**
     * Process.
     *
     * @param folderName the folder name
     * @param destinationFile the destination file
     * @throws WMQEnhancedException the pyramide exception
     */
    private void process(final String htmlFilesFolderName, final String destinationFile) throws WMQEnhancedException {
        LOGGER.info("Analyzing .html files contained in [" + htmlFilesFolderName + "]");
        Collection<String> results = new TreeSet<String>();
        try {
            @SuppressWarnings("unchecked")
            Collection<File> files = FileUtils.listFiles(new File(htmlFilesFolderName), new String[] { "html" }, false);
            String extractedContent;
            for (final File file : files) {
                extractedContent = processHTMLFile(file);
                if (StringUtils.isNotEmpty(extractedContent)) {
                    results.add(extractedContent);
                }
            }
            LOGGER.info("Writing the [" + results.size() + "] errors codes found to [" + destinationFile + "]");
            FileUtils.writeLines(new File(destinationFile), results, "\n");
        } catch (IOException e) {
            throw new WMQEnhancedException(e);
        }
    }

    /**
     * Process html file.
     * 
     * @param file
     *            the file
     * @return the string
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private String processHTMLFile(final File file) throws IOException {
        String title = null;
        Document doc = null;
        String result = "";
        doc = Jsoup.parse(file, HTML_DOCUMENT_ENCODING);
        title = extractTitleFromDocument(doc);

        if (StringUtils.isNotEmpty(title) && title.contains(HTML_DOCUMENT_ERROR_CODE_IDENTIFICATION)) {
            StringTokenizer st = new StringTokenizer(title, ":");
            String first = st.nextToken();
            String last = st.nextToken();

            result = last.trim() + ";";
            result += extractFieldsFromTitle(first);
            result += extractDescriptionsFromDocument(doc);
        }

        return result;
    }
}
