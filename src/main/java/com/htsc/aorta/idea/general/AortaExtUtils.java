package com.htsc.aorta.idea.general;

import com.google.common.base.Strings;
import com.htsc.aorta.idea.general.model.IdDomElement;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.util.PsiTreeUtil;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AortaExtUtils {

    public static Set<String> analyseDubboInterfaces(String toBeAnalyseText) {
        Set<String> rSet = new HashSet<>();
        if (Strings.isNullOrEmpty(toBeAnalyseText)) {
            return rSet;
        }
        Pattern regPattern = Pattern.compile("interface=\"(.+?)\"");
        Matcher matcher = regPattern.matcher(toBeAnalyseText);
        while (matcher.find()) {
            rSet.add(matcher.group(1));
        }
        return rSet;
    }

    public static String formatJsonArray(String text) {
        if (StringUtils.isNotEmpty(text)) {
            return text.replace(",", "\n");
        }
        return "";
    }

    public static Optional<PsiMethod> getParentPsiMethodSetByCaret(@NotNull AnActionEvent e) {
        // 我们也可以通过插入符号位置获取到当前位置的 PsiElement
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        CaretModel caretModel = editor.getCaretModel();
        PsiElement psiElement = psiFile.findElementAt(caretModel.getOffset());
        if (psiFile != null && DomUtils.isJavaFile(psiFile)) {
            PsiElement parent = PsiTreeUtil.getParentOfType(psiElement, PsiMethod.class);
            return Optional.ofNullable((PsiMethod) parent);
        }
        if (psiFile != null && DomUtils.isMybatisFile(psiFile)) {
            Optional<IdDomElement> idDomElementOpt = MapperUtils.findParentIdDomElement(psiElement);
            if (idDomElementOpt.isPresent()) {
                return JavaUtils.findMethod(e.getProject(), idDomElementOpt.get());
            }
        }
        return Optional.empty();
    }
}
