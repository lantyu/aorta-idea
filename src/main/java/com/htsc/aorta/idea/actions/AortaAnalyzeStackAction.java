package com.htsc.aorta.idea.actions;

import com.htsc.aorta.idea.general.AortaExtUtils;
import com.htsc.aorta.idea.handler.MethodRecordHandler;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiCall;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.MethodSignatureBackedByPsiMethod;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Query;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class AortaAnalyzeStackAction extends AnAction {

    private LinkedList<PsiMethod> methods2Scan = new LinkedList<>();

    private Set<PsiMethod> scannedMethods = new HashSet<>();

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Optional<PsiMethod> parentMethodOpt = AortaExtUtils.getParentPsiMethodSetByCaret(e);
        if (parentMethodOpt.isPresent()) {
            addMethods2Scan(parentMethodOpt.get());
        }

        analyseMethod(e.getProject());
    }

    private void markMethodsScanned(PsiMethod method) {
        if (Objects.nonNull(method)) {
            scannedMethods.add(method);
            MethodRecordHandler.getInstance().recordMethod(method);
        }
    }

    private void addMethods2Scan(PsiMethod method) {
        if (Objects.nonNull(method) && !scannedMethods.contains(method)) {
            methods2Scan.add(method);
            PsiMethod[] superMethods = method.findSuperMethods(false);
            for (int i = 0; i < superMethods.length; i++) {
                methods2Scan.add(superMethods[i]);
            }
            List<MethodSignatureBackedByPsiMethod> superMethodSignaturesIncludingStatic = method.findSuperMethodSignaturesIncludingStatic(false);
            for (MethodSignatureBackedByPsiMethod methodSignatureBackedByPsiMethod : superMethodSignaturesIncludingStatic) {
                methods2Scan.add(methodSignatureBackedByPsiMethod.getMethod());
            }
        }
    }

    private void analyseMethod(Project project) {
        while (!methods2Scan.isEmpty()){
            PsiMethod methodScanning = methods2Scan.removeFirst();
            if (scannedMethods.contains(methodScanning)) {
                continue;
            }
            Query<PsiReference> search = ReferencesSearch.search(methodScanning, GlobalSearchScope.projectScope(project), false);
            for (PsiReference psiReference : search.findAll()) {
                System.out.println("开始处理引用：" + psiReference.getCanonicalText());
                PsiCall psiCall = PsiTreeUtil.getParentOfType(psiReference.getElement(), PsiCall.class);
                PsiMethod resolveMethod = psiCall.resolveMethod();
                // 新的引用需要被扫描
                addMethods2Scan(resolveMethod);
            }
            markMethodsScanned(methodScanning);
        }
    }
}
