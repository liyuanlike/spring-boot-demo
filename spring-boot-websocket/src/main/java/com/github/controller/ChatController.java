package com.github.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

  @Autowired
  private SimpMessagingTemplate template;
  
  // 注入其它Service

  // 群聊天
//  @MessageMapping("/notice")
//  public void notice(Principal principal, String message) {
//    // 参数说明 principal 当前登录的用户， message 客户端发送过来的内容
//    // principal.getName() 可获得当前用户的username
//
//    // 发送消息给订阅 "/topic/notice" 且在线的用户
//    template.convertAndSend("/topic/notice", message);
//  }

//   点对点聊天
//  @MessageMapping("/chat")
//  public void chat(Principal principal, String message){
//     参数说明 principal 当前登录的用户， message 客户端发送过来的内容（应该至少包含发送对象toUser和消息内容content）
//     principal.getName() 可获得当前用户的username
//
//     发送消息给订阅 "/user/topic/chat" 且用户名为toUser的用户
//    template.convertAndSendToUser(toUser, "/topic/chat", content);
//  }

}

