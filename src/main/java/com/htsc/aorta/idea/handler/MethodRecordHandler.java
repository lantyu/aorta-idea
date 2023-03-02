package com.htsc.aorta.idea.handler;

import com.alibaba.fastjson2.JSON;
import com.htsc.aorta.idea.config.AortaIdeaExtSettingHolder;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiMethod;
import org.apache.commons.collections.CollectionUtils;

import java.awt.datatransfer.StringSelection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class MethodRecordHandler {

    private static class SingletonHolder {
        private static final MethodRecordHandler INSTANCE = new MethodRecordHandler();
    }

    private MethodRecordHandler() {
    }

    public static final MethodRecordHandler getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void recordMethod(PsiMethod method, Set<MethodRef> container) {
        String fileName = method.getContainingFile().getName();
        if (fileName.endsWith(".class")) {
            return;
        }
        String className = method.getContainingClass().getQualifiedName();
        MethodRef methodRef = new MethodRef(String.format("%s#%s", Optional.ofNullable(className).orElse("匿名方法"), method.getName()), Optional.ofNullable(className).orElse("匿名方法"), fileName);
        if (null != container) {
            container.add(methodRef);
        }
        System.out.println("scanning ====》" + JSON.toJSONString(methodRef));
    }

    public void finishHandler(Project project, Set<MethodRef> methodRefs) {
        Set<String> dubboServicesAll = AortaIdeaExtSettingHolder.getInstance(project).getDubboServices();
        AnalyzeResult analyzeResult = new AnalyzeResult();
        analyzeResult.setMethodRefs(methodRefs);
        if (CollectionUtils.isNotEmpty(dubboServicesAll)) {
            analyzeResult.setDubboMethods(methodRefs.stream().filter(e -> dubboServicesAll.contains(Optional.ofNullable(e.getClassRefString()).orElse(""))).collect(Collectors.toSet()));
        }
        CopyPasteManager.getInstance().setContents(new StringSelection(JSON.toJSONString(analyzeResult)));
        Notifications.Bus.notify(new Notification("Print", "Aorta-Idea插件", "已扫描完成（扫描结果已置入剪贴板，JSON结构）", NotificationType.INFORMATION), project);
    }
}
