package com.sparta.schedulemanager.repository;

import com.sparta.schedulemanager.entity.File;
import com.sparta.schedulemanager.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
