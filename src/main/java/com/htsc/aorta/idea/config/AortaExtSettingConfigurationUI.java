package com.htsc.aorta.idea.config;

import com.htsc.aorta.idea.general.AortaExtUtils;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.JBColor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.util.Set;

public class AortaExtSettingConfigurationUI implements Configurable {

    private JComponent component;

    private JButton fileChooserBtn;

    private JTextArea dubboRefFiles;

    private Project project;

    private String dubboFilesStr;

    public AortaExtSettingConfigurationUI(Project project) {
        this.component = new JPanel();
        this.component.setLayout(new BoxLayout(this.component, BoxLayout.Y_AXIS));
        this.fileChooserBtn = new JButton("选择Dubbo provider Ref xml配置文件");
        this.dubboRefFiles = new JTextArea(15,50);
        this.dubboRefFiles.setLineWrap(true);

        this.project = project;

        fileChooserBtn.addActionListener(e -> {
            // Create a file chooser with current user directory
            FileChooserDescriptor descriptor = new FileChooserDescriptor(true, false, false, false, false, true);
            descriptor.setTitle("请选择dubbo-ref文件（可多选）");
            descriptor.setHideIgnored(true);
            VirtualFile[] virtualFiles = FileChooser.chooseFiles(descriptor, project, null);
            for (VirtualFile virtualFile : virtualFiles) {
                appendDubboFile2JText(virtualFile);
            }
        });

        if (CollectionUtils.isNotEmpty(AortaIdeaExtSettingHolder.getInstance(project).getDubboRefFiles())) {
            dubboFilesStr = StringUtils.join(AortaIdeaExtSettingHolder.getInstance(project).getDubboRefFiles(), ",");
            this.dubboRefFiles.setText(dubboFilesStr);
        }

        this.component.add(this.dubboRefFiles);
        this.component.add(this.fileChooserBtn);
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
        if (StringUtils.isBlank(this.dubboRefFiles.getText())) {
            AortaIdeaExtSettingHolder.getInstance(this.project).clearSettings();
            return;
        }

        Set<String> dubboRefFiles = AortaExtUtils.analyseDubboFiles(this.dubboRefFiles.getText());
        if (CollectionUtils.isNotEmpty(dubboRefFiles)) {
            AortaIdeaExtSettingHolder aortaIdeaExtSettingHolder = AortaIdeaExtSettingHolder.getInstance(this.project);
            aortaIdeaExtSettingHolder.setDubboRefFiles(dubboRefFiles);
            aortaIdeaExtSettingHolder.setDubboServices(AortaExtUtils.analyseDubboInterfaces(dubboRefFiles));
        }
        this.dubboRefFiles.setText(StringUtils.join(AortaIdeaExtSettingHolder.getInstance(project).getDubboRefFiles(), ","));
    }

    private void appendDubboFile2JText(VirtualFile dubboRefFile) {
        if (null != dubboRefFile) {
            this.dubboRefFiles.append("," + dubboRefFile.getCanonicalPath());
            String jtextStr = this.dubboRefFiles.getText();
            if (jtextStr.startsWith(",")) {
                this.dubboRefFiles.setText(jtextStr.substring(1));
            }
        }
    }

}
