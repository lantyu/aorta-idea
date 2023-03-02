package com.htsc.aorta.idea.handler;

public class MethodRef {

    private String qualifiedMethodRef;

    private String classRefString;

    private String file;

    public MethodRef(String qualifiedMethodRef, String classRefString, String file) {
        this.qualifiedMethodRef = qualifiedMethodRef;
        this.classRefString = classRefString;
        this.file = file;
    }

    public String getQualifiedMethodRef() {
        return qualifiedMethodRef;
    }

    public void setQualifiedMethodRef(String qualifiedMethodRef) {
        this.qualifiedMethodRef = qualifiedMethodRef;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getClassRefString() {
        return classRefString;
    }

    public void setClassRefString(String classRefString) {
        this.classRefString = classRefString;
    }
}
