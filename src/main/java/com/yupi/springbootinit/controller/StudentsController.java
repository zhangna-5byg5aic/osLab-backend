package com.yupi.springbootinit.controller;

import com.yupi.springbootinit.annotation.AuthCheck;
import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.constant.UserConstant;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.mapper.StudentsMapper;
import com.yupi.springbootinit.model.dto.student.StudentAddRequest;
import com.yupi.springbootinit.model.entity.Students;
import com.yupi.springbootinit.model.entity.User;
import com.yupi.springbootinit.model.vo.StudentVO;
import com.yupi.springbootinit.model.vo.UserVO;
import com.yupi.springbootinit.service.StudentsService;
import com.yupi.springbootinit.service.UserService;
import com.yupi.springbootinit.utils.PasswordGenerator;
import com.yupi.springbootinit.utils.PinyinConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.yupi.springbootinit.service.impl.UserServiceImpl.SALT;


/**
 * 学生管理
 */
@RestController
@RequestMapping("/student")
@Slf4j
public class StudentsController {
    @Resource
    private StudentsService studentsService;
    @Resource
    private UserService userService;
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<StudentVO> addStudents(@RequestBody StudentAddRequest studentAddRequest)
    {
        String account = PinyinConverter.convertToPinyin(studentAddRequest.getName());
        String defaultPassword = PasswordGenerator.generatePassword();
        User user = new User();
        user.setUserAccount(account);
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + defaultPassword).getBytes());
        user.setUserPassword(encryptPassword);
        user.setUserName(studentAddRequest.getName());
        user.setUserRole(UserConstant.DEFAULT_ROLE);
        boolean userSave = userService.save(user);
        if(!userSave)
        {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        Students student = new Students();
        BeanUtils.copyProperties(studentAddRequest,student);
        student.setUserId(user.getId());
        try{
            studentsService.save(student);
            StudentVO studentVO = new StudentVO();
            BeanUtils.copyProperties(student,studentVO);
            studentVO.setUserAccount(account);
            studentVO.setUserPassword(defaultPassword);
            studentVO.setUserId(user.getId());
            studentVO.setUserName(user.getUserName());
            return ResultUtils.success(studentVO);
        }catch (DuplicateKeyException e)
        {
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"学号重复");
        }
    }
}
