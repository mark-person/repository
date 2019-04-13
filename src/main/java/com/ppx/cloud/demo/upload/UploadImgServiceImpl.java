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

@Service
public class UploadImgServiceImpl extends MyDaoSupport implements UploadImgService {
	
	public Map<?, ?> deleteKnowledgeImg(String path) {
		
		String imgPath = UploadImgController.KNOWLEDGE_MODULE_PATH + path;
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
	
	
	public Map<?, ?> convertToMini(String path) throws Exception {
		String imgPath = UploadImgController.KNOWLEDGE_MODULE_PATH + path;
		// 缩放
		// convert -resize 200x100 src.jpg dest.jpg 200×100(等比缩放)
		String miniPath = imgPath + "_360.jpg";
		if (!new File(miniPath).exists()) {
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
