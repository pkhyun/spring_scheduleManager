package com.sparta.schedulemanager.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "schedule") // schedule 테이블 생성
@NoArgsConstructor
public class Schedule {
}
