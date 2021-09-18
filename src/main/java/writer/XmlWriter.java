package writer;

import formator.fxml.XDepObject;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class XmlWriter {

    public void toXml(Object xDepObject, String fileName)  {

        try {
            //XDepObject class 
            JAXBContext jaxbContext = JAXBContext.newInstance(XDepObject.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(xDepObject, new FileOutputStream(fileName));
        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
