package in.maayd.atlassian.mailhandler;

import com.atlassian.sal.api.ApplicationProperties;

public class ActivateUserMailHandlerComponent implements IActivateUserMailHandlerComponent
{
    private final ApplicationProperties applicationProperties;

    public ActivateUserMailHandlerComponent(ApplicationProperties applicationProperties)
    {
        this.applicationProperties = applicationProperties;
    }

    public String getName()
    {
        if(null != applicationProperties)
        {
            return "myComponent:" + applicationProperties.getDisplayName();
        }
        
        return "myComponent";
    }
}