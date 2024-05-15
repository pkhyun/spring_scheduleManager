package com.sparta.schedulemanager.repository;

import com.sparta.schedulemanager.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
}
