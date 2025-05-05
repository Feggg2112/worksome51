package com.easylive.utils;

import com.easylive.entity.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.easylive.entity.constants.constants;
import javax.annotation.Resource;

@Component
public class FFmpegUtils {

    private static final Logger log = LoggerFactory.getLogger(FFmpegUtils.class);
    @Resource
    private AppConfig appConfig;

    public void createImageThumbnail(String filePath) {
        try{
            String CMD = "ffmpeg -i \"%s\" -vf scale=200:-1 \"%s\"";
            CMD = String.format(CMD, filePath, filePath + constants.IMAGE_THUMBNAIL_SUFFIX);
            ProcessUtils.executeCommand(CMD, appConfig.getShowFFmpegLog());
        }catch (Exception e){
            log.info("斯密马赛，我懒得安装ffmpeg了");
        }

    }

}