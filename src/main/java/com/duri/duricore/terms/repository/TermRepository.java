package com.duri.duricore.terms.repository;

import com.duri.duricore.terms.entity.Term;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermRepository extends JpaRepository<Term, Long> {

    List<Term> findAllByActiveTrue();
}
