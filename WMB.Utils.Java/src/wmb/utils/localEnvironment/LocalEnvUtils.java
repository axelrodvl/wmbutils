package wmb.utils.localEnvironment;

import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbMessageAssembly;

public class LocalEnvUtils {
	public static void addValueLocalEnv(MbMessageAssembly messageTree, String key, String value) throws MbException {
		messageTree.getLocalEnvironment().getRootElement().createElementAsLastChild(MbElement.TYPE_NAME, key, value);		
	}
	
	public static String getValueLocalEnv(MbMessageAssembly messageTree, String key) throws MbException {
		MbElement localEnvKeyElement = messageTree.getLocalEnvironment().getRootElement().getFirstElementByPath(key);
		if(localEnvKeyElement == null)
			return null;
		else
			return localEnvKeyElement.getValueAsString();
	}
}
