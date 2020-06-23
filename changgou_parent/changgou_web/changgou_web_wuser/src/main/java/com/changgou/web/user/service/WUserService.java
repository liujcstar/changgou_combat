package com.changgou.web.user.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author: phx
 * @date: 2020/6/20
 * @time: 18:41
 */
public interface WUserService {
    String uploadImg(MultipartFile file);
}
