package com.example.easypan.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UploadResultDto implements Serializable {


    private String fileId;
    private String status;

    public UploadResultDto(String fileId, String status) {
        this.fileId = fileId;
        this.status = status;
    }

    public UploadResultDto() {
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
