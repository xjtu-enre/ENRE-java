package formator;

import formator.fjson.JBuildObject;
import formator.fjson.JDepObject;
import formator.fxml.XBuildObject;
import formator.fxml.XDepObject;

public class Formator {
    private MapObject mapObject;

    public Formator(String[] depTypes) {
        mapObject = new MapObject(depTypes);
    }

    public XDepObject getfXmlDataModel() {
        XBuildObject xBuildObject = new XBuildObject();
        return xBuildObject.buildObjectProcess(mapObject);
    }

    public JDepObject getfJsonDataModel() {
        JBuildObject jBuildObject = new JBuildObject();
        return jBuildObject.buildObjectProcess(mapObject);

        //JsonWriter jasonFormat = new JsonWriter();
        //jasonFormat.toJson(jDepObject);

        //XmlWriter xmlFormat = new XmlWriter();
        //xmlFormat.toXml(xDepObject);
    }


}
