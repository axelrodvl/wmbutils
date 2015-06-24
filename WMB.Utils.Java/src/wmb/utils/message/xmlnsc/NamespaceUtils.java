package wmb.utils.message.xmlnsc;

import java.util.Map;

import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbXMLNSC;

public class NamespaceUtils {
	/* Recursive namespace removing in tree, start from chosen element */
	public static void removeNamespaces(MbElement elem) throws MbException {
		elem.setNamespace("");
		if(elem.getFirstChild() != null) 
			removeNamespaces(elem.getFirstChild());
		if(elem.getNextSibling() != null) 
			removeNamespaces(elem.getNextSibling());
	}
	
	/* Recursive namespace setting in tree, start from chosen element */
	public static void setNamespaces(MbElement elem, String namespace) throws MbException {
		elem.setNamespace(namespace);
		if(elem.getFirstChild() != null) 
			setNamespaces(elem.getFirstChild(), namespace);
		if(elem.getNextSibling() != null) 
			setNamespaces(elem.getNextSibling(), namespace);
	}
	
	/* Add namespace in XML message root */
	public static void addRootNamespace(MbElement root, String rootTagName,  String namespaceKey, String namespaceValue) throws MbException {
		// If namespace is already exists - delete it
		MbElement oldNamespaceDecl = root.getFirstElementByPath(rootTagName + "/" + namespaceKey);
		if(oldNamespaceDecl != null)
			oldNamespaceDecl.delete();
		
		// Settings new namespace in message root
		MbElement namespaceDecl = root.getFirstElementByPath(rootTagName).createElementAsFirstChild(MbXMLNSC.NAMESPACE_DECLARATION, 
				namespaceKey, namespaceValue);
		namespaceDecl.setNamespace("xmlns");
	}
	
	/* Add namespace in XML message root (from namespaces Map) */
	public static void addRootNamespace(MbElement root, String rootTagName,  String namespaceKey, Map<String, String> namespaces) throws MbException {
		addRootNamespace(root, rootTagName, namespaceKey, namespaces.get(namespaceKey));
	}
	
	/* Adding all namespaces from Map to XML message root */
	public static void addRootNamespaces(MbElement root, String rootTagName, Map<String, String> namespaces) throws MbException {
		for(Map.Entry<String, String> namespace : namespaces.entrySet()) {
			addRootNamespace(root, rootTagName, namespace.getKey(), namespace.getValue());		
		}
	}
}
