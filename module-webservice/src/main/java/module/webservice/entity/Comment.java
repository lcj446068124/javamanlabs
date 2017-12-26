
package module.webservice.entity;

import javax.xml.bind.annotation.*;
import java.util.Date;

@XmlRootElement(name="comment")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "comment", propOrder = {
        "content",
        "date",
        "id"
})
public class Comment {

    @XmlElement(required = false)
    protected String content;

    @XmlSchemaType(name = "dateTime")
    protected Date date;

    @XmlElement(required = true)
    protected int id;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
