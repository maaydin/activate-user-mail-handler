package in.maayd.atlassian.mailhandler;

import com.atlassian.configurable.ObjectConfigurationException;
import com.atlassian.jira.plugins.mail.webwork.AbstractEditHandlerDetailsWebAction;
import com.atlassian.jira.service.JiraServiceContainer;
import com.atlassian.jira.service.services.file.AbstractMessageHandlingService;
import com.atlassian.jira.service.util.ServiceUtils;
import com.atlassian.jira.util.collect.MapBuilder;
import com.atlassian.plugin.PluginAccessor;

import java.util.Map;

public class ActivateUserMailHandlerDetailsWebAction extends AbstractEditHandlerDetailsWebAction {

   public ActivateUserMailHandlerDetailsWebAction(PluginAccessor pluginAccessor) {
      super(pluginAccessor);
   }
   private String exceptionFilter;
   public String getExceptionFilter() {
      return exceptionFilter;
   }

   public void setExceptionFilter(String exceptionFilter) {
      this.exceptionFilter = exceptionFilter;
   }

   @Override
   protected void copyServiceSettings(JiraServiceContainer jiraServiceContainer) throws ObjectConfigurationException {
      final String params = jiraServiceContainer.getProperty(AbstractMessageHandlingService.KEY_HANDLER_PARAMS);
      final Map<String, String> parameterMap = ServiceUtils.getParameterMap(params);
      exceptionFilter = parameterMap.get(ActivateUserMailHandler.KEY_EXCEPTIONS);
   }

   @Override
   protected Map<String, String> getHandlerParams() {
      return MapBuilder.build(ActivateUserMailHandler.KEY_EXCEPTIONS, this.exceptionFilter);
   }

   @Override
   protected void doValidation() {
      if (configuration == null) {
         return;
      }
      super.doValidation();
   }
}