package com.htsc.aorta.idea.general;

import com.htsc.aorta.idea.handler.MethodRef;
import com.intellij.psi.PsiMethod;

import java.util.Optional;

public class PsiMethod2MethodRefTranslator {

    private static class SingletonHolder {
        private static final PsiMethod2MethodRefTranslator instance = new PsiMethod2MethodRefTranslator();
    }

    private PsiMethod2MethodRefTranslator() {
    }

    public static final PsiMethod2MethodRefTranslator getInstance() {
        return SingletonHolder.instance;
    }

    public Optional<MethodRef> psiMethod2MethodRef(Optional<PsiMethod> psiMethodOpt) {
        if (psiMethodOpt.isPresent()) {
            PsiMethod psiMethod = psiMethodOpt.get();
            String fileName = psiMethod.getContainingFile().getName();
            if (fileName.endsWith(".class")) {
                return Optional.empty();
            }
            String className = psiMethod.getContainingClass().getQualifiedName();
            MethodRef methodRef = new MethodRef(String.format("%s#%s", Optional.ofNullable(className).orElse("匿名方法"), psiMethod.getName()), Optional.ofNullable(className).orElse("匿名方法"), fileName);
            return Optional.ofNullable(methodRef);
        }
        return Optional.empty();
    }
}
