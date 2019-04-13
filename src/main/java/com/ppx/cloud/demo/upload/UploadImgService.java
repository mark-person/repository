package com.ppx.cloud.demo.upload;

import java.util.Map;

public interface UploadImgService {
	Map<?, ?> deleteKnowledgeImg(String path);
	
	Map<?, ?> convertToMini(String imgPath) throws Exception;
}
