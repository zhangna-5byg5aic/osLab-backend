package com.yupi.springbootinit.service;

import com.yupi.springbootinit.model.entity.KnowledgeGraphNode;
import com.yupi.springbootinit.repository.KnowledgeGraphRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class KnowledgeGraphServiceTest {
    @Autowired
    private KnowledgeGraphService knowledgeGraphService;
    @Test
    void findByName() {
        List<KnowledgeGraphNode> nodes = knowledgeGraphService.findByName("内存管理");
        for (KnowledgeGraphNode node : nodes) {
            System.out.println("名字："+node.getDescription());
        }
        assertNotNull(nodes);
    }
}