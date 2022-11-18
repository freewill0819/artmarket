package com.artmarket.repository;

import com.artmarket.domain.board.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {

    Optional<File> findById(Long id);

    List<File> findFileByBoardId(Long boardId);
}
