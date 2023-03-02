package com.htsc.aorta.idea.general;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Optional;


/**
 * @author yanglin
 */
public final class MapperUtils {

    private MapperUtils() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    public static Optional<XmlTag> findParentMybatisXmlTag(@Nullable PsiElement element) {
        if (null == element) {
            return Optional.empty();
        }
        if (isElementWithinMybatis(element)) {
            return Optional.of((XmlTag) element);
        }
        return getParentOfXmlTag(element);
    }

    @NotNull
    public static Optional<XmlTag> getParentOfXmlTag(@Nullable PsiElement element) {
        XmlTag tag = PsiTreeUtil.getParentOfType(element, XmlTag.class, false);
        while (tag != null) {
            if(isElementWithinMybatis(tag)){
                return Optional.ofNullable(tag);
            }

            tag = tag.getParentTag();
        }
        return Optional.empty();
    }

    public static boolean isElementWithinMybatis(@NotNull PsiElement element) {
        if (null != element && element instanceof XmlTag) {
            return Arrays.asList("select", "update", "insert", "delete").contains(((XmlTag) element).getName().toLowerCase());
        }
        return false;
    }

    @NotNull
    @NonNls
    public static String getNamespace(@NotNull XmlTag element) {
        String namespace = ((XmlFile) element.getContainingFile()).getRootTag().getAttributeValue("namespace");
        return namespace;
    }


    public static String getId(@NotNull XmlTag xmlTag) {
        return xmlTag.getAttributeValue("id");
    }

}
