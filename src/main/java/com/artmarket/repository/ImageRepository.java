package com.artmarket.repository;

import com.artmarket.domain.board.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
