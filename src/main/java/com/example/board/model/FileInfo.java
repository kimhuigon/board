package com.example.board.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class FileInfo {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer id;

  String originalName;

  String saveName;

  @ManyToOne
  Board board;
}
