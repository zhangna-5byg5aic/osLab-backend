package com.yupi.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.model.entity.Students;
import com.yupi.springbootinit.service.StudentsService;
import com.yupi.springbootinit.mapper.StudentsMapper;
import org.springframework.stereotype.Service;

/**
* @author 张娜
* @description 针对表【students(学生表)】的数据库操作Service实现
* @createDate 2025-05-04 16:28:29
*/
@Service
public class StudentsServiceImpl extends ServiceImpl<StudentsMapper, Students>
    implements StudentsService{

}




