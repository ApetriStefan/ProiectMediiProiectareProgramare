package intrusii.server.Repository.XMLRepository;


import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import intrusii.common.Domain.Client;
import intrusii.common.Domain.Validators.ContractException;
import intrusii.common.Domain.Validators.Validator;
import intrusii.common.Domain.Validators.ValidatorException;
import intrusii.server.Repository.InMemoryRepository;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.Optional;


public class ClientXMLRepository extends InMemoryRepository<Long, Client> {

    private String fileName;
    private static Long idGenerator = 0L;

    public ClientXMLRepository(Validator<Client> validator, String fileName) {
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
            NodeList nList = doc.getElementsByTagName("Client");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    Long id = Long.valueOf( eElement.getAttribute("id"));
                    String cnp = eElement.getElementsByTagName("cnp").item(0).getTextContent();
                    String name = eElement.getElementsByTagName("name").item(0).getTextContent();
                    String email = eElement.getElementsByTagName("email").item(0).getTextContent();
                    String address = eElement.getElementsByTagName("address").item(0).getTextContent();
                    if (id > idGenerator)
                        idGenerator = id;

                    Client client = new Client(cnp,name,email,address);
                    client.setId(id);
                    try {
                        super.save(client);
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

    private void writeOneToXML(Client client) throws TransformerException, IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.parse(new File(fileName));
        Element Clients = doc.getDocumentElement();
        Element c = doc.createElement("Client");

        Attr attr = doc.createAttribute("id");
        attr.setValue(String.valueOf(client.getId()));
        c.setAttributeNode(attr);

        Element cnp = doc.createElement("cnp");
        cnp.appendChild(doc.createTextNode(client.getCnp()));
        c.appendChild(cnp);

        Element name= doc.createElement("name");
        name.appendChild(doc.createTextNode(client.getName()));
        c.appendChild(name);

        Element email = doc.createElement("email");
        email.appendChild(doc.createTextNode(client.getEmail()));
        c.appendChild(email);

        Element address = doc.createElement("address");
        address.appendChild(doc.createTextNode(client.getAddress()));
        c.appendChild(address);

        Clients.appendChild(c);

        DOMSource source = new DOMSource(doc);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        StreamResult result = new StreamResult(fileName);
        transformer.transform(source, result);
    }

    @Override
    public Optional<Client> save(Client entity) throws ValidatorException {
        entity.setId(getNextId());
        Optional<Client> optional = super.save(entity);
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
    public Optional<Client> delete(Long id) {
        try {
            idGenerator--;
            removeFromXML(super.findOne(id).get());
            Optional<Client> clientTemp = super.delete(id);
            return clientTemp;
        }
        catch(RuntimeException ex) {
            throw new ContractException(ex);
        }
    }

    private void removeFromXML(Client client) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(fileName);
            Node Clients = doc.getFirstChild();
            NodeList childNodes = Clients.getChildNodes();
            for (int count = 0; count < childNodes.getLength(); count++) {
                Node node = childNodes.item(count);

                if(node.getTextContent().contains(client.getCnp()))
                    if(node.getTextContent().contains(client.getName()))
                        if(node.getTextContent().contains(client.getEmail()))
                            if(node.getTextContent().contains(client.getAddress()))
                            {
                                Clients.removeChild(node);
                            }
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult(new PrintWriter(new FileOutputStream(fileName, false)));
            DOMSource source = new DOMSource(doc);
            transformer.transform(source, result);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Client> update(Client entity) throws ContractException {
        try {
            Optional<Client> oldClient = super.findOne(entity.getId());
            Optional<Client> client = super.update(entity);
            updateXMLFile(client.get(),oldClient.get());
            return client;
        }
        catch(RuntimeException ex) {
            throw new ContractException(ex);
        }
    }

    private void updateXMLFile(Client client, Client oldClient) {
        try {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(fileName);
        Node Clients = doc.getFirstChild();
        NodeList childNodes = Clients.getChildNodes();
        for (int count = 0; count < childNodes.getLength(); count++) {
            Node node = childNodes.item(count);

            if(node.getTextContent().contains(oldClient.getCnp()))
                if(node.getTextContent().contains(oldClient.getName()))
                    if(node.getTextContent().contains(oldClient.getEmail()))
                        if(node.getTextContent().contains(oldClient.getAddress()))
                        {
                            NamedNodeMap attr = node.getAttributes();
                            Node nodeAttr = attr.getNamedItem("id");
                            nodeAttr.setTextContent(String.valueOf(client.getId()));
                            NodeList list = node.getChildNodes();
                            for (int temp = 0; temp < list.getLength(); temp++) {
                                Node c = list.item(temp);
                                if (node.getNodeType() == Node.ELEMENT_NODE) {
                                    Element eElement = (Element) c;
                                    if ("cnp".equals(eElement.getNodeName()))
                                    {
                                            eElement.setTextContent(String.valueOf(client.getCnp()));
                                    }
                                    if ("name".equals(eElement.getNodeName()))
                                    {
                                        eElement.setTextContent(String.valueOf(client.getName()));
                                    }
                                    if ("email".equals(eElement.getNodeName()))
                                    {
                                        eElement.setTextContent(String.valueOf(client.getEmail()));
                                    }
                                    if ("address".equals(eElement.getNodeName()))
                                    {
                                        eElement.setTextContent(String.valueOf(client.getAddress()));
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
        e.printStackTrace(); }
    }
}
