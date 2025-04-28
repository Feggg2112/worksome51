package com.easylive.admin.controller;


import com.easylive.entity.vo.ResponseVO;
import com.wf.captcha.ArithmeticCaptcha;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/category")
public class CategoryController extends ABaseController{

    @RequestMapping("/loadDataList")
    public ResponseVO checkCode() {

        return getSuccessResponseVO(null);
    }

}
