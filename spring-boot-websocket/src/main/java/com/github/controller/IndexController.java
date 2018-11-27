/*
 * ............................................. 
 * 
 * 				    _ooOoo_ 
 * 		  	       o8888888o 
 * 	  	  	       88" . "88 
 *                 (| -_- |) 
 *                  O\ = /O 
 *              ____/`---*\____ 
 *               . * \\| |// `. 
 *             / \\||| : |||// \ 
 *           / _||||| -:- |||||- \ 
 *             | | \\\ - /// | | 
 *            | \_| **\---/** | | 
 *           \  .-\__ `-` ___/-. / 
 *            ___`. .* /--.--\ `. . __ 
 *        ."" *< `.___\_<|>_/___.* >*"". 
 *      | | : `- \`.;`\ _ /`;.`/ - ` : | | 
 *         \ \ `-. \_ __\ /__ _/ .-` / / 
 *======`-.____`-.___\_____/___.-`____.-*====== 
 * 
 * ............................................. 
 *              佛祖保佑 永无BUG 
 *
 * 佛曰: 
 * 写字楼里写字间，写字间里程序员； 
 * 程序人员写程序，又拿程序换酒钱。 
 * 酒醒只在网上坐，酒醉还来网下眠； 
 * 酒醉酒醒日复日，网上网下年复年。 
 * 但愿老死电脑间，不愿鞠躬老板前； 
 * 奔驰宝马贵者趣，公交自行程序员。 
 * 别人笑我忒疯癫，我笑自己命太贱； 
 * 不见满街漂亮妹，哪个归得程序员？
 *
 * 北纬30.√  154518484@qq.com
 */
package com.github.controller;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


@Controller
@EnableScheduling
@RequestMapping
public class IndexController {

	@Autowired private SimpMessagingTemplate messagingTemplate;


	@RequestMapping({"", "/", "index"})
	public String index() {
		return "index";
	}

	@MessageMapping("/receive")
	@SendTo("/topic/send")
	public String receive(String message) {
		System.err.println(message);
		return message + ": " + RandomStringUtils.randomAlphabetic(6);
	}

//	@Scheduled(fixedRate = 6 * 1000)
//	@SendTo("/topic/callback")
//	public Object callback() {
//		// 发现消息
//		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		messagingTemplate.convertAndSend("/topic/callback", df.format(new Date()));
//		return "callback";
//	}

	@ResponseBody
	@GetMapping("send")
	public Object send() {
		// 发现消息
		messagingTemplate.convertAndSend("/topic/callback", RandomStringUtils.randomAlphabetic(2));
		return "send success.";
	}

}

