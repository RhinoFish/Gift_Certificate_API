package com.epam.esm.giftcertificate.data;

import com.epam.esm.giftcertificate.model.Gift;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GiftRepository extends JpaRepository<Gift, Long> {


    Page<Gift> findByTags_name(String name, Pageable pageable);
    Page<Gift> findByNameContainingOrDescriptionContaining(String name,String description,Pageable pageable);
}
