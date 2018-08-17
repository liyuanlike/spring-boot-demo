package com.github.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class MailService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource private JavaMailSender mailSender;

	@Value("${spring.mail.username}")
	private String from;

	/**
	 * 发送文本邮件
	 */
	public void sendSimpleMail(String to, String subject, String content) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(content);

		try {
			mailSender.send(message);
			logger.info("简单邮件已经发送。");
		} catch (Exception e) {
			logger.error("发送简单邮件时发生异常！", e);
		}

	}

	public void sendMail(String content) {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = null;
		try {
			helper = new MimeMessageHelper(message, true);

			helper.setFrom("yaokangming@chaoxing.com");
			helper.setFrom(new InternetAddress("yaokangming@chaoxing.com", "成都研发中心")); // 收件方显示的名称设置
			helper.setTo(new InternetAddress("yangchengliang@chaoxing.com", "杨成亮"));
			helper.setCc(new InternetAddress[]{
					new InternetAddress("herui@chaoxing.com", "何锐")
			});
			helper.setSubject("环球中心公网IP");
			helper.setText(content);

			mailSender.send(message);
			logger.info("Send mail success...");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送html邮件
	 */
	public void sendHtmlMail(String to, String subject, String content) {
		MimeMessage message = mailSender.createMimeMessage();

		try {
			//true表示需要创建一个multipart message
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content, true);

			mailSender.send(message);
			logger.info("html邮件发送成功");
		} catch (MessagingException e) {
			logger.error("发送html邮件时发生异常！", e);
		}
	}


	/**
	 * 发送带附件的邮件
	 */
	public void sendAttachmentsMail(String to, String subject, String content, String filePath) {
		MimeMessage message = mailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content, true);

			FileSystemResource file = new FileSystemResource(new File(filePath));
			String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
			helper.addAttachment(fileName, file);
			//helper.addAttachment("test"+fileName, file);

			mailSender.send(message);
			logger.info("带附件的邮件已经发送。");
		} catch (MessagingException e) {
			logger.error("发送带附件的邮件时发生异常！", e);
		}
	}


	/**
	 * 发送正文中有静态资源（图片）的邮件
	 * 嵌入图片<img src='cid:head'/>，其中cid:是固定的写法，而aaa是一个contentId
	 */
	public void sendInlineResourceMail(String to, String subject, String content, String rscPath, String rscId) {
		MimeMessage message = mailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content, true);

			FileSystemResource res = new FileSystemResource(new File(rscPath));
			helper.addInline(rscId, res);

			mailSender.send(message);
			logger.info("嵌入静态资源的邮件已经发送。");
		} catch (MessagingException e) {
			logger.error("发送嵌入静态资源的邮件时发生异常！", e);
		}
	}
}
