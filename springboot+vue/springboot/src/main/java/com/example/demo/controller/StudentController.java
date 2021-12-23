package com.example.demo.controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Student;
import com.example.demo.mapper.StudentMapper;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;


@RestController
@RequestMapping("/student")
public class StudentController {
    @Resource
    StudentMapper studentMapper;
    //新增
    @PostMapping
    public Result<?> save(@RequestBody Student stu){
        if(stu.getPassword()==null){
            stu.setPassword("123456");
        }
        studentMapper.insert(stu);
        return Result.success();
    }

    //查询
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNumber,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search){
        LambdaQueryWrapper<Student> wrapper = Wrappers.<Student>lambdaQuery();
        //判断search不是空，不是空时再查询
        if(StringUtils.isNotBlank(search)){
            wrapper.like(Student::getUsername,search);
        }

        Page<Student> studentPage = studentMapper.selectPage(new Page<>(pageNumber, pageSize),wrapper);
        return Result.success(studentPage);
    }

    //更新
    @PutMapping
    public Result<?> update(@RequestBody Student stu){
        studentMapper.updateById(stu);
        return Result.success();
    }

    //删除
    @DeleteMapping("/{id}")
    public Result<?> update(@PathVariable Long id){
        studentMapper.deleteById(id);
        return Result.success();
    }

    //登陆
    @PostMapping("/login")
    public Result<?> login(@RequestBody Student stu){

        Student s = studentMapper.selectOne(Wrappers.<Student>lambdaQuery().eq(Student::getNumber,stu.getNumber()).eq(Student::getPassword,stu.getPassword()));
        if(s ==null){
            return Result.error("-1","用户名或密码错误");
        }
        return Result.success();
    }

    //注册
    @PostMapping("/register")
    public Result<?> register(@RequestBody Student stu){
        Student s = studentMapper.selectOne(Wrappers.<Student>lambdaQuery().eq(Student::getNumber,stu.getNumber()));
        if(s != null){
            return Result.error("-1","学生账号重复！");
        }
        if(stu.getPassword() == null){
            stu.setPassword("123456");
        }
        studentMapper.insert(stu);
        return Result.success();
    }



}
