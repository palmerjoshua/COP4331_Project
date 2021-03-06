package database;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public class Database {
    private final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    protected String XML_PATH;

    public Database() {
        XML_PATH = "src/database/users.xml";
    }

    public Database(String xmlPath) {
        XML_PATH = xmlPath;
    }

    public Element getBlankElement(String tagName) throws Exception {
        return getDocument(XML_PATH).createElement(tagName);
    }

    public HashMap<Element, Document> getBlankElementAndDocument(String tagName) throws Exception {
        Document document = getDocument(XML_PATH);
        Element element = document.createElement(tagName);
        HashMap<Element, Document> result = new HashMap<>();
        result.put(element, document);
        return result;
    }

    protected Element getElementById(String id) throws Exception {
        Document document = getDocument(XML_PATH);
        XPath xpath =  XPathFactory.newInstance().newXPath();
        String queryString = "//*[@id='"+id+"']";
        Node node = (Node) xpath.evaluate(queryString, document, XPathConstants.NODE);
        return (Element) node;
    }

    protected Element getElementById(Document document, String id) throws Exception {
        XPath xpath =  XPathFactory.newInstance().newXPath();
        String queryString = "//*[@id='"+id+"']";
        Node node = (Node) xpath.evaluate(queryString, document, XPathConstants.NODE);
        return (Element) node;
    }

    protected Element getElementByEmail(String email) throws Exception {
        Document document = getDocument(XML_PATH);
        Node root = document.getElementsByTagName("*").item(0);
        NodeList nodes = ((Element)root).getElementsByTagName("*");

        Node      currentNode = null,
             currentEmailNode = null;

        String  currentEmail = null;
        Element element = null;
        for(int i=0; i<nodes.getLength(); i++) {
            currentNode = nodes.item(i);
            currentEmailNode = ((Element)currentNode).getElementsByTagName("email").item(0);
            if (currentEmailNode != null) {
                currentEmail = currentEmailNode.getTextContent();
                if (currentEmail.equals(email)) {
                    element = (Element) currentNode;
                    break;
                }
            }
        }
        return element;
    }

    protected String getIdByEmail(String email) throws Exception {
        Element element = getElementByEmail(email);
        return element.getAttribute("id");
    }

    protected Document getDocument(String filePath) throws Exception {
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(filePath));
        document.getDocumentElement().normalize();
        return document;
    }

    protected void saveDocument(Document document) throws Exception {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(XML_PATH));
        transformer.transform(source, result);
    }

    protected NodeList getItems() throws Exception {
        Document document = getDocument(XML_PATH);
        Node root = document.getElementsByTagName("*").item(0);
        return ((Element) root).getElementsByTagName("*");

    }

    protected String generateNewID() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    protected void changeAttribute(String elmID, String attributeName, String newValue) throws Exception {
        Document document = getDocument(XML_PATH);
        document.getElementById(elmID).setAttribute(attributeName, newValue);
        saveDocument(document);
    }

    protected Element addElementToRoot(String tagName, HashMap<String, String> childContents) throws Exception {
        Document document = getDocument(XML_PATH);
        Element newElm = document.createElement(tagName);
        for(HashMap.Entry<String, String> entry: childContents.entrySet()) {
            Element childElm = document.createElement(entry.getKey());
            childElm.setTextContent(entry.getValue());
            newElm.appendChild(childElm);
        }
        newElm.setAttribute("id", generateNewID());
        newElm.setAttribute("logged-in", "false");
        document.getDocumentElement().appendChild(newElm);
        saveDocument(document);
        return newElm;
    }

    protected Element addElementToRoot(String tagName, String id, HashMap<String, String> childContents) throws Exception {
        Document document = getDocument(XML_PATH);
        Element newElm = document.createElement(tagName);
        for(HashMap.Entry<String, String> entry: childContents.entrySet()) {
            Element childElm = document.createElement(entry.getKey());
            childElm.setTextContent(entry.getValue());
            newElm.appendChild(childElm);
        }
        newElm.setAttribute("id", id);
        document.getDocumentElement().appendChild(newElm);
        saveDocument(document);
        return newElm;
    }

    protected Element addElementToRoot(String tagName, String id) throws Exception {
        Document document = getDocument(XML_PATH);
        Element newElm = document.createElement(tagName);
        newElm.setAttribute("id", id);
        document.getDocumentElement().appendChild(newElm);
        saveDocument(document);
        return newElm;
    }

    protected NodeList getNodeListByXPath(String xpathString) throws Exception {
        Document document = getDocument(XML_PATH);
        XPath xpath =  XPathFactory.newInstance().newXPath();
        try {
            return (NodeList) xpath.evaluate(xpathString, document, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    protected Node getNodeByXPath(String xpathString) throws Exception {
        Document document = getDocument(XML_PATH);
        XPath xpath =  XPathFactory.newInstance().newXPath();
        try {
            return (Node) xpath.evaluate(xpathString, document, XPathConstants.NODE);
        } catch (XPathExpressionException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
