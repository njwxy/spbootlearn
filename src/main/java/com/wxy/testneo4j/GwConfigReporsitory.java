package com.wxy.testneo4j;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

public interface GwConfigReporsitory extends Neo4jRepository <GwConfig,Long>{
    //@Query("MATCH (d:gwconfig) WHERE d.devAddr = {devAddr} RETURN d")
    @Query("match(c:GwConfig) where c.gwAddr={devAddr} return c")
    GwConfig findByGwaddr(@Param("devAddr") long devAddr);
}
