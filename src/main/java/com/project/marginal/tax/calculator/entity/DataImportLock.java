package com.project.marginal.tax.calculator.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "data_import_lock")
public class DataImportLock {
  @Id
  private Integer id;

  @Column(name = "completed_at")
  private OffsetDateTime completedAt;
}

