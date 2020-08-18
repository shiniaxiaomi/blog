package com.lyj.blog.service;

import freemarker.template.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import java.util.Map;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/8/14 12:03 下午
 */
@Service
public class AsyncService {

    @Value("${host}")
    String host;

    @Autowired
    private JavaMailSender mailSender;//注入邮件工具类

    @Autowired
    private Configuration configuration;//freemarker配置

    // 开启异步发送反馈邮件
    @Async
    public void feedbackMail(String from,String content){
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mailSender.createMimeMessage(), false);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo("lyj.8066@qq.com");
            mimeMessageHelper.setSubject("博客反馈意见");
            mimeMessageHelper.setText("<html><div>"+content+"</div></html>",true);//邮件内容
            mailSender.send(mimeMessageHelper.getMimeMessage());//正式发送邮件
        } catch (Exception e) {
            throw new RuntimeException("邮件发送异常");
        }
    }

    // 开启异步发送评论回复邮件
    @Async
    public void sendMail(String toEmail, Map<String, Object> model){
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mailSender.createMimeMessage(), false);
            mimeMessageHelper.setFrom("lyj.8066@qq.com");
            mimeMessageHelper.setTo(toEmail);
            mimeMessageHelper.setSubject("来自 http://luyingjie.cn 的评论回复");

            Template template = configuration.getTemplate("tool/email.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template,model);
            mimeMessageHelper.setText(html,true);//邮件内容
            mailSender.send(mimeMessageHelper.getMimeMessage());//正式发送邮件
        } catch (Exception e) {
            throw new RuntimeException("邮件发送异常");
        }

    }

    // 开启异步发送通知邮件
    @Async
    public void notifyEmail(int blogId,String html) {
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mailSender.createMimeMessage(), false);
            mimeMessageHelper.setFrom("lyj.8066@qq.com");
            mimeMessageHelper.setTo("806648324@qq.com");
            mimeMessageHelper.setSubject("收到新评论");

            String sb = html + "<br><a href='http://" + host + "/blog/" + blogId + "'>查看博客</a>";
            mimeMessageHelper.setText(sb,true);//邮件内容
            mailSender.send(mimeMessageHelper.getMimeMessage());//正式发送邮件
        } catch (MessagingException e) {
            throw new RuntimeException("邮件发送异常");
        }
    }


}
