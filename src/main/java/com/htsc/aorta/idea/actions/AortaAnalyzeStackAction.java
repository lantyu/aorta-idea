package com.htsc.aorta.idea.actions;

import com.htsc.aorta.idea.general.AortaExtUtils;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Query;
import org.jetbrains.annotations.NotNull;

public class AortaAnalyzeStackAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        PsiMethod parentMethod = AortaExtUtils.getParentPsiMethodSetByCaret(e);
        if (parentMethod != null) {
            Query<PsiReference> search = ReferencesSearch.search(parentMethod, GlobalSearchScope.projectScope(e.getProject()), false);
            for (PsiReference psiReference : search.findAll()) {
                System.out.println("开始处理引用：" + psiReference.getCanonicalText());
                PsiCall psiCall = PsiTreeUtil.getParentOfType(psiReference.getElement(), PsiCall.class);
                System.out.println(psiCall.resolveMethod());
            }
        }
    }
}
