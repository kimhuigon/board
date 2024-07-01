package com.example.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.example.board.model.Board;
import com.example.board.model.Comment;
import com.example.board.model.FileInfo;
import com.example.board.model.User;
import com.example.board.repository.BoardRepository;
import com.example.board.repository.CommentRepository;
import com.example.board.repository.FileInfoRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class BoardController {
	@Autowired BoardRepository boardRepository;

	@Autowired FileInfoRepository fileInfoRepository;

	@Autowired HttpSession session;

	@Autowired CommentRepository commentRepository;

	@GetMapping("/board/delete/{id}")
	public String boardDelete(@PathVariable("id") long id) {
		Board board = new Board();
		board.setId(id);

		List<FileInfo> list = fileInfoRepository.findByBoard(board);
		for(FileInfo fileInfo : list) {
			fileInfoRepository.delete(fileInfo);
		}

		boardRepository.deleteById(id);
		return "redirect:/board/list";
	}

	@GetMapping("/board/update/{id}")
	public String boardUpdate(Model model, @PathVariable("id") long id) {
		User user = (User)session.getAttribute("user_info");
		String email = user.getEmail();

		Optional<Board> data = boardRepository.findById(id);
		Board board = data.get();
		String userId = board.getUserId();
		if(!userId.equals(email)) { // 문자열 비교는 항상 equals()
			return "redirect:/board/" + id;
		}


		model.addAttribute("board", board);
		return "board/update";
	}

	@PostMapping("/board/update/{id}")
	public String boardUpdate(
			@ModelAttribute Board board, @PathVariable("id") long id) {
		User user = (User) session.getAttribute("user_info");
		if (user == null) {
			return "redirect:/signin";
		}
		String userId = user.getEmail();
		board.setUserId(userId);
		board.setId(id);
		boardRepository.save(board);
		return "redirect:/board/" + id;
	}

	@GetMapping("/board/{id}")
	public String boardView(Model model, @PathVariable("id") long id) {
		Optional<Board> data = boardRepository.findById(id);
		Board board = data.get();
		model.addAttribute("board", board);
		return "board/view";
	}

	@GetMapping("/board/list")
	public String boardList(Model model,
	@RequestParam(value = "page", defaultValue = "1") int page,
	@RequestParam(value = "search", required = false) String search
	) {
		// Sort sort = Sort.by(Sort.Direction.DESC, "id");
		// List<Board> list = boardRepository.findAll(sort);

		Sort sort = Sort.by(Order.desc("id"));
		Pageable pageable = PageRequest.of(page - 1, 10, sort);

		Page<Board> p = null;
		List<Board> list = null;

		if(search == null) {
			p = boardRepository.findAll(pageable);
		} else {
			p = boardRepository.findByTitleContainingOrContentContaining(search, search, pageable);
		}
		
		list = p.getContent();

		int startPage = (page - 1) /10 * 10 + 1;
		int endPage = startPage + 9;
		int total = p.getTotalPages();
		if(endPage > total) {
			endPage = total;
		}

		model.addAttribute("list", list);
		// model.addAttribute("board", "active");
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		model.addAttribute("total", total);
		return "board/list";
	}

	@GetMapping("/board/write")
	public String boardWrite() {
		return "board/write";
	}

	@PostMapping("/board/write")
	public String boardWrite(@ModelAttribute Board board, @RequestParam("file") MultipartFile mFile) {
		String fileName = mFile.getOriginalFilename();
		System.out.println(fileName);
		try {
			mFile.transferTo(new File("c:/files/" + fileName));
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		User user = (User) session.getAttribute("user_info");
		String userId = user.getEmail();
		board.setUserId(userId);
		Board saveBoard = boardRepository.save(board);

		FileInfo fileInfo = new FileInfo();
		fileInfo.setBoard(saveBoard);
		fileInfo.setOriginalName(fileName);
		fileInfo.setSaveName(fileName);
		fileInfoRepository.save(fileInfo);

		return "redirect:/board/list";
	}

	@PostMapping("/comment/write")
	public String commentWrite(@ModelAttribute Comment comment, @RequestParam("boardId") long id) {
		Board board = new Board();
		board.setId(id);
		comment.setBoard(board);

		commentRepository.save(comment);
		return "redirect:/board/" + id;
	}
	
}