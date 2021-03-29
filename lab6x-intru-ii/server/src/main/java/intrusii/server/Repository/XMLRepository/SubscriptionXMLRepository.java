package intrusii.server.Repository.XMLRepository;

import intrusii.common.Domain.Subscription;
import intrusii.common.Domain.SubscriptionType;
import intrusii.common.Domain.Validators.ContractException;
import intrusii.common.Domain.Validators.Validator;
import intrusii.common.Domain.Validators.ValidatorException;
import intrusii.server.Repository.InMemoryRepository;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;


public class SubscriptionXMLRepository extends InMemoryRepository<Long, Subscription> {

    private String fileName;
    private static Long idGenerator = 0L;

    public SubscriptionXMLRepository(Validator<Subscription> validator, String fileName) {
        super(validator);
        this.fileName = fileName;
        loadData();
    }

    private void loadData() {
        try {
            File inputFile = new File(fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("Subscription");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    Long id = Long.valueOf( eElement.getAttribute("id"));
                    String type = eElement.getElementsByTagName("type").item(0).getTextContent();
                    String price = eElement.getElementsByTagName("price").item(0).getTextContent();
                    String duration = eElement.getElementsByTagName("duration").item(0).getTextContent();
                    if (id > idGenerator)
                        idGenerator = id;

                    Subscription subscription = new Subscription(SubscriptionType.valueOf(type),Float.parseFloat(price),Integer.parseInt(duration));
                    subscription.setId(id);
                    try {
                        super.save(subscription);
                    } catch (ValidatorException e) {
                        e.printStackTrace();
                    }
                    idGenerator++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Long getNextId(){
        return idGenerator++;
    }

    private void writeOneToXML(Subscription subscription) throws TransformerException, IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.parse(new File(fileName));
        Element Subscriptions = doc.getDocumentElement();
        Element s = doc.createElement("Subscription");

        Attr attr = doc.createAttribute("id");
        attr.setValue(String.valueOf(subscription.getId()));
        s.setAttributeNode(attr);

        Element type = doc.createElement("type");
        type.appendChild(doc.createTextNode(String.valueOf(subscription.getType())));
        s.appendChild(type);

        Element price= doc.createElement("price");
        price.appendChild(doc.createTextNode(String.valueOf(subscription.getPrice())));
        s.appendChild(price);

        Element duration = doc.createElement("duration");
        duration.appendChild(doc.createTextNode(String.valueOf(subscription.getDuration())));
        s.appendChild(duration);

        Subscriptions.appendChild(s);

        DOMSource source = new DOMSource(doc);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        StreamResult result = new StreamResult(fileName);
        transformer.transform(source, result);
    }

    @Override
    public Optional<Subscription> save(Subscription entity) throws ValidatorException {
        entity.setId(getNextId());
        Optional<Subscription> optional = super.save(entity);
        if (optional.isPresent()) {
            return optional;
        }
        try {
            writeOneToXML(entity);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public Optional<Subscription> delete(Long id) {
        try {
            idGenerator--;
            removeFromXML(super.findOne(id).get());
            Optional<Subscription> subscriptionTemp = super.delete(id);
            return subscriptionTemp;
        }
        catch(RuntimeException ex) {
            throw new ContractException(ex);
        }
    }

    private void removeFromXML(Subscription subscription) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(fileName);
            Node Subscriptions = doc.getFirstChild();
            NodeList childNodes = Subscriptions.getChildNodes();
            for (int count = 0; count < childNodes.getLength(); count++) {
                Node node = childNodes.item(count);

                if(node.getTextContent().contains(String.valueOf(subscription.getType())))
                    if(node.getTextContent().contains(String.valueOf(subscription.getPrice())))
                        if(node.getTextContent().contains(String.valueOf(subscription.getDuration()))) {
                            Subscriptions.removeChild(node);
                        }
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult(new PrintWriter(new FileOutputStream(fileName, false)));
            DOMSource source = new DOMSource(doc);
            transformer.transform(source, result);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public Optional<Subscription> update(Subscription entity) throws ContractException {
        try {
            Optional<Subscription> oldSubscription = super.findOne(entity.getId());
            Optional<Subscription> subscription = super.update(entity);
            updateXMLFile(subscription.get(),oldSubscription.get());
            return subscription;
        }
        catch(RuntimeException ex) {
            throw new ContractException(ex);
        }
    }

    private void updateXMLFile(Subscription subscription, Subscription oldSubscription) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(fileName);
            Node Subscriptions = doc.getFirstChild();
            NodeList childNodes = Subscriptions.getChildNodes();
            for (int count = 0; count < childNodes.getLength(); count++) {
                Node node = childNodes.item(count);

            if(node.getTextContent().contains(String.valueOf(oldSubscription.getType())))
                if(node.getTextContent().contains(String.valueOf(oldSubscription.getPrice())))
                    if(node.getTextContent().contains(String.valueOf(oldSubscription.getDuration())))
                    {
                        NamedNodeMap attr = node.getAttributes();
                        Node nodeAttr = attr.getNamedItem("id");
                        nodeAttr.setTextContent(String.valueOf(subscription.getId()));
                        NodeList list = node.getChildNodes();
                        for (int temp = 0; temp < list.getLength(); temp++) {
                            Node c = list.item(temp);
                            if (node.getNodeType() == Node.ELEMENT_NODE) {
                                Element eElement = (Element) c;
                                if ("type".equals(eElement.getNodeName()))
                                {
                                    eElement.setTextContent(String.valueOf(subscription.getType()));
                                }
                                if ("price".equals(eElement.getNodeName()))
                                {
                                    eElement.setTextContent(String.valueOf(subscription.getPrice()));
                                }
                                if ("duration".equals(eElement.getNodeName()))
                                {
                                    eElement.setTextContent(String.valueOf(subscription.getDuration()));
                                }
                            }
                        }
                    }
        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        StreamResult result = new StreamResult(new PrintWriter(new FileOutputStream(fileName, false)));
        DOMSource source = new DOMSource(doc);
        transformer.transform(source, result);
    }
    catch (Exception e) {
        e.printStackTrace();
    }
    }
}
