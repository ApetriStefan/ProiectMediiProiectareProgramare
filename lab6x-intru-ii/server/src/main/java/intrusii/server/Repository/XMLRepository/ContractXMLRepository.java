package intrusii.server.Repository.XMLRepository;


import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import intrusii.server.Domain.Contract;
import intrusii.server.Domain.Validators.ContractException;
import intrusii.server.Domain.Validators.Validator;
import intrusii.server.Domain.Validators.ValidatorException;
import intrusii.server.Repository.InMemoryRepository;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.io.*;
import java.time.LocalDate;
import java.util.Optional;


public class ContractXMLRepository extends InMemoryRepository<Long, Contract> {

    private String fileName;
    private static Long idGenerator = 0L;

    public ContractXMLRepository(Validator<Contract> validator, String fileName){
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
            NodeList nList = doc.getElementsByTagName("Contract");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    Long id = Long.valueOf( eElement.getAttribute("id"));
                    String clientId = eElement.getElementsByTagName("clientId").item(0).getTextContent();
                    String subscriptionId = eElement.getElementsByTagName("subscriptionId").item(0).getTextContent();
                    String date = eElement.getElementsByTagName("date").item(0).getTextContent();
                    if (id > idGenerator)
                        idGenerator = id;

                    Contract contract = new Contract(Long.valueOf(clientId),Long.valueOf(subscriptionId), LocalDate.parse(date));
                    contract.setId(id);
                    try {
                        super.save(contract);
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

    private void writeOneToXML(Contract contract) throws TransformerException, IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.parse(new File(fileName));
        Element Contracts = doc.getDocumentElement();
        Element c = doc.createElement("Contract");

        Attr attr = doc.createAttribute("id");
        attr.setValue(String.valueOf(contract.getId()));
        c.setAttributeNode(attr);

        Element clientId = doc.createElement("clientId");
        clientId.appendChild(doc.createTextNode(String.valueOf(contract.getClientId())));
        c.appendChild(clientId);

        Element subscriptionId= doc.createElement("subscriptionId");
        subscriptionId.appendChild(doc.createTextNode(String.valueOf(contract.getSubscriptionId())));
        c.appendChild(subscriptionId);

        Element date = doc.createElement("date");
        date.appendChild(doc.createTextNode(String.valueOf(contract.getDate())));
        c.appendChild(date);

        Contracts.appendChild(c);

        DOMSource source = new DOMSource(doc);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        StreamResult result = new StreamResult(fileName);
        transformer.transform(source, result);
    }

    @Override
    public Optional<Contract> save(Contract entity) throws ValidatorException {
        entity.setId(getNextId());
        Optional<Contract> optional = super.save(entity);
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
    public Optional<Contract> delete(Long id) {
        try {
            idGenerator--;
            removeFromXML(super.findOne(id).get());
            Optional<Contract> contractTemp = super.delete(id);
            return contractTemp;
        }
        catch(RuntimeException ex) {
            throw new ContractException(ex);
        }
    }

    private void removeFromXML(Contract contract) {
        try {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(fileName);
        Node Contracts = doc.getFirstChild();
        NodeList childNodes = Contracts.getChildNodes();
        for (int count = 0; count < childNodes.getLength(); count++) {
            Node node = childNodes.item(count);

            if(node.getTextContent().contains(String.valueOf(contract.getClientId())))
                if(node.getTextContent().contains(String.valueOf(contract.getSubscriptionId())))
                    if(node.getTextContent().contains(String.valueOf(contract.getDate())))
                        {
                            Contracts.removeChild(node);
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
    public Optional<Contract> update(Contract entity) throws ContractException {
        try {
            Optional<Contract> oldContract = super.findOne(entity.getId());
            Optional<Contract> contract = super.update(entity);
            updateXMLFile(contract.get(),oldContract.get());
            return contract;
        }
        catch(RuntimeException ex) {
            throw new ContractException(ex);
        }
    }

    private void updateXMLFile(Contract contract, Contract oldContract) {
        try {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(fileName);
        Node Contracts = doc.getFirstChild();
        NodeList childNodes = Contracts.getChildNodes();
        for (int count = 0; count < childNodes.getLength(); count++) {
            Node node = childNodes.item(count);

            if(node.getTextContent().contains(String.valueOf(oldContract.getClientId())))
                if(node.getTextContent().contains(String.valueOf(oldContract.getSubscriptionId())))
                    if(node.getTextContent().contains(String.valueOf(oldContract.getDate())))
                        {
                            NamedNodeMap attr = node.getAttributes();
                            Node nodeAttr = attr.getNamedItem("id");
                            nodeAttr.setTextContent(String.valueOf(contract.getId()));
                            NodeList list = node.getChildNodes();
                            for (int temp = 0; temp < list.getLength(); temp++) {
                                Node c = list.item(temp);
                                if (node.getNodeType() == Node.ELEMENT_NODE) {
                                    Element eElement = (Element) c;
                                    if ("clientId".equals(eElement.getNodeName()))
                                    {
                                        eElement.setTextContent(String.valueOf(contract.getClientId()));
                                    }
                                    if ("subscriptionId".equals(eElement.getNodeName()))
                                    {
                                        eElement.setTextContent(String.valueOf(contract.getSubscriptionId()));
                                    }
                                    if ("date".equals(eElement.getNodeName()))
                                    {
                                        eElement.setTextContent(String.valueOf(contract.getDate()));
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
