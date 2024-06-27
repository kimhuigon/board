package com.example.board.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.board.model.Board;
import com.example.board.model.FileInfo;

public interface FileInfoRepository extends JpaRepository<FileInfo, Integer> {
  List<FileInfo> findByBoard(Board board);
}