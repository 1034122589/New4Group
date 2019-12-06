package com.itheima.health.dao;

import com.itheima.health.pojo.Member;

import java.util.List;
import java.util.Map;

public interface MemberDao {
    Member findMemberByTelephone(String telephone);

    void add(Member member);

    Integer findMemberCountByRegTime(String regTime);

    Integer findTodayNewMember(String today);

    Integer findTotalMember();

    Integer findThisNewMember(String monday);

    List<Map> findSexCount();
}
