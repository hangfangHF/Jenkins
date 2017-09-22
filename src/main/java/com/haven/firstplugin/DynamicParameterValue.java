package com.haven.firstplugin;

import hudson.model.StringParameterValue;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.export.Exported;

public class DynamicParameterValue extends StringParameterValue{
    private static final long serialVersionUID = -2922191393128020620L;

    @Exported(visibility = 4)
    public final String dynamicName;
    public final String dynamicValue;

    @DataBoundConstructor
    public DynamicParameterValue(String name, String value, String dynamicName, String dynamicValue) {
        this(name, value,dynamicName,dynamicValue,null);
    }

    public DynamicParameterValue(String name, String value, String description, String dynamicName, String dynamicValue) {
        super(name, value, description);
        this.dynamicName = dynamicName;
        this.dynamicValue = dynamicValue;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
