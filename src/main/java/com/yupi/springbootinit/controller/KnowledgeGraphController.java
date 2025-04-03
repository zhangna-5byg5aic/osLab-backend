package com.yupi.springbootinit.controller;

import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.model.dto.knowledgegraph.GraphDataDTO;
import com.yupi.springbootinit.model.entity.KnowledgeGraphNode;
import com.yupi.springbootinit.service.KnowledgeGraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/knowledge_graph")
public class KnowledgeGraphController {
    @Autowired
    private KnowledgeGraphService knowledgeGraphService;
    @GetMapping("/get")
    public BaseResponse<GraphDataDTO> getKnowledgeGraphData(String name) {
        // 1. 从数据库或其他数据源获取原始数据
        List<KnowledgeGraphNode> rawData = knowledgeGraphService.getKnowledgeGraph(name);
        // 2. 转换为ECharts格式
        GraphDataDTO graphData = knowledgeGraphService.convertToEChartsFormat(rawData);

        return ResultUtils.success(graphData);
    }

}
