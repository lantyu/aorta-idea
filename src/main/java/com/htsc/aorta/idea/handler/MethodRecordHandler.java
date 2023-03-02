package com.htsc.aorta.idea.handler;

import com.alibaba.fastjson2.JSON;
import com.htsc.aorta.idea.config.AortaExtSettingConfigurationUI;
import com.htsc.aorta.idea.config.AortaIdeaExtSettingHolder;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiMethod;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.awt.datatransfer.StringSelection;
import java.util.HashSet;
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

    public void recordMethod(PsiMethod method, Set<MethodRefResult> container) {
        String fileName = method.getContainingFile().getName();
        if (fileName.endsWith(".class")) {
            return;
        }
        String className = method.getContainingClass().getQualifiedName();
        MethodRefResult methodRefResult = new MethodRefResult(String.format("%s#%s", Optional.ofNullable(className).orElse("匿名方法"), method.getName()), Optional.ofNullable(className).orElse("匿名方法"), fileName);
        if (null != container) {
            container.add(methodRefResult);
        }
        System.out.println("scanning ====》" + JSON.toJSONString(methodRefResult));
    }

    public void finishHandler(Project project, Set<MethodRefResult> methodRefResults) {
        Set<String> dubboServices = AortaIdeaExtSettingHolder.getInstance(project).getDubboServices();
        if (CollectionUtils.isNotEmpty(dubboServices)) {
            methodRefResults = methodRefResults.stream().filter(e -> dubboServices.contains(Optional.ofNullable(e.getClassRefString()).orElse(""))).collect(Collectors.toSet());
        }
        CopyPasteManager.getInstance().setContents(new StringSelection(JSON.toJSONString(methodRefResults)));
        Notifications.Bus.notify(new Notification("Print", "Aorta-Idea插件", "已扫描完成（扫描结果已置入剪贴板，JSON结构）", NotificationType.INFORMATION), project);
    }
}
