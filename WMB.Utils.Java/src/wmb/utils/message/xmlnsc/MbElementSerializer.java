package wmb.utils.message.xmlnsc;

import static wmb.utils.message.xmlnsc.NamespaceUtils.removeNamespaces;

import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbMessage;
import com.ibm.broker.plugin.MbXMLNSC;

public class MbElementSerializer {
	/* Serialize MbElement root with child-elements to String without namespaces */
	public static String serialize(MbElement elem, int encoding, int ccsid) throws MbException {
		// Creating new MbMessage in order to provide ability of serializing any MbElement
		MbMessage message = new MbMessage();
		MbElement outRoot = message.getRootElement();
		MbElement xmlRoot = outRoot.createElementAsLastChild(MbXMLNSC.PARSER_NAME);
		MbElement xmlBody = xmlRoot.createElementAsFirstChild(MbXMLNSC.FIELD, "SerializedMessage", null);
		xmlBody.copyElementTree(elem);
		removeNamespaces(xmlBody);
		
		byte[] elemAsBytes = outRoot.toBitstream(
			null, // messageType - The message type definition used to create the bit stream from the element tree. A value of null will cause this parameter to be ignored.
			null, // messageSet - The message set definition used to create the bit stream from the element tree. A value of null will cause this parameter to be ignored.
			null, // messageFormat - The message format definition used to create the bit stream from the element tree. A value of null will cause this parameter to be ignored.
			encoding, // encoding - The encoding to use when writing the bit stream. A special value of 0 may be supplied to indicate that the queue manager's encoding should be used.
			ccsid, // ccsid - The coded character set identifier to use when writing the bit stream. 
			0); // options - Ignored. This is a place holder for future enhancements.
		return new String(elemAsBytes);
	}
	
	/* Unserialize String with MbElement inside */
	public static MbElement unserialize(String mbElementAsString, int encoding, int ccsid) throws MbException {
		byte[] mbElementAsBytes = mbElementAsString.getBytes(java.nio.charset.StandardCharsets.UTF_8);
		MbMessage temp = new MbMessage();
		temp.getRootElement().createElementAsLastChildFromBitstream(
				mbElementAsBytes, // bitstream - The bit stream to be parsed and added to the element tree.      
				MbXMLNSC.PARSER_NAME, // parserName - The name of the parser class to use to parse the bit stream. The same parser must be used to parse the whole bit stream.
				null, // messageType - The message type definition used to create the element tree from the bit stream. A value of null will cause this parameter to be ignored.
				null, // messageSet - The message set definition used to create the element tree from the bit stream. A value of null will cause this parameter to be ignored.
				null, // messageFormat - The message format definition used to create the element tree from the bit stream. A value of null will cause this parameter to be ignored.
				encoding, // encoding - The encoding to use when parsing the bit stream. A special value of 0 may be supplied to indicate that the queue manager's encoding should be used.
				ccsid, // ccsid - The coded character set identifier to use when parsing the bit stream. This parameter is mandatory. A special value of 0 may be supplied to indicate that the queue manager's ccsid should be used.
				0); // options - Ignored. This is a place holder for future enhancements.
		
		MbMessage deserialized = new MbMessage();
		MbElement outRoot = deserialized.getRootElement();
		MbElement xmlRoot = outRoot.createElementAsLastChild(MbXMLNSC.PARSER_NAME);
		
		MbElement serializedRootFirstLevelChild = temp.getRootElement().getLastChild().getFirstChild().getFirstChild();
		while(serializedRootFirstLevelChild != null) {
			xmlRoot.addAsLastChild(serializedRootFirstLevelChild.copy());
			serializedRootFirstLevelChild = serializedRootFirstLevelChild.getNextSibling();
		}
		
		return deserialized.getRootElement().getLastChild();
	}
}
