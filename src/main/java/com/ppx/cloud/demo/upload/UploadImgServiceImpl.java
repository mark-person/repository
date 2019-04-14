package com.ppx.cloud.demo.upload;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ppx.cloud.common.contoller.ReturnMap;
import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.util.ApplicationUtils;

@Service
public class UploadImgServiceImpl extends MyDaoSupport implements UploadImgService {
	
	public Map<?, ?> deleteKnowledgeImg(String path) {
		
		String imgPath = ApplicationUtils.JAR_PARENT_HOME + UploadImgController.IMG_UPLOAD_PATH + UploadImgController.KNOWLEDGE_MODULE + path;
		File f = new File(imgPath);
		if (f.exists()) {
			f.delete();
		}
		
		File miniF = new File(imgPath + "_360.jpg");
		if (miniF.exists()) {
			miniF.delete();
		}
		
		return ReturnMap.of();
	}
	
	public Map<?, ?> deleteKnowledgeMiniImg(String path) {
		
		String imgPath = ApplicationUtils.JAR_PARENT_HOME + UploadImgController.IMG_UPLOAD_PATH + UploadImgController.KNOWLEDGE_MODULE + path;
		File miniF = new File(imgPath + "_360.jpg");
		if (miniF.exists()) {
			miniF.delete();
		}
		
		return ReturnMap.of();
	}
	
	
	public Map<?, ?> convertToMini(String path) throws Exception {
		String imgPath = ApplicationUtils.JAR_PARENT_HOME + UploadImgController.IMG_UPLOAD_PATH + UploadImgController.KNOWLEDGE_MODULE + path;
		// 缩放
		// convert -resize 200x100 src.jpg dest.jpg 200×100(等比缩放)
		String miniPath = imgPath + "_360.jpg";
		if (!new File(miniPath).exists()) {
			if (imgPath.endsWith(".gif")) {
				imgPath += "[0]";
			}
			String command = "convert -resize 360x360> " + imgPath + " " + miniPath;
			Process process = Runtime.getRuntime().exec(command);
			try (InputStream inputStream = process.getErrorStream();) {
				String cmdResult = new BufferedReader(new InputStreamReader(inputStream, "UTF-8")).lines()
						.collect(Collectors.joining(System.lineSeparator()));
				if (!StringUtils.isEmpty(cmdResult)) {
					throw new RuntimeException("convert执行结果出错:" + cmdResult);
				}
			}
		}
		return ReturnMap.of();
	}
}
