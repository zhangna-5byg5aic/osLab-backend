package com.yupi.springbootinit.model.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("Entity")
@Data
public class KnowledgeGraphNode {
    @Id
    private Long id;
    private String name;
    private String description;
    private String label;
}
