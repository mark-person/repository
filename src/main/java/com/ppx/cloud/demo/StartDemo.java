package com.ppx.cloud.demo;



import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import com.ppx.cloud.auth.config.AuthAllConfigExec;
import com.ppx.cloud.auth.config.AuthConfigExec;
import com.ppx.cloud.auth.config.AuthGrantConfigExec;
import com.ppx.cloud.common.exception.custom.ConfigException;
import com.ppx.cloud.config.ConfigExec;
import com.ppx.cloud.config.ConfigUtils;
import com.ppx.cloud.config.api.ConfigApiServ;
import com.ppx.cloud.config.pojo.Config;
import com.ppx.cloud.monitor.config.MonitorSwitchConfigExec;
import com.ppx.cloud.monitor.config.MonitorThresholdConfigExec;

/**
 * # 启动设置
 * @author mark
 * @date 2019年1月3日
 */
@Service
public class StartDemo implements ApplicationListener<ContextRefreshedEvent> {
    
    @Autowired
    private ConfigApiServ configServ;
    
    @Value("${info.app.artifactId}")
    private String artifactId;
    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event)  {
    	
    	// 绑定config_name 	varchar(64) not null comment '每个名称对应一个ConfigExec的实现类',
    	
    	ConfigUtils.bindConfigExec("MONITOR_THRESHOLD", new MonitorThresholdConfigExec());
    	ConfigUtils.bindConfigExec("MONITOR_SWITCH", new MonitorSwitchConfigExec());
    	
    	ConfigUtils.bindConfigExec("AUTH_CONFIG", new AuthConfigExec());
    	ConfigUtils.bindConfigExec("AUTH_ALL", new AuthAllConfigExec());
    	ConfigUtils.bindConfigExec("AUTH_GRANT", new AuthGrantConfigExec());
    	
    	List<Config> configList = configServ.listConfig(artifactId);
    	for (Config config : configList) {
			String configName = config.getConfigName();
			String configValue = config.getConfigValue();
			
			ConfigExec configExec = ConfigUtils.getConfigExec(configName);
			if (configExec == null) {
				throw new ConfigException(configName + " not binding");
			}
			else {
				if (Strings.isNotEmpty(configValue)) {
					configExec.run(configValue);
				}
			}
		}
    	
    	
    	
    }
}
