package com.example.airankings.demo.web.controller;

import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.example.airankings.demo.web.config.KeywordRequest;
import com.example.airankings.demo.web.config.Ranking;
import com.example.airankings.demo.web.util.IpUtil;
import com.example.airankings.demo.web.util.TongYiQianWenUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class BasicController {


    String keyword = "";
    String tag = "";
    int displayCount = 10;

    private final ConcurrentHashMap<String, Integer> ipAddressAccessCounter = new ConcurrentHashMap<>();

    /*
    * 此Java函数处理HTTP请求，其功能主要包括：通过IpUtil.getIpAddr(request)获取请求的客户端IP地址并打印；然后返回字符串"index"，
    * 指示应显示名为"index"的视图页面。
    * */
    @RequestMapping("")
    public String html(HttpServletRequest request, ModelMap map) throws Exception {
        String ipAddress = IpUtil.getIpAddr(request);
        System.out.println("ipAddress:" + ipAddress);
        return "index";
    }


    /*
    * 该函数是Spring框架中的一个HTTP POST处理器，用于接收并处理包含关键字请求的JSON数据。
    * 它获取请求的IP地址、解析请求体中的关键字、标签ID和展示次数，并根据标签ID设置对应的标签名称。
    * 最后返回一个HTTP 200状态码及“response”作为响应内容。
    * */
    @PostMapping("keyword")
    public ResponseEntity<String> handleKeyword(HttpServletRequest httpServletRequest, @RequestBody KeywordRequest request) throws Exception {
        String ipAddress = IpUtil.getIpAddr(httpServletRequest);
        keyword = request.getKeyword();
        String tagId = request.getTagId();
        displayCount = request.getDisplayCount();

        if (Objects.equals(tagId, "1")){
            tag = "高校";
        } else if (Objects.equals(tagId, "2")){
            tag = "企业";
        } else if (Objects.equals(tagId, "3")) {
            tag = "科研机构";
        } else {
            tag = tagId;
        }
        return ResponseEntity.ok("response");
    }


    /*
    * 该函数处理GET类型的"ranking"请求，获取客户端IP地址并检查访问频率是否受限。
    * 若未超出访问限制，则获取并返回指定数量的排名数据至前端“ranking”视图；
    * 否则，标记访问受限并在视图中显示相关提示及IP地址信息。
    * */
    @GetMapping("ranking")
    public String ranking(HttpServletRequest httpServletRequest, ModelMap map) throws Exception {
        String ipAddress = IpUtil.getIpAddr(httpServletRequest);
        map.addAttribute("accessLimitReached", false); // 初始化为 false 或者根据实际情况赋值

        if (!limitIpAddressAccess(ipAddress)) {
            map.addAttribute("accessLimitReached", true);
            map.addAttribute("ipAddress", ipAddress);
        } else {
            LinkedList<Ranking> rankings = getRankingList(keyword, tag, displayCount + "");
            if (rankings != null) { // 确保 rankings 不为 null
                map.addAttribute("rankings", rankings);
            } else {
                map.addAttribute("rankings", new LinkedList<>()); // 若获取到的数据为空，也应初始化为一个空集合
            }
        }
        return "ranking";
    }


    /*
    * 此Java函数通过调用TongYiQianWenUtil工具类处理message、tag和count参数，得到一个包含排名信息的答案字符串。
    * 它将答案按行分割，每行再按"："分割后转换为Ranking对象加入链表。若处理过程中出现异常，则返回空链表。最终返回处理后的Ranking对象链表。
    * */
    public LinkedList<Ranking> getRankingList(String message, String tag, String count) {
        LinkedList<Ranking> rankings = new LinkedList<>();
        TongYiQianWenUtil bigModelNewUtil = new TongYiQianWenUtil();
        String answer = null;
        try {
            answer = bigModelNewUtil.callWithMessage(message ,tag, count);
            String[] lines = answer.split("\n"); // 按行分割字符串
            for (String line : lines) {
                String[] parts = line.split("：");
                rankings.add(new Ranking(parts[0].trim(), parts[1].trim()));
            }
        } catch (Exception e) {
            // 当捕获到任何类型的异常时，清空并返回空列表
            rankings.clear();
        }
        return rankings;
    }

    /*
    * 此Java函数用于检查并限制IP地址的访问次数。
    * 若IP地址访问次数已达或超过5次，则阻止访问并返回false；否则，增加访问次数并允许访问，返回true。
    * 如果输入的IP地址为空，则默认返回true。
    * */
    private boolean limitIpAddressAccess(String ipAddress) throws UnknownHostException {
        if (ipAddress != null) {
            Integer currentCount = ipAddressAccessCounter.getOrDefault(ipAddress, 0);
            if (currentCount >= 5) {
                System.out.println("The ipAddress " + ipAddress + " has accessed " + currentCount + " times and has been blocked.");
                return false;
            } else {
                ipAddressAccessCounter.put(ipAddress, currentCount + 1);
                return true;
            }
        }
        return true; // 如果ipAddress为空，一般不应该发生，此处返回true仅作兜底处理
    }
}
