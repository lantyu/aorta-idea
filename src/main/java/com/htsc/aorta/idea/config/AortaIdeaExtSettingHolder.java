package com.htsc.aorta.idea.config;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@State(name = "aortaIdeaConfig", storages = {@Storage(value = "AortaIdea-Config.xml")})
public class AortaIdeaExtSettingHolder implements PersistentStateComponent<AortaIdeaExtSettingHolder> {

    private Set<String> dubboRefFiles;

    private Set<String> dubboServices;

    public static AortaIdeaExtSettingHolder getInstance(Project project) {
        AortaIdeaExtSettingHolder currentSetting = project.getService(AortaIdeaExtSettingHolder.class);
        if (null == currentSetting.dubboRefFiles) {
            currentSetting.dubboRefFiles = new HashSet<>();
        }
        return currentSetting;
    }

    public void clearSettings() {
        this.dubboRefFiles.clear();
        this.dubboServices.clear();
    }

    public Set<String> getDubboRefFiles() {
        return dubboRefFiles;
    }

    public void setDubboRefFiles(Set<String> dubboRefFiles) {
        this.dubboRefFiles = dubboRefFiles;
    }

    public Set<String> getDubboServices() {
        return dubboServices;
    }

    public void setDubboServices(Set<String> dubboServices) {
        this.dubboServices = dubboServices;
    }

    @Override
    public @Nullable AortaIdeaExtSettingHolder getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull AortaIdeaExtSettingHolder state) {
        this.dubboRefFiles = state.dubboRefFiles;
        this.dubboServices = state.dubboServices;
    }
}
