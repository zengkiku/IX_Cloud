package com.ix.server.dfs.service.impl;

import cn.hutool.core.io.FastByteArrayOutputStream;
import com.alibaba.nacos.common.utils.UuidUtils;
import com.ix.api.dfs.domain.SysDfs;
import com.ix.framework.core.exception.IXException;
import com.ix.framework.utils.JacksonUtils;
import com.ix.framework.utils.StringUtils;
import com.ix.framework.utils.file.FileUtils;
import com.ix.server.dfs.config.MinioClientConfig;
import com.ix.server.dfs.mapper.DFSMapper;
import com.ix.server.dfs.service.IDFSService;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import io.undertow.util.DateUtils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: Minio文件上传实现类
 */
@Primary
@Service
@RequiredArgsConstructor
public class DFSServiceMinioImpl implements IDFSService {

    private static final Logger logger = LoggerFactory.getLogger(DFSServiceQiNiuImpl.class);

    private final MinioClientConfig minioClientConfig;

    private final MinioClient minioClient;

    private final DFSMapper dfsMapper;


    /**
     *
     * @param files 上传的文件
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<SysDfs> uploadFiles(MultipartFile[] files) {
        List<SysDfs> fileDfs = new ArrayList<>();

        try {
            for (MultipartFile file : files) {
                SysDfs sysDfs = this.upload(file);
                fileDfs.add(sysDfs);
            }

            dfsMapper.batchSysDfs(fileDfs);
            return fileDfs;
        } catch (Exception e) {
            logger.error("文件上传异常", e);
            throw new IXException("文件上传异常");
        }
    }

    /**
     *
     * @param file 上传的文件
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysDfs uploadFile(MultipartFile file) {
        try {
            SysDfs sysDfs = this.upload(file);

            dfsMapper.insertSysDfs(sysDfs);
            return sysDfs;
        } catch (Exception e) {
            logger.error("文件上传异常", e);
            throw new IXException("文件上传异常");
        }
    }

    public SysDfs upload(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            if (StringUtils.isBlank(originalFilename)) {
                throw new IXException("原始文件名为空");
            }
//            String fileName = UuidUtils.generateUuid() + originalFilename.substring(originalFilename.lastIndexOf("."));
//            String objectName = DateUtils.toDateString(new Date()) + "/" + fileName;
            String filePath = FileUtils.defaultUploadPath(originalFilename);

            PutObjectArgs objectArgs = PutObjectArgs.builder()
                    .bucket(minioClientConfig.getBucketName())
                    .object(filePath)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build();
            //文件名称相同会覆盖
            minioClient.putObject(objectArgs);

            SysDfs sysDfs = new SysDfs();
            sysDfs.setSize(file.getSize());
            sysDfs.setPath(FileUtils.SLASH + minioClientConfig.getBucketName() + filePath);
            sysDfs.setSpaceName(FileUtils.getFileSpaceName(filePath));
            sysDfs.setType(FileUtils.getSuffix(originalFilename));
            sysDfs.setFileName(FileUtils.getFileName(filePath));
            sysDfs.setOriginalFileName(originalFilename);

            return sysDfs;
        } catch (Exception e) {
            logger.error("文件上传异常", e);
            throw new IXException("文件上传异常");
        }
    }

    /**
     *
     * @param fileIds 文件ID
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteFile(Long[] fileIds) {
        try {
            List<SysDfs> sysDfsList = dfsMapper.selectDfsListByFileIds(fileIds);
            for (SysDfs sysDfs : sysDfsList) {
                String key = sysDfs.getPath();
                minioClient.removeObject(RemoveObjectArgs.builder().bucket(minioClientConfig.getBucketName()).object(key).build());
            }
            // 删除数据库信息
            dfsMapper.deleteSysDfsByFileIds(fileIds);
        } catch (Exception e) {
            logger.error("删除文件出错", e);
            throw new IXException("删除文件出错");
        }
    }

    /**
     *
     * @param sysDfs SysDfs
     * @return
     */
    @Override
    public List<SysDfs> selectSysDfsList(SysDfs sysDfs) {
        return dfsMapper.selectDfsList(sysDfs);
    }


    /**
     * 查看存储bucket是否存在
     *
     * @return boolean
     */
    public Boolean bucketExists(String bucketName) {
        boolean found;
        try {
            found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            logger.error("查找存储bucket出错");
            return false;
        }
        return found;
    }

    /**
     * 创建存储bucket
     *
     * @return Boolean
     */
    public Boolean makeBucket(String bucketName) {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            logger.error("创建存储bucket出错");
            return false;
        }
        return true;
    }

    /**
     * 删除存储bucket
     *
     * @return Boolean
     */
    public Boolean removeBucket(String bucketName) {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            logger.error("删除存储bucket出错");
            return false;
        }
        return true;
    }

    /**
     * 获取全部bucket
     */
    public List<Bucket> getAllBuckets() {
        try {
            return minioClient.listBuckets();
        } catch (Exception e) {
            logger.error("获取全部bucket出错");
        }
        return null;
    }


    /**
     * 预览
     *
     * @param fileName
     * @return
     */
    public String preview(String fileName) {
        // 查看文件地址
        GetPresignedObjectUrlArgs build = GetPresignedObjectUrlArgs
                .builder()
                .bucket(minioClientConfig.getBucketName())
                .object(fileName)
                .method(Method.GET).build();
        try {
            return minioClient.getPresignedObjectUrl(build);
        } catch (Exception e) {
            logger.error("文件预览失败");
            throw new IXException("文件预览失败");
        }
    }

    /**
     * 文件下载
     *
     * @param fileName 文件名称
     * @param res      response
     * @return Boolean
     */
    public void download(String fileName, HttpServletResponse res) {
        GetObjectArgs objectArgs = GetObjectArgs.builder().bucket(minioClientConfig.getBucketName())
                .object(fileName).build();
        try (GetObjectResponse response = minioClient.getObject(objectArgs)) {
            byte[] buf = new byte[1024];
            int len;
            try (FastByteArrayOutputStream os = new FastByteArrayOutputStream()) {
                while ((len = response.read(buf)) != -1) {
                    os.write(buf, 0, len);
                }
                os.flush();
                byte[] bytes = os.toByteArray();
                res.setCharacterEncoding("utf-8");
                // 设置强制下载不打开
                // res.setContentType("application/force-download");
                res.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
                try (ServletOutputStream stream = res.getOutputStream()) {
                    stream.write(bytes);
                    stream.flush();
                }
            }
        } catch (Exception e) {
            logger.error("文件" + fileName + "下载失败");
            throw new IXException("文件" + fileName + "下载失败");
        }
    }

    /**
     * 查看文件对象
     *
     * @return 存储bucket内文件对象信息
     */
    public List<Item> listObjects() {
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(minioClientConfig.getBucketName()).build());
        List<Item> items = new ArrayList<>();
        try {
            for (Result<Item> result : results) {
                items.add(result.get());
            }
        } catch (Exception e) {
            logger.error(JacksonUtils.toJson(e));
            return null;
        }
        return items;
    }

    /**
     * 删除
     *
     * @param fileName
     * @return
     * @throws Exception
     */
    public boolean remove(String fileName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(minioClientConfig.getBucketName()).object(fileName).build());
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    public static String extractUploadFileName(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        if (fileName == null) {
            throw new RuntimeException("无法获取文件名称");
        }
        String prefix = UUID.randomUUID().toString();
        return getCurrentDatePath() + "/" + prefix + "-" + fileName;
    }

    /*** 获取当前日期路径** @return {@link String}*/
    private static String getCurrentDatePath() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.format(new Date());
    }
}
