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

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@Controller
@RequestMapping
public class IndexController {

	private static Map<String, String> cache = new ConcurrentHashMap<>();

	@RequestMapping({"", "/", "index"})
	public String index() {
		return "index";
	}

	/* 登录页面: 1展示二维码 2轮询扫码状态 */
	@GetMapping("login")
	public String login(Model model) {
		String uuid = UUID.randomUUID().toString().replace("-", "");
		cache.put(uuid, "");
		model.addAttribute("uuid", uuid);

		String url = String.format("http://i.chaoxing.com/login/%s/prompt", uuid);
		model.addAttribute("url", url);

		return "login";
	}

	/* 刷新uuid */
	@ResponseBody
	@GetMapping("refresh/{uuid}")
	public String refresh(@PathVariable String uuid) {
		cache.remove(uuid);
		uuid = UUID.randomUUID().toString().replace("-", "");
		cache.put(uuid, "");
		return "success";
	}

	/* 轮询uuid状态 */
	@ResponseBody
	@GetMapping("polling/{uuid}/status")
	public String polling(@PathVariable String uuid) {
		return cache.get(uuid);
	}



	/* -------------------------- 手机上的操作 -------------------------- */

	/* 手机已经是登录了的, 并且确保手机打开的WebView能够携带相应的Cookie到服务端 */

	/* 扫码后, 跳转链接, 二维码编码地址: http://domain/login/{uuid}/prompt */
	@GetMapping("login/{uuid}/prompt")
	public String loginPrompt(@PathVariable String uuid, @CookieValue("UID") Integer uid, Model model) {
		if (cache.containsKey(uuid)) {
			cache.replace(uuid, "doing"); // 登录页面显示: xxx已扫码, 确认登录中
		}

		String url = String.format("http://i.chaoxing.com/login/%s/confirm", uuid);
		model.addAttribute("url", url);
		model.addAttribute("uuid", uuid);

		return "prompt";
	}

	/* 确认登录 */
	@ResponseBody
	@GetMapping("login/{uuid}/confirm")
	public String loginConfirm(@PathVariable String uuid, @CookieValue("UID") Integer uid, Model model) {
		if (cache.containsKey(uuid)) {
			cache.replace(uuid, "success"); // 已确认登录, 即将跳转
		}
		return "success";
	}




	@ResponseBody
	@GetMapping(value = "qrcode", produces = MediaType.IMAGE_PNG_VALUE)
	public byte[] qrcode(String content) {
		return createQrCode(content);
	}

	private static byte[] createQrCode(String content) {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
			hints.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8);
			hints.put(EncodeHintType.MARGIN, 1);
			BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 400, 400, hints);
			int width = bitMatrix.getWidth();
			int height = bitMatrix.getHeight();
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
				}
			}
			ImageIO.write(image, "PNG", out);
//			return Base64.encodeBase64String(out.toByteArray());
			return out.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}

