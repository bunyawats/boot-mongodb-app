package com.ssc.mongo.MongoDBApp.bpm.plugin;

import org.camunda.bpm.engine.impl.form.type.AbstractFormFieldType;
import org.camunda.bpm.engine.variable.value.TypedValue;

public class DoubleFormFieldType extends AbstractFormFieldType {
    @Override
    public String getName() {
        return "double";
    }

    @Override
    public TypedValue convertToFormValue(TypedValue propertyValue) {
        return propertyValue;
    }

    @Override
    public TypedValue convertToModelValue(TypedValue propertyValue) {
        return propertyValue;
    }

    @Override
    public Object convertFormValueToModelValue(Object propertyValue) {
        return propertyValue;
    }

    @Override
    public String convertModelValueToFormValue(Object modelValue) {
        return modelValue.toString();
    }
}
