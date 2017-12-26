package module.webservice.services.example3;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Created by root on 2015/12/30.
 */
public class UUIDHandler implements SOAPHandler<SOAPMessageContext> {
    private static final String LoggerName = "ClientSideLogger";
    private Logger logger;
    private final boolean log_p = true; // set to false to turn off

    public UUIDHandler() {
        logger = Logger.getLogger(LoggerName);
    }


    @Override
    public Set<QName> getHeaders() {
        if (log_p) logger.info("getHeaders");
        return null;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        if (log_p) logger.info("handleMessage");
        // Is this an outbound message, i.e., a request?
        Boolean request_p = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        // Manipulate the SOAP only if it's a request
        if (request_p) {
            // Generate a UUID and a timestamp to place in the message header.
            UUID uuid = UUID.randomUUID();
            try {
                SOAPMessage msg = context.getMessage();
                SOAPEnvelope env = msg.getSOAPPart().getEnvelope();
                SOAPHeader hdr = env.getHeader();
                // Ensure that the SOAP message has a header.
                if (hdr == null) hdr = env.addHeader();
                QName qname = new QName("http://example3.services.webservice.module", "uuid");
                SOAPHeaderElement helem = hdr.addHeaderElement(qname);
                helem.setActor(SOAPConstants.URI_SOAP_ACTOR_NEXT); // default
                helem.addTextNode(uuid.toString());
                msg.saveChanges();
                // For tracking, write to standard output.
                msg.writeTo(System.out);
            } catch (SOAPException | IOException e) {
                System.err.println(e);
            }
        }
        return true; // continue down the chain
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        if (log_p) logger.info("handleFault");
        try {
            context.getMessage().writeTo(System.out);
        } catch (SOAPException | IOException e) {
            System.err.println(e);
        }
        return true;
    }

    @Override
    public void close(MessageContext context) {
        if (log_p) logger.info("close");
    }
}
