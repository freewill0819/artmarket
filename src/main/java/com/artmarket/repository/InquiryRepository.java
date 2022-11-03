package com.artmarket.repository;

import com.artmarket.domain.inquiry.Confirm;
import com.artmarket.domain.inquiry.Inquiry;
import com.artmarket.domain.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    Page<Inquiry> findByUser(User user, Pageable pageable);

    Page<Inquiry> findByConfirm(Confirm confirm, Pageable pageable);
    Page<Inquiry> findByUserAndConfirm(User user, Confirm confirm, Pageable pageable);
}
