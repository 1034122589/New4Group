package com.itheima.health.service;

import com.itheima.health.pojo.Member;

import java.util.List;
import java.util.Map;

public interface MemberService {

    Member findMemberByTelephone(String telephone);

    void add(Member member);

    List<Integer> findMemberCountByRegTime(List<String> months);

    List<Map> findSexCount();
}
