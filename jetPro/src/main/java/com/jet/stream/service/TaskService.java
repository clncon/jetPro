package com.jet.stream.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.Job;
import com.hazelcast.jet.impl.JetBootstrap;
import com.hazelcast.jet.impl.util.ConcurrentMemoizingSupplier;
import com.jet.stream.domain.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 2021/10/1
 *
 * @author kongmin
 * @version 1.0.0
 * <p>
 * 类功能说明
 */
@Service
public class TaskService {

    private Logger logger = LoggerFactory.getLogger(TaskService.class);

    private static ConcurrentMemoizingSupplier<JetInstance> supplier;

    @Qualifier("jetInstance")
    @Autowired
    private JetInstance jetInstance;




    public List<Task> transform (List<Job> jobs){
        List<Task> tasks = new ArrayList<>();

        if(CollectionUtil.isNotEmpty(jobs)){
            for(Job job:jobs){
                Task task = transform(job);
                tasks.add(task);
            }


        }

        return tasks;


    }

    public Task transform (Job job){
        Task task = new Task();
        task.setId(String.valueOf(job.getId()));
        task.setTaskName(job.getName());
        task.setStatus(job.getStatus().name());
        task.setSubmitTime(job.getSubmissionTime());

        return task;
    }

    public void uploadTaskJar(MultipartFile file){

        try{
            InputStream fileInputStream = file.getInputStream();
            String filePath = String.format("/tmp/%s",file.getOriginalFilename());
            File uploadFile = new File(filePath);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(uploadFile));
            byte[] bs = new byte[1024];
            int len;
            while ((len = fileInputStream.read(bs)) != -1) {
                bos.write(bs, 0, len);
            }
            bos.flush();
            JetBootstrap.executeJar(()->jetInstance,uploadFile.getAbsolutePath(),null,uploadFile.getName(),null, ListUtil.empty());
        }catch (Exception e){
         logger.error("throws some errors where upload file",e);
        }

    }

}
