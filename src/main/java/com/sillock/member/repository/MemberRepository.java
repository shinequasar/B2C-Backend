package com.sillock.member.repository;

import com.sillock.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByUniqueCode(String uniqueCode);
    Optional<Member> findByUidAndProcider(String uid, String provider);
//    @EntityGraph(attributePaths = "authorities")
//    Optional<Member> findOneWithAuthoritiesByname(String name);
}