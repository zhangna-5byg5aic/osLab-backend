package com.yupi.springbootinit.service;

import com.yupi.springbootinit.model.entity.KnowledgeGraphNode;
import com.yupi.springbootinit.repository.KnowledgeGraphRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KnowledgeGraphService {
    @Autowired
    private KnowledgeGraphRepository knowledgeGraphRepository;
    public KnowledgeGraphNode saveKnowledgeGraphNode(KnowledgeGraphNode node) {
        return knowledgeGraphRepository.save(node);
    }
    public KnowledgeGraphNode findById(Long id) {
        return knowledgeGraphRepository.findById(id).orElse(null);
    }
    public List<KnowledgeGraphNode> findByName(String name) {
        return knowledgeGraphRepository.findByName(name);
    }
}
