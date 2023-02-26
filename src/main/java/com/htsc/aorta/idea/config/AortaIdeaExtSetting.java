package com.htsc.aorta.idea.config;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

@State(name = "aortaIdeaConfig", storages = {@Storage(value = "AortaIdea-Config.xml")})
public class AortaIdeaExtSetting implements PersistentStateComponent<AortaIdeaExtSetting> {

    public Set<String> dubboServices;

    public static AortaIdeaExtSetting getInstance(Project project) {
        return project.getService(AortaIdeaExtSetting.class);
    }

    @Override
    public @Nullable AortaIdeaExtSetting getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull AortaIdeaExtSetting state) {
        this.dubboServices = state.dubboServices;
    }
}
