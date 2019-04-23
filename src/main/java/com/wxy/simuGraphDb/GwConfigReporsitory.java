package com.wxy.simuGraphDb;

import com.wxy.simuGraphDb.GwConfig;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

@EnableNeo4jRepositories
public interface GwConfigReporsitory extends Neo4jRepository <GwConfig,Long>{
    //@Query("MATCH (d:gwconfig) WHERE d.devAddr = {devAddr} RETURN d")
    @Query("match(c:GwConfig) where c.gwAddr={devAddr} return c")
    GwConfig findByGwaddr(@Param("devAddr") long devAddr);
}
