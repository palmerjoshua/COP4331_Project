package database;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.LinkedHashMap;

public class BandDB extends Database {

    public static BandDB getBandDB() {return database;}
    public NodeList getBands() throws Exception {
        return getItems();
    }

    public void addBand(Element userElement) throws Exception {
        String email = userElement.getElementsByTagName("email").item(0).getTextContent(),
                id = userElement.getAttribute("id");
        LinkedHashMap<String, String> children = new LinkedHashMap<String, String>();
        children.put("email", email);
        children.put("profileImage", "src/images/defaultUserImg.png");
        children.put("members", "");
        children.put("need", "");
        addElementToRoot("band", id, children);
    }

    public void addBandMember(String bandID, String memberID) {

    }

    private static BandDB database = new BandDB();
    private BandDB() {XML_PATH = "src/database/bands.xml";}
}
