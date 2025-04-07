package com.yupi.springbootinit.service;

import com.yupi.springbootinit.constant.RelationType;
import com.yupi.springbootinit.model.dto.knowledgegraph.CategoryDTO;
import com.yupi.springbootinit.model.dto.knowledgegraph.GraphDataDTO;
import com.yupi.springbootinit.model.dto.knowledgegraph.LinkDTO;
import com.yupi.springbootinit.model.dto.knowledgegraph.NodeDTO;
import com.yupi.springbootinit.model.entity.EntityNode;
import com.yupi.springbootinit.model.entity.KnowledgeGraphNode;
import com.yupi.springbootinit.repository.KnowledgeGraphRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
    public GraphDataDTO convertToEChartsFormat(List<KnowledgeGraphNode> knowledgeGraphNodes) {
        GraphDataDTO graphData = new GraphDataDTO();
        List<LinkDTO> links = new ArrayList<>();
        // 用于去重和快速查找的映射
        Map<String, NodeDTO> nodeMap = new HashMap<>();
        Set<String> categorySet = new HashSet<>();
        for (KnowledgeGraphNode kgNode : knowledgeGraphNodes) {
            // 处理主实体节点
            EntityNode entity = kgNode.getEntity();
            NodeDTO mainNode = createNodeDTO(entity);
            nodeMap.putIfAbsent(mainNode.getId(), mainNode);
            categorySet.add(entity.getLabel());
            // 处理相关实体节点
            for (EntityNode relatedEntity : kgNode.getRelatedEntities()) {
                NodeDTO relatedNode = createNodeDTO(relatedEntity);
                nodeMap.putIfAbsent(relatedNode.getId(), relatedNode);
                categorySet.add(relatedEntity.getLabel());
                // 创建关系链接
                LinkDTO link = new LinkDTO();
                link.setSource(mainNode.getId());
                link.setTarget(relatedNode.getId());
                link.setValue(kgNode.getRelationType());
                link.setRelationType(kgNode.getRelationType());
                links.add(link);
            }
        }
        List<NodeDTO> nodes = new ArrayList<>(nodeMap.values());
        graphData.setNodes(nodes);
        graphData.setLinks(links);
        // 使用 Stream API 转换 Set 到 List<CategoryDTO>
        List<CategoryDTO> categories = categorySet.stream()
                .map(category -> {
                    CategoryDTO cat = new CategoryDTO(category);
                    return cat;
                })
                .collect(Collectors.toList());
        graphData.setCategories(categories); // 设置分类

        return graphData;
    }
    public GraphDataDTO convertToEChartsFormat(KnowledgeGraphNode knowledgeGraphNode) {
        GraphDataDTO graphData = new GraphDataDTO();
        List<LinkDTO> links = new ArrayList<>();
        // 用于去重和快速查找的映射
        Map<String, NodeDTO> nodeMap = new HashMap<>();

        // 处理主实体节点
        EntityNode entity = knowledgeGraphNode.getEntity();
        NodeDTO mainNode = createNodeDTO(entity);
        nodeMap.putIfAbsent(mainNode.getId(), mainNode);

        // 处理相关实体节点
        for (EntityNode relatedEntity : knowledgeGraphNode.getRelatedEntities()) {
            NodeDTO relatedNode = createNodeDTO(relatedEntity);
            nodeMap.putIfAbsent(relatedNode.getId(), relatedNode);

            // 创建关系链接
            LinkDTO link = new LinkDTO();
            link.setSource(mainNode.getId());
            link.setTarget(relatedNode.getId());
            link.setValue(knowledgeGraphNode.getRelationType());
            link.setRelationType(knowledgeGraphNode.getRelationType());
            links.add(link);
        }
        List<NodeDTO> nodes = new ArrayList<>(nodeMap.values());
        graphData.setNodes(nodes);
        graphData.setLinks(links);
        graphData.setCategories(new ArrayList<>(List.of(new CategoryDTO(knowledgeGraphNode.getRelationType())))); // 设置分类

        return graphData;
    }
    private NodeDTO createNodeDTO(EntityNode entity) {
        NodeDTO node = new NodeDTO();
        node.setId(entity.getId().toString());
        node.setName(entity.getName());
        node.setCategory(entity.getLabel());
        node.setDescription(entity.getDescription());
        return node;
    }

    // 将 label 转换为分类索引
    private Integer getCategoryIndex(String label) {
        return switch(label) {
            case "基础" -> 0;
            case "进阶" -> 1;
            case "应用" -> 2;
            default -> throw new IllegalArgumentException("未知的标签类型: " + label);
        };
    }


    public List<KnowledgeGraphNode> getKnowledgeGraph(List<String> names) {
        List<KnowledgeGraphNode> result=new ArrayList<>();
        List<String> relationTypes = Arrays.asList(RelationType.RELATED, RelationType.DEPENDENT, RelationType.CONTAIN, RelationType.DEPENDENT);
        for(String name:names)
        {
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
        }
        return result;
    }
    public List<KnowledgeGraphNode> getAllKnowledgeGraph()
    {
        List<EntityNode> allEntityNodes = knowledgeGraphRepository.findAll();
        List<KnowledgeGraphNode> result=new ArrayList<>();
        List<String> allNames = allEntityNodes.stream()
                .map(EntityNode::getName)
                .collect(Collectors.toList());
        return getKnowledgeGraph(allNames);

    }
    public List<EntityNode> getAllEntityNode()
    {
        return knowledgeGraphRepository.findAll();
    }
}
