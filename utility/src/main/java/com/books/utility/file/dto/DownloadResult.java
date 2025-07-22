package com.books.utility.file.dto;

import jakarta.validation.constraints.NotNull;

public class DownloadResult {
    @NotNull
    private byte[] file;
    @NotNull
    private String mimeType;

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
