package com.maidf.javaquiz.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletResponse;

@Component
public class CaptchaUtils {
    // 验证码字符集（排除易混淆字符，如 0, O, 1, I 等）
    private static final String CODES = "23456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnopqrstuvwxyz";
    private static final int WIDTH = 120; // 图片宽度
    private static final int HEIGHT = 40; // 图片高度
    private static final int CODE_LENGTH = 5; // 验证码长度
    private static final int LINE_COUNT = 10; // 干扰线数量
    private static final Random RANDOM = new Random();

    /**
     * 生成随机验证码字符串
     */
    public static String generateCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(CODES.charAt(RANDOM.nextInt(CODES.length())));
        }
        return sb.toString();
    }

    /**
     * 生成验证码图片
     */
    public static BufferedImage generateImage(String code) {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // 设置背景色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // 绘制字符
        for (int i = 0; i < code.length(); i++) {
            g.setColor(getRandomColor());
            g.setFont(new Font("Arial", Font.BOLD, 28));
            // 字符偏移
            g.drawString(String.valueOf(code.charAt(i)), 20 * i + 10, 30);
        }

        // 添加干扰线
        for (int i = 0; i < LINE_COUNT; i++) {
            g.setColor(getRandomColor());
            int x1 = RANDOM.nextInt(WIDTH);
            int y1 = RANDOM.nextInt(HEIGHT);
            int x2 = RANDOM.nextInt(WIDTH);
            int y2 = RANDOM.nextInt(HEIGHT);
            g.drawLine(x1, y1, x2, y2);
        }

        g.dispose();
        return image;
    }

    /**
     * 获取随机颜色
     */
    private static Color getRandomColor() {
        return new Color(
                RANDOM.nextInt(200),
                RANDOM.nextInt(200),
                RANDOM.nextInt(200));
    }

    /**
     * 将图片写入响应流
     */
    public static void writeToResponse(BufferedImage image, HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        ImageIO.write(image, "jpeg", response.getOutputStream());
    }

}