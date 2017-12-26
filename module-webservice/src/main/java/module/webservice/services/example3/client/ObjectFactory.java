
package module.webservice.services.example3.client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the module.webservice.services.example.client package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _CountRabbits_QNAME = new QName("http://example3.services.webservice.module/", "countRabbits");
    private final static QName _Exception_QNAME = new QName("http://example3.services.webservice.module/", "Exception");
    private final static QName _CountRabbitsResponse_QNAME = new QName("http://example3.services.webservice.module/", "countRabbitsResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: module.webservice.services.example.client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Exception }
     * 
     */
    public Exception createException() {
        return new Exception();
    }

    /**
     * Create an instance of {@link CountRabbitsResponse }
     * 
     */
    public CountRabbitsResponse createCountRabbitsResponse() {
        return new CountRabbitsResponse();
    }

    /**
     * Create an instance of {@link CountRabbits }
     * 
     */
    public CountRabbits createCountRabbits() {
        return new CountRabbits();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CountRabbits }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://example3.services.webservice.module/", name = "countRabbits")
    public JAXBElement<CountRabbits> createCountRabbits(CountRabbits value) {
        return new JAXBElement<CountRabbits>(_CountRabbits_QNAME, CountRabbits.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Exception }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://example3.services.webservice.module/", name = "Exception")
    public JAXBElement<Exception> createException(Exception value) {
        return new JAXBElement<Exception>(_Exception_QNAME, Exception.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CountRabbitsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://example3.services.webservice.module/", name = "countRabbitsResponse")
    public JAXBElement<CountRabbitsResponse> createCountRabbitsResponse(CountRabbitsResponse value) {
        return new JAXBElement<CountRabbitsResponse>(_CountRabbitsResponse_QNAME, CountRabbitsResponse.class, null, value);
    }

}
