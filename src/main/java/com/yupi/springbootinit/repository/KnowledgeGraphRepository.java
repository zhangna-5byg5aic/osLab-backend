package com.yupi.springbootinit.repository;

import com.yupi.springbootinit.model.entity.EntityNode;
import com.yupi.springbootinit.model.entity.KnowledgeGraphNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface KnowledgeGraphRepository extends Neo4jRepository<EntityNode, Long> {
    List<EntityNode> findByName(String name);
    // 查询节点及其关联节点
    @Query("MATCH (e:Entity {name: $name})-[r]-(related) WHERE type(r) = $relationshipType \n" +
            "RETURN e AS entity, collect(related) AS relatedEntities\n")
    Optional<KnowledgeGraphNode> findByNameWithRelationship(String name, String relationshipType);

}
