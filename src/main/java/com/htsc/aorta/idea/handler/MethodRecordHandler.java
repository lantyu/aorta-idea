package com.htsc.aorta.idea.handler;

import com.intellij.psi.PsiMethod;

import java.util.logging.Logger;

public class MethodRecordHandler {

    private static class SingletonHolder {
        private static final MethodRecordHandler INSTANCE = new MethodRecordHandler();
    }
    private MethodRecordHandler (){}
    public static final MethodRecordHandler getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void recordMethod(PsiMethod method) {
        System.out.println("已完成扫描====》" + method.getContainingClass().getQualifiedName());
    }
}
