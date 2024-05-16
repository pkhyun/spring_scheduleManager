package com.sparta.schedulemanager.repository;

import com.sparta.schedulemanager.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    List<Schedule> findAllByOrderByModifiedAtDesc();
}
