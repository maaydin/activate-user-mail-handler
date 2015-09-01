package in.maayd.atlassian.mailhandler;

import com.atlassian.crowd.embedded.api.CrowdService;
import com.atlassian.crowd.embedded.api.User;
import com.atlassian.crowd.model.user.UserTemplate;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.service.util.handler.MessageHandler;
import com.atlassian.jira.service.util.handler.MessageHandlerContext;
import com.atlassian.jira.service.util.handler.MessageHandlerErrorCollector;
import com.atlassian.jira.service.util.handler.MessageUserProcessor;

import javax.mail.Message;
import javax.mail.MessagingException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivateUserMailHandler implements MessageHandler {
   private String exceptionFilter;
	private final MessageUserProcessor messageUserProcessor;
	public static final String KEY_EXCEPTIONS = "exceptionFilter";

    // we can use dependency injection here too!
	public ActivateUserMailHandler(MessageUserProcessor messageUserProcessor) {
		this.messageUserProcessor = messageUserProcessor;
	}

	@Override
	public void init(Map<String, String> params, MessageHandlerErrorCollector monitor) {
      String exceptionFilter = params.get(KEY_EXCEPTIONS);
      if(exceptionFilter == null) {
         exceptionFilter = "";
      }
      this.exceptionFilter = exceptionFilter;
	}

	@Override
	public boolean handleMessage(Message message, MessageHandlerContext context) throws MessagingException {
      try {
         CrowdService crowdService = ComponentAccessor.getCrowdService();

         String fromAddress = message.getFrom()[0].toString();
         Pattern pattern = Pattern.compile(exceptionFilter);
         Matcher matcher = pattern.matcher(fromAddress);
         boolean matches = matcher.matches();
         if(matches) {
            context.getMonitor().info("User account for '" + fromAddress + "' is not activated as it matches with the exception filter '" + exceptionFilter + "'.");
            return false;
         }

         User sender = messageUserProcessor.getAuthorFromSender(message);
         if (sender != null) {
            UserTemplate user = new UserTemplate(sender);
            user.setActive(true);
            crowdService.updateUser(user);
            context.getMonitor().info("User account for '" + sender.getEmailAddress() + "' activated.");
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
		return false;
	}
}

