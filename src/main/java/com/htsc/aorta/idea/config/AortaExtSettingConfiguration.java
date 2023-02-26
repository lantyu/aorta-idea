package com.htsc.aorta.idea.config;

import com.alibaba.fastjson2.JSON;
import com.htsc.aorta.idea.general.AortaExtUtils;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.ui.JBColor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Set;

public class AortaExtSettingConfiguration implements Configurable {

    private final String headHint = "您可直接使用dubbo reference配置覆盖下方配置：\n";
    private final String nullHint = "请复制任意dubbo-ref xml的文本内容";
    private JComponent component;

    private JTextArea dubboTexts;

    private Project project;

    private String dubboServiceStr;

    public AortaExtSettingConfiguration(Project project) {
        this.component = new JPanel();
        this.dubboTexts = new JTextArea(30, 70);
        this.project = project;

        this.dubboTexts.addFocusListener(new TextAreaListener(this.dubboTexts, nullHint));

        if (CollectionUtils.isNotEmpty(AortaIdeaExtSetting.getInstance(project).dubboServices)) {
            dubboServiceStr = headHint + AortaExtUtils.formatJsonArray(JSON.toJSONString(AortaIdeaExtSetting.getInstance(project).dubboServices));
            this.dubboTexts.setText(dubboServiceStr);
        } else {
            this.dubboTexts.setText(nullHint);
            this.dubboTexts.setForeground(JBColor.GRAY);
        }
        this.component.add(new JScrollPane().add(this.dubboTexts));
    }

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "AortaIdeaExt";
    }

    @Override
    public @Nullable JComponent createComponent() {
        return component;
    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public void apply() {
        if (StringUtils.equalsIgnoreCase(nullHint, this.dubboTexts.getText())) {
            return;
        }
        Set<String> dubboServices = AortaExtUtils.analyseDubboInterfaces(this.dubboTexts.getText());
        if (CollectionUtils.isNotEmpty(dubboServices)) {
            AortaIdeaExtSetting.getInstance(this.project).dubboServices = dubboServices;
            this.dubboTexts.setText(AortaExtUtils.formatJsonArray(headHint + JSON.toJSONString(dubboServices)));
        }
    }

    static class TextAreaListener implements FocusListener {

        private final String defaultHint;
        private final JTextArea textArea;

        public TextAreaListener(JTextArea textArea, String defaultHint) {
            this.defaultHint = defaultHint;
            this.textArea = textArea;
        }

        @Override
        public void focusGained(FocusEvent e) {
            // 清空提示语，设置为黑色字体
            if (textArea.getText().equals(defaultHint)) {
                textArea.setText("");
                textArea.setForeground(JBColor.BLACK);
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            // 如果内容为空，设置提示语
            if (textArea.getText().equals("")) {
                textArea.setText(defaultHint);
                textArea.setForeground(JBColor.GRAY);
            }
        }
    }
}
