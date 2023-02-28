package com.htsc.aorta.idea.general.model;

import com.intellij.util.xml.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author yanglin
 */
public interface Mapper extends DomElement {

    @NotNull
    @SubTagsList({"insert", "update", "delete", "select"})
    public List<IdDomElement> getDaoElements();

    @Required
    @NameValue
    @NotNull
    @Attribute("namespace")
    public GenericAttributeValue<String> getNamespace();
}
