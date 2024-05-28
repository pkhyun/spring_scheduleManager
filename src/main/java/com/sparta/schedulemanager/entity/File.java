package com.sparta.schedulemanager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "files")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "file", cascade = CascadeType.ALL)
    private Schedule schedule;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_extension")
    private String fileExtension;

    @Column(name = "file_size")
    private int fileSize;

    @Column(name = "file_path")
    private String filePath; // 파일 경로 추가

    public File(String fileName, String fileExtension, Integer fileSize, String filePath) {
        this.fileName = fileName;
        this.fileExtension = fileExtension;
        this.fileSize = fileSize;
        this.filePath = filePath;
    }


}

