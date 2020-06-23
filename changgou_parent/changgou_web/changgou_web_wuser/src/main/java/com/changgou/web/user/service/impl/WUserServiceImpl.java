package com.changgou.web.user.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.changgou.util.PicUtils;
import com.changgou.util.RandomUtil;
import com.changgou.web.user.service.WUserService;
import com.changgou.web.user.utils.ConstantPropertiesUtils;
import net.coobird.thumbnailator.Thumbnails;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author: phx
 * @date: 2020/6/20
 * @time: 18:42
 */
@Service
@Transactional
public class WUserServiceImpl implements WUserService {

    /**
     * 头像上传
     *
     * @param file
     * @return
     */
    @Override
    public String uploadImg(MultipartFile file) {

        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = ConstantPropertiesUtils.ENDPOINT;
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
        String accessKeyId = ConstantPropertiesUtils.KEYID;
        String accessKeySecret = ConstantPropertiesUtils.KEYSECRET;
        String bucketName = ConstantPropertiesUtils.BUCKETNAME;


        try {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

           file= PicUtils.tranMultipartFile(file);
            // 上传文件流。
            InputStream inputStream = file.getInputStream();

            //获取文件名称
            String fileName = file.getOriginalFilename();
            //1.由于文件名重复会覆盖，生成随机文件名
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            fileName = uuid + fileName;
            //2.把文件按照日期分类
            String datePath = new DateTime().toString("yyyy/MM/dd");
            fileName = datePath + "/" + fileName; //2020/6/4/dafdf.jpg

            //第二个参数：上传到oss的文件路径和文件名称  /aa/bb1.jpg
            ossClient.putObject(bucketName, fileName, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();

            //把上传之后oss返回的文件url返回（）
            //url格式：https://edu-guli-study.oss-cn-beijing.aliyuncs.com/%25U%7EHW%2502P2OH6FXR%29%5B8%60T2A.png
            String url = "https://" + bucketName + "." + endpoint + "/" + fileName;
            return url;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
