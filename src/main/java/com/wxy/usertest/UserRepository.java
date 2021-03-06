package com.wxy.usertest;

import com.wxy.usertest.User;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface UserRepository extends Neo4jRepository<User,Long> {
    @Query("MATCH (n:User) RETURN n ")
    List<User> getUserNodeList();

}
