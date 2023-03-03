package com.htsc.aorta.idea.actions;

import com.htsc.aorta.idea.general.AortaExtUtils;
import com.htsc.aorta.idea.handler.MethodRecordHandler;
import com.htsc.aorta.idea.handler.MethodRef;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
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

    private LinkedList<PsiMethod> methods2Scan;

    private Set<PsiMethod> scannedMethods;

    private Set<MethodRef> methodRefs;

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        methods2Scan = new LinkedList<>();
        scannedMethods = new HashSet<>();
        methodRefs = new HashSet<>();

        Optional<PsiMethod> parentMethodOpt = AortaExtUtils.getParentPsiMethodSetByCaret(e);
        if (parentMethodOpt.isPresent()) {
            addMethods2Scan(parentMethodOpt.get());
        }

        analyseMethod(e.getProject());
        MethodRecordHandler.getInstance().finishHandler(e.getProject(),parentMethodOpt, methodRefs);
    }

    private void markMethodsScanned(PsiMethod method) {
        if (Objects.nonNull(method)) {
            scannedMethods.add(method);
            MethodRecordHandler.getInstance().recordMethod(method, methodRefs);
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
        while (!methods2Scan.isEmpty()) {
            PsiMethod methodScanning = methods2Scan.removeFirst();
            if (scannedMethods.contains(methodScanning)) {
                continue;
            }
            Query<PsiReference> search = ReferencesSearch.search(methodScanning, GlobalSearchScope.projectScope(project), false);
            for (PsiReference psiReference : search.findAll()) {
                PsiMethod psiMethod = PsiTreeUtil.getParentOfType(psiReference.getElement(), PsiMethod.class);
                // 新的引用需要被扫描
                addMethods2Scan(psiMethod);
            }
            markMethodsScanned(methodScanning);
        }
    }
}
