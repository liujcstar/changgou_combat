package com.changgou.util;
import com.changgou.util.RandomUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.http.entity.ContentType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PicUtils {

    /**
     * 图片缩略
     * @param needCoverted 需要被转换的图片地址
     * @param coverted  转换过得图片地址
     * @return
     * @throws IOException
     */
    private static File  tranPic(File needCoverted,File coverted) throws IOException {


        File endfile=new File(coverted.getPath()+File.separator+needCoverted.getName());
        FileInputStream fileInputStream=new FileInputStream(needCoverted);
        Thumbnails.of(fileInputStream)
            .scale(0.5)//大于1就是放大,小于1就是缩小
                .toFile(endfile);
        //返回转换过图片的路径
        fileInputStream.close();
        return endfile;
    }

    public static   MultipartFile  tranMultipartFile(MultipartFile file) throws IOException {
        //判断文件大小
        if (file.getSize() > 2097152) { //判断图片大小 单位字节,判断文件是否大于2M

            File fileSourcePath = new File("D:\\tempPic");
            File fileSource = new File(fileSourcePath, file.getOriginalFilename());
            if (!fileSourcePath.exists()) {
                fileSourcePath.mkdirs();
            }
            //将multipartFile 转换为File,存到本地临时文件
            file.transferTo(fileSource);


            File temp = new File("D:\\littlePic");
            if (!fileSourcePath.exists()) {
                fileSourcePath.mkdirs();
            }
            //用工具类将文件缩略,并返回缩略过图片地址
            File littlePic = PicUtils.tranPic(fileSource, temp);

            //将file转换为multipartFile
            InputStream inputStream = new FileInputStream(littlePic);

            file = new MockMultipartFile(littlePic.getName(), littlePic.getName(), ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream);


            inputStream.close();

            littlePic.delete();

            fileSource.delete();

        }
        return file;
    }
}
