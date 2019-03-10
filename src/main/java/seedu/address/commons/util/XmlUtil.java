package seedu.address.commons.util;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * Converts between XML Document and String.
 * Code modified from https://www.journaldev.com/1237/java-convert-string-to-xml-document-and-xml-document-to-string
 */
public class XmlUtil {

    /**
     * Converts a XML Document to string
     * @param doc XML Document
     * @return string from doc
     * @throws TransformerException
     */
    public static String convertDocumentToString(Document doc) throws TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        String output = writer.getBuffer().toString();
        return output;
    }

    /**
     * Converts a string to XML Document
     * @param xmlStr string representation of a XML Document
     * @return XML Document from xmlStr
     * @throws Exception
     */
    public static Document convertStringToDocument(String xmlStr) throws Exception {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));
        return doc;
    }

}
