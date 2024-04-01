package com.example.airankings.demo.web.util;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;


import java.util.Arrays;


public class TongYiQianWenUtil {


    /*
    * 此Java函数的主要功能是利用预定义的消息模板和输入参数（message、tag、count）生成文本，并返回生成的文本内容。
    * 它通过初始化Generation类并设置相关参数来完成这一过程，最终从生成结果中提取并返回第一条消息内容。
    * */
    public  String callWithMessage(String message, String tag, String count)
            throws NoApiKeyException, ApiException, InputRequiredException {
        Generation gen = new Generation();
        Message systemMsg = Message.builder().role(Role.SYSTEM.getValue())
                .content("You are a helpful assistant.").build();
        Message userMsg =
                Message.builder().role(Role.USER.getValue()).content("请按照给定的格式列出有"+message+"专业的"+count+"家以内国内"+tag+"，严格按照每行只输出一条内容，不要输出任何提示词，内容格式如下：\\n1：某某企业\\n2：某某企业\\n3：某某企业").build();
        GenerationParam param = GenerationParam.builder().model("qwen-turbo")
                .messages(Arrays.asList(systemMsg, userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE).topP(0.8).build();
        GenerationResult result = gen.call(param);
        System.out.println(result.getOutput().getChoices().get(0).getMessage().getContent());
        return result.getOutput().getChoices().get(0).getMessage().getContent();
    }
}
