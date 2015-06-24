package wmb.utils.message.headers;

import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbMessageAssembly;

public class Rfh2Utils {
	/* Add new RFH2 String property and set MQMD/Format to MQHRF2 */
	public static void addStringPropertyMQRFH2(MbMessageAssembly messageTree, String name, String value) throws MbException {
		MbElement root = messageTree.getMessage().getRootElement();
		MbElement body = root.getLastChild();
		MbElement psc = null;
		
		MbElement rfh2 = body.getPreviousSibling();
		if(!rfh2.getName().equals("MQRFH2")) {
			rfh2 = body.createElementBefore("MQRFH2");				
			rfh2.createElementAsFirstChild(MbElement.TYPE_NAME_VALUE, "Version", new Integer(2));
			rfh2.createElementAsFirstChild(MbElement.TYPE_NAME_VALUE, "Format", "MQSTR");
			rfh2.createElementAsFirstChild(MbElement.TYPE_NAME_VALUE, "NameValueCCSID", new Integer(1208));
			psc = rfh2.createElementAsFirstChild(MbElement.TYPE_NAME, "usr", null);
			root.getFirstElementByPath("MQMD/Format").setValue("MQHRF2  ");
		} else {
			psc = rfh2.getFirstChild();
		}

		psc.createElementAsFirstChild(MbElement.TYPE_NAME, name, value);		
	}
}
