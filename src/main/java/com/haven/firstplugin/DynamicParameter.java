package com.haven.firstplugin;

import hudson.Extension;
import hudson.model.Hudson;
import hudson.model.Job;
import hudson.model.ParameterDefinition;
import hudson.model.ParameterValue;
import hudson.model.ParametersDefinitionProperty;
import hudson.util.ListBoxModel;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.logging.Logger;


public class DynamicParameter extends ParameterDefinition {

    private static final Logger LOG = Logger.getLogger(DynamicParameter.class.getName());
    private static final long serialVersionUID = 4371122931200558836L;
    public String value = "";
    public String dynamicValue = "";
    public String dynamicName = "";
    public String valueOptions;
    public String dynamicValueOptions;

    @DataBoundConstructor
    public DynamicParameter(String name, String description, String dynamicName, String valueOptions, String dynamicValueOptions) {
        super(name, description);
        this.dynamicName = dynamicName;
        this.valueOptions = valueOptions;
        this.dynamicValueOptions = dynamicValueOptions;
    }

    @Extension
    public static final class DescriptorImpl extends ParameterDescriptor{
        @Override
        public String getDisplayName() {
            return "HangFang Test Dynamic Parameter";
        }

        private DynamicParameter getDynamicParameter(String param){
           String containsJobName = getCurrentDescriptorByNameUrl();
           String jobName = null;
            try {
                jobName = URLDecoder.decode(containsJobName.substring(containsJobName.lastIndexOf("/")+1),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                LOG.warning("Could not find parameter definition instance for parameter " + param + " due to encoding error in job name: " + e.getMessage());
                return null;
            }
            Job<?,?> j = Hudson.getInstance().getItemByFullName(jobName,Job.class)==null?null:Hudson.getInstance().getItemByFullName(jobName,Job.class);
            if(j != null) {
                ParametersDefinitionProperty pdp = j.getProperty(ParametersDefinitionProperty.class);
                List<ParameterDefinition> pds = pdp.getParameterDefinitions();
                for (ParameterDefinition pd : pds){
                    if (this.isInstance(pd) && ((DynamicParameter)pd).getName().equalsIgnoreCase(param)){
                        return (DynamicParameter) pd;
                    }
                }
            }
            LOG.warning("Could not find parameter definition instance for parameter " + param);
            return null;
        }

        public ListBoxModel doFillValueItems(@QueryParameter String name) {
            LOG.finer("Called with param: " + name);
            ListBoxModel m = new ListBoxModel();
            DynamicParameter dp = this.getDynamicParameter(name);
            if(dp != null){
                for(String s : dp.valueOptions.split("\\r?\\n")) {
                    m.add(s) ;
                }
            }
            return m;
        }
        public  ListBoxModel doFillDynamicValueItems(@QueryParameter String name,@QueryParameter String value) {
            ListBoxModel m = new ListBoxModel();
            DynamicParameter dp = this.getDynamicParameter(name);
            if(dp != null){
                for(String s : dp.dynamicValueOptions.split("\\r?\\n")) {
                    if(s.indexOf(value) == 0) {
                        String[] str = s.split(":");
                        m.add(str[1]);
                    }

                }
            }
            return m;
        }
    }
    @Override
    public ParameterValue createValue(StaplerRequest staplerRequest, JSONObject jsonObject) {
        DynamicParameterValue value = staplerRequest.bindJSON(DynamicParameterValue.class,jsonObject);
        return value;
    }

    @Override
    public ParameterValue createValue(StaplerRequest staplerRequest) {
        String[] value = staplerRequest.getParameterValues(getName());
        String[] dynamicValue = staplerRequest.getParameterValues(this.dynamicName);
        LOG.warning(getName() + ": " + value[0] + "\n");
        LOG.warning(this.dynamicName + ":" + dynamicValue[0] + "\n");
        return new DynamicParameterValue(getName(),value[0],this.dynamicName,dynamicValue[0]);
    }
}
