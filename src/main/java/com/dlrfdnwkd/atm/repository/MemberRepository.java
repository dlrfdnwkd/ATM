package com.dlrfdnwkd.atm.repository;

import com.dlrfdnwkd.atm.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findById(final String id);


}
