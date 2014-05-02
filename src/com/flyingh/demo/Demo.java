package com.flyingh.demo;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Demo {

	@Test
	public void test7() throws SAXException, IOException, ParserConfigurationException, XPathExpressionException, TransformerConfigurationException,
			TransformerException, TransformerFactoryConfigurationError {
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.parse(getClass().getClassLoader().getResourceAsStream("books.xml"));
		NodeList nodeList = (NodeList) XPathFactory.newInstance().newXPath()
				.evaluate("//book[title='Learning XML']", document, XPathConstants.NODESET);
		Element bookElement = (Element) nodeList.item(0);
		Element priceElement = (Element) bookElement.getElementsByTagName("price").item(0);
		System.out.println(priceElement.getTextContent());
		priceElement.setTextContent("3995");
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.transform(new DOMSource(document), new StreamResult(System.out));
	}

	@Test
	public void Test6() throws XMLStreamException, FactoryConfigurationError {
		XMLEventWriter xmlEventWriter = XMLOutputFactory.newInstance().createXMLEventWriter(System.out);
		XMLEventFactory xmlEventFactory = XMLEventFactory.newInstance();
		xmlEventWriter.add(xmlEventFactory.createStartDocument("utf-8", "1.0"));
		xmlEventWriter.add(xmlEventFactory.createStartElement("ns", null, "books"));
		xmlEventWriter.add(xmlEventFactory.createStartElement("ns", null, "book"));
		xmlEventWriter.add(xmlEventFactory.createAttribute("id", "1"));
		xmlEventWriter.add(xmlEventFactory.createStartElement("ns", null, "name"));
		xmlEventWriter.add(xmlEventFactory.createCharacters("Java"));
		xmlEventWriter.add(xmlEventFactory.createEndElement("ns", null, "name"));
		xmlEventWriter.add(xmlEventFactory.createStartElement("ns", null, "price"));
		xmlEventWriter.add(xmlEventFactory.createCharacters("88.8"));
		xmlEventWriter.add(xmlEventFactory.createEndElement("ns", null, "price"));
		xmlEventWriter.add(xmlEventFactory.createEndElement("ns", null, "book"));
		xmlEventWriter.add(xmlEventFactory.createEndElement("ns", null, "books"));
		xmlEventWriter.add(xmlEventFactory.createEndDocument());
		xmlEventWriter.close();
	}

	@Test
	public void test5() throws XMLStreamException, FactoryConfigurationError {
		XMLStreamWriter xmlStreamWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(System.out);
		xmlStreamWriter.writeStartDocument("utf-8", "1.0");

		xmlStreamWriter.writeStartElement("persons");

		xmlStreamWriter.writeStartElement("person");
		xmlStreamWriter.writeAttribute("id", "1");

		xmlStreamWriter.writeStartElement("name");
		xmlStreamWriter.writeCharacters("zhangsan");
		xmlStreamWriter.writeEndElement();

		xmlStreamWriter.writeStartElement("age");
		xmlStreamWriter.writeCharacters("22");
		xmlStreamWriter.writeEndElement();

		xmlStreamWriter.writeEndElement();

		xmlStreamWriter.writeEndElement();

		xmlStreamWriter.writeEndDocument();
		xmlStreamWriter.close();
	}

	@Test
	public void test4() throws SAXException, IOException, ParserConfigurationException, XPathExpressionException {
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.parse(getClass().getClassLoader().getResourceAsStream("books.xml"));
		NodeList nodeList = (NodeList) XPathFactory.newInstance().newXPath().evaluate("//book[@category='WEB']", document, XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element item = (Element) nodeList.item(i);
			System.out.println(item.getElementsByTagName("title").item(0).getTextContent());
		}
	}

	@Test
	public void test3() throws XMLStreamException, FactoryConfigurationError {
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLEventReader xmlEventReader = factory.createFilteredReader(
				factory.createXMLEventReader(getClass().getClassLoader().getResourceAsStream("books.xml")),
				(e) -> e.isStartElement() && "title".equals(e.asStartElement().getName().getLocalPart()));
		while (xmlEventReader.hasNext()) {
			xmlEventReader.nextEvent();
			System.out.println(xmlEventReader.getElementText());
		}
	}

	@Test
	public void test2() throws XMLStreamException, FactoryConfigurationError {
		XMLEventReader xmlEventReader = XMLInputFactory.newFactory().createXMLEventReader(
				(getClass().getClassLoader().getResourceAsStream("books.xml")));
		while (xmlEventReader.hasNext()) {
			XMLEvent event = xmlEventReader.nextEvent();
			if (event.isStartElement()) {
				System.out.println(event.asStartElement().getName().getLocalPart());
			} else if (event.isCharacters()) {
				System.out.println(event.asCharacters().getData());
			} else if (event.isEndElement()) {
				System.out.println(event.asEndElement().getName().getLocalPart());
			}
		}
	}

	@Test
	public void test() throws XMLStreamException, FactoryConfigurationError {
		XMLStreamReader xmlStreamReader = XMLInputFactory.newFactory().createXMLStreamReader(
				getClass().getClassLoader().getResourceAsStream("books.xml"));
		while (xmlStreamReader.hasNext()) {
			xmlStreamReader.next();
			if (xmlStreamReader.isStartElement()) {
				String name = xmlStreamReader.getName().getLocalPart();
				if ("book".equals(name)) {
					System.out.println(xmlStreamReader.getAttributeName(0) + ":" + xmlStreamReader.getAttributeValue(0));
				} else if ("title".equals(name) || "price".equals(name)) {
					System.out.println(name + "->" + xmlStreamReader.getElementText());
				}
			}
		}
	}
}
