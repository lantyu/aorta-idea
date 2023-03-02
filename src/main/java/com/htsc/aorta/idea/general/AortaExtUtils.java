package com.htsc.aorta.idea.general;

import com.google.common.base.Strings;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlTag;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AortaExtUtils {

    public static Set<String> analyseDubboInterfaces(Set<String> dubboRefFilesPaths) {
        Set<String> rSet = new HashSet<>();
        if (CollectionUtils.isNotEmpty(dubboRefFilesPaths)) {
            for (String dubboFilePath : dubboRefFilesPaths) {
                File dubboRefFile = new File(dubboFilePath);
                if (dubboRefFile.exists()) {
                    rSet.addAll(getAllDubboInterface(dubboRefFile));
                }
            }
        }
        return rSet;
    }

    private static Set<String> getAllDubboInterface(File file) {
        Set<String> rSet = new HashSet<>();
        //java 8中这样写也可以
        try {
            try (BufferedReader br = Files.newBufferedReader(Paths.get(file.getAbsolutePath()))){
                String line;
                while ((line = br.readLine()) != null) {
                    rSet.addAll(extractDubboInterfaceFromDubboStr(line));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return rSet;
    }


    private static Set<String> extractDubboInterfaceFromDubboStr(String toBeAnalyseText) {
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

    public static Set<String> analyseDubboFiles(String toBeAnalyseText) {
        Set<String> dubboFiles = new HashSet<>();
        if (StringUtils.isNotEmpty(toBeAnalyseText)) {
            dubboFiles = new HashSet<>(Arrays.asList(toBeAnalyseText.split(",")));
        }
        return dubboFiles;
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
            Optional<XmlTag> xmlTagOpt = MapperUtils.findParentMybatisXmlTag(psiElement);
            if (xmlTagOpt.isPresent()) {
                return JavaUtils.findMethod(e.getProject(), xmlTagOpt.get());
            }
        }
        return Optional.empty();
    }
}
