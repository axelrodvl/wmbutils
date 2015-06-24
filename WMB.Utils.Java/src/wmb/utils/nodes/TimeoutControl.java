package wmb.utils.nodes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbMessage;
import com.ibm.broker.plugin.MbMessageAssembly;

public class TimeoutControl {
	/* Add timer start instruction message for TimeoutControl node */
	public static void startTimer(MbMessageAssembly messageTree, int timeoutInMs, String timeoutIdentifier) throws MbException {
		// Create TimeoutRequest in LocalEnvironment
		MbMessage env = messageTree.getLocalEnvironment();
		env.getRootElement().getFirstElementByPath("TimeoutRequest").delete();			
		
		// Adding payload to message
		MbElement timeoutRequest = env.getRootElement().createElementAsFirstChild(MbElement.TYPE_NAME, "TimeoutRequest", "");
		timeoutRequest.createElementAsFirstChild(MbElement.TYPE_NAME_VALUE, "Action", "SET");
		timeoutRequest.createElementAsFirstChild(MbElement.TYPE_NAME_VALUE, "Identifier", timeoutIdentifier);
		
		// Calculate date and time for timer
		Calendar calendar = Calendar.getInstance(); 
		calendar.add(Calendar.SECOND, timeoutInMs);
		calendar.add(Calendar.HOUR, -1); // Workaround for Moscow time zone
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat dateTimeFormat = new SimpleDateFormat("HH:mm:ss.SSSSSS");
		
		timeoutRequest.createElementAsLastChild(MbElement.TYPE_NAME_VALUE,"StartDate",dateFormat.format(calendar.getTime()));
		timeoutRequest.createElementAsLastChild(MbElement.TYPE_NAME_VALUE,"StartTime",dateTimeFormat.format(calendar.getTime()));
		timeoutRequest.createElementAsFirstChild(MbElement.TYPE_NAME_VALUE, "IgnoreMissed", "FALSE");
		timeoutRequest.createElementAsFirstChild(MbElement.TYPE_NAME_VALUE, "AllowOverwrite", "FALSE");
	}
	
	/* Add timer stop instruction message for TimeoutControl node */
	public static void stopTimer(MbMessageAssembly messageTree, String timeoutIdentifier) throws MbException {
		// Create TimeoutRequest in LocalEnvironment
		MbMessage env = messageTree.getLocalEnvironment();
		env.getRootElement().getFirstElementByPath("TimeoutRequest").delete();

		// Adding payload to message
		MbElement timeoutRequest = env.getRootElement().createElementAsFirstChild(MbElement.TYPE_NAME, "TimeoutRequest", "");
		timeoutRequest.createElementAsFirstChild(MbElement.TYPE_NAME_VALUE, "Action", "CANCEL");
		timeoutRequest.createElementAsFirstChild(MbElement.TYPE_NAME_VALUE, "Identifier", timeoutIdentifier);
	}
}
