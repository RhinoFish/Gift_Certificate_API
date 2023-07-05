package com.epam.esm.giftcertificate.data;

import com.epam.esm.giftcertificate.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository <Tag,Long>{

    @Override
    <S extends Tag> S save(S entity);
}
