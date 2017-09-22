package com.haven.firstplugin;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Descriptor;
import hudson.model.Job;
import hudson.model.JobProperty;
import hudson.model.JobPropertyDescriptor;
import hudson.model.ParameterDefinition;
import hudson.model.ParametersDefinitionProperty;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.export.ExportedBean;

import java.util.List;

@ExportedBean(defaultVisibility = 3)
public class CustomizeParametersDefinitionProperty extends ParametersDefinitionProperty{

    public CustomizeParametersDefinitionProperty(List<ParameterDefinition> parameterDefinitions) {
        super(parameterDefinitions);
    }

    public CustomizeParametersDefinitionProperty(ParameterDefinition... parameterDefinitions) {
        super(parameterDefinitions);
    }

    @Extension
    public static class DescriptorImpl extends JobPropertyDescriptor {
        @Override
        public boolean isApplicable(Class<? extends Job> jobType) {
            return AbstractProject.class.isAssignableFrom(jobType);
        }

        @Override
        public JobProperty<?> newInstance(StaplerRequest req, JSONObject formData) throws FormException {
            if(formData.isNullObject()){
                return null;
            }
            JSONObject parameterized = formData.getJSONObject("parameterized");
            if (parameterized.isNullObject()){
                return null;
            }
            List<ParameterDefinition> parameterDefinitions = Descriptor.newInstancesFromHeteroList(req,parameterized,"parameter",ParameterDefinition.all());
            if (parameterized.isEmpty()) {
                return null;
            }
            return new CustomizeParametersDefinitionProperty(parameterDefinitions);
        }

        @Override
        public String getDisplayName() {
            return "CustomizeParametersDefinitionProperty.DisplayName";
        }
    }
    public String getDisplayName() {
        return super.getDisplayName();
    }
    public String getIconFileName() {
        return super.getIconFileName();
    }
    public String getUrlName() {
        return super.getUrlName();
    }
}
