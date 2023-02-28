package com.htsc.aorta.idea.general.model;

import com.intellij.util.xml.*;

/**
 * @author yanglin
 */
public interface IdDomElement extends DomElement {

    @Required
    @NameValue
    @Attribute("id")
    public GenericAttributeValue<String> getId();

    public void setValue(String content);
}
