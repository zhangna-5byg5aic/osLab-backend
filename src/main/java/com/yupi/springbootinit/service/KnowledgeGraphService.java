package com.yupi.springbootinit.service;

import com.yupi.springbootinit.constant.RelationType;
import com.yupi.springbootinit.model.entity.EntityNode;
import com.yupi.springbootinit.model.entity.KnowledgeGraphNode;
import com.yupi.springbootinit.repository.KnowledgeGraphRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class KnowledgeGraphService {
    @Autowired
    private KnowledgeGraphRepository knowledgeGraphRepository;
    public EntityNode saveKnowledgeGraphNode(EntityNode node) {
        return knowledgeGraphRepository.save(node);
    }
    public EntityNode findById(Long id) {
        return knowledgeGraphRepository.findById(id).orElse(null);
    }
    public List<EntityNode> findByName(String name) {
        return knowledgeGraphRepository.findByName(name);
    }
    public List<KnowledgeGraphNode> getKnowledgeGraph(String name) {
        List<KnowledgeGraphNode> result=new ArrayList<>();
        List<String> relationTypes = Arrays.asList(RelationType.RELATED, RelationType.DEPENDENT, RelationType.CONTAIN, RelationType.DEPENDENT);

        for (String relationType : relationTypes) {
            Optional<KnowledgeGraphNode> nodeOptional = knowledgeGraphRepository.findByNameWithRelationship(name, relationType);
            if (nodeOptional.isPresent()) {
                KnowledgeGraphNode node = nodeOptional.get();
                node.setRelationType(relationType); // 设置关系类型
                result.add(node);
            } else {
                System.out.println("No Data for relation type: " + relationType);
            }
        }

        return result;
    }
    public KnowledgeGraphNode findByNameWithRelationship(String name,String relationshipType)
    {
        Optional<KnowledgeGraphNode> result = knowledgeGraphRepository.findByNameWithRelationship(name, relationshipType);
        if (result.isPresent()) {
            KnowledgeGraphNode node = result.get();
            node.setRelationType(relationshipType);
            // 可以在这里做进一步的处理
            return node;
        } else {
            System.out.println("No Data");
            return null;
        }
    }

}
