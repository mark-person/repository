package com.ppx.cloud.demo.upload;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ppx.cloud.common.contoller.ReturnMap;
import com.ppx.cloud.common.util.ApplicationUtils;

/**
 * 
 * convert -quality 30% 3.jpg out.jpg
 * 
 * ImageMagick
 * 
 * @author mark
 * @date 2019年2月27日
 */
@Controller
public class UploadImgController {
	
	private final static String IMG_UPLOAD_PATH = "img/";
	
	private final static String KNOWLEDGE_MODULE = "knowledge/";
	private final static String KNOWLEDGE_MAIN = "main/";
	private final static String KNOWLEDGE_ADDITIONAL = "additional/";
	
	
	public Map<?, ?> uploadKnowledge(MultipartFile[] mFile, Integer isMain[]) throws Exception {
		
		var returnList = new ArrayList<String>();
		
		// 不存就创建文件夹 >>>>>>>>>>>>>>>>>>>>>
		String modulePath = ApplicationUtils.JAR_PARENT_HOME + IMG_UPLOAD_PATH + KNOWLEDGE_MODULE;
		// 7天一个文件夹2019w11
		Date today = new Date();
		// String.format("%tj", d)一年的第几天
		String dateFolder = String.format("%tY", today) + "w"
				+ String.format("%02d", Integer.parseInt(String.format("%tj", today)) / 10) + "/";
					
		String mainPath = modulePath + dateFolder + KNOWLEDGE_MAIN;
		String additionalPath = modulePath + dateFolder + KNOWLEDGE_ADDITIONAL;
		File mainPathFile = new File(mainPath);
		if (!mainPathFile.exists()) {
			mainPathFile.mkdirs();
		}
		File additionalPathFile = new File(additionalPath);
		if (!additionalPathFile.exists()) {
			additionalPathFile.mkdirs();
		}
		
		for (int i = 0; i < mFile.length; i++) {
			MultipartFile file = mFile[i];
			
			String fileName = file.getOriginalFilename();
			String ext = ".jpg";
			if (!"blob".equals(fileName)) {
				ext = fileName.substring(fileName.lastIndexOf("."));
			}
			
			String imgFileName = UUID.randomUUID().toString().replaceAll("-", "") + ext;
			if (isMain[i] == 1) {
				// >>>>>>>>>>>>>>>>>主
				file.transferTo(new File(mainPath + imgFileName));
				
				// 缩放
				// convert -resize 200x100 src.jpg dest.jpg 200×100(等比缩放)
				String miniPath = mainPath + imgFileName + "_100.jpg";
				String command = "convert -resize 400 " + mainPath + imgFileName + " " + miniPath;
				Process process = Runtime.getRuntime().exec(command);
				try (InputStream inputStream = process.getErrorStream();) {
					String cmdResult = new BufferedReader(new InputStreamReader(inputStream, "UTF-8")).lines()
							.collect(Collectors.joining(System.lineSeparator()));
					if (!StringUtils.isEmpty(cmdResult)) {
						return ReturnMap.of(4001, "convert执行结果出错:" +cmdResult);
					}
				} catch (Exception e) {
					return ReturnMap.of(4002, "转换命令出错:" + e.getMessage());
				}
				returnList.add(KNOWLEDGE_MODULE + dateFolder + KNOWLEDGE_MAIN + imgFileName);
			}
			else {
				// >>>>>>>>>>>>>>>>>附加
				file.transferTo(new File(additionalPath + imgFileName));
				returnList.add(KNOWLEDGE_MODULE + dateFolder + KNOWLEDGE_ADDITIONAL + imgFileName);
			}
		}
		
		return ReturnMap.of("list", returnList);
	}

}