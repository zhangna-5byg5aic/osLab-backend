package com.yupi.springbootinit.repository;

import com.yupi.springbootinit.model.entity.KnowledgeGraphNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface KnowledgeGraphRepository extends Neo4jRepository<KnowledgeGraphNode, Long> {
    List<KnowledgeGraphNode> findByName(String name);

}
