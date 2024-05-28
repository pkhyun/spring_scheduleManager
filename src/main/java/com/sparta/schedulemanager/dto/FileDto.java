package com.sparta.schedulemanager.dto;


import com.sparta.schedulemanager.entity.File;
import lombok.Getter;

@Getter
public class FileDto {
    private String fileName;
    private String fileExtension;
    private int fileSize;
    private String filePath;

    public FileDto(File file) {
        this.fileName = file.getFileName();
        this.fileExtension = file.getFileExtension();
        this.fileSize = file.getFileSize();
        this.filePath = file.getFilePath();
    }
}
