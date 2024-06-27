package com.example.board.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Comment {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer id;
  String text;
  LocalDateTime createDate;

  @ManyToOne
  Board board;

  // Date  LocalDateTime
  
}
