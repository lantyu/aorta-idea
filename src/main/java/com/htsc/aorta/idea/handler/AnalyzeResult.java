package com.htsc.aorta.idea.handler;

import java.util.Set;

public class AnalyzeResult {

    private Set<MethodRef> methodRefs;

    private Set<MethodRef> dubboMethods;

    public Set<MethodRef> getMethodRefs() {
        return methodRefs;
    }

    public void setMethodRefs(Set<MethodRef> methodRefs) {
        this.methodRefs = methodRefs;
    }

    public Set<MethodRef> getDubboMethods() {
        return dubboMethods;
    }

    public void setDubboMethods(Set<MethodRef> dubboMethods) {
        this.dubboMethods = dubboMethods;
    }
}
