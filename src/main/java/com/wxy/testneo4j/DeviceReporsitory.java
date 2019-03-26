package com.wxy.testneo4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;

public interface DeviceReporsitory extends Neo4jRepository<Device,Long> {

    @Query("MATCH (d:Device) WHERE d.devAddr = {devAddr} RETURN d")
    Device findByDevAddr(@Param("devAddr") long devAddr);

    @Query ("MATCH (gw:Device) where gw.devAddr={gwAddr} MATCH (node:Device) where node.devAddr={nodeAddr} create (gw)-[:HAS]->(node)")
    void gwAddNode(@Param("gwAddr") long gwAddr,@Param("nodeAddr") long nodeAddr);

    @Query("match (gw:Device)-[:HAS]->(node:Device) where gw.devAddr={gwAddr} return node order by node.devAddr")
    ArrayList<Device> getDeviceList(@Param("gwAddr") long gwAddr);

    @Query("match (gw:Device) where gw.devAddr={gwAddr} match(node:Device) where node.devAddr<{nodeAddr}+{number} and node.devAddr>={nodeAddr} create (gw)-[:HAS]->(node)")
    void addRelationHas(@Param("gwAddr") long gwAddr,@Param("nodeAddr") long nodeAddr,@Param("number") int number);

    @Query("match (n:Device)-[:HAS]->(k:Device) where k.devAddr={nodeAddr} return n")
    Device findGwByNodeAddr(@Param("nodeAddr") long nodeAddr);

}
