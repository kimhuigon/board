package com.example.board.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString(exclude = {"fileInfos", "comments"})
public class Board {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String title;
	private String content;
	private String userId;

	@OneToMany(mappedBy = "board", orphanRemoval = true, cascade = CascadeType.REMOVE)
	List<FileInfo>fileInfos = new ArrayList<>();

	@OneToMany(mappedBy = "board",orphanRemoval = true, cascade = CascadeType.REMOVE)
	List<Comment> comments = new ArrayList<>();
}