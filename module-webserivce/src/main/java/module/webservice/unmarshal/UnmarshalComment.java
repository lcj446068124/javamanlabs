package module.webservice.unmarshal;

import module.webservice.entity.Comment;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;

/**
 * Created by root on 2015/12/29.
 */
public class UnmarshalComment {
    public static void main(String[] args) throws Exception {
        String path = "E:\\github\\spring-integration-demo\\module-webserivce\\src\\main\\resources\\xsds\\schema1.xsd.xml";
        JAXBContext jc = JAXBContext.newInstance(Comment.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        Comment comment = (Comment) unmarshaller.unmarshal(new FileInputStream(path));
        System.out.println(comment.getContent());
        System.out.println(comment.getId());


        // Modify marshal
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(comment, System.out);
    }
}
