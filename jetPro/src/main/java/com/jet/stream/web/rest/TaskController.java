package com.jet.stream.web.rest;

import cn.hutool.core.collection.CollectionUtil;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.Job;
import com.jet.stream.domain.Task;
import com.jet.stream.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 2021/9/28
 *
 * @author kongmin
 * @version 1.0.0
 * <p>
 * 任务处理类
 */
@RestController
@RequestMapping("/api/tasking")
@CrossOrigin(origins = "*",maxAge = 3600)
public class TaskController {

    @Qualifier("jetInstance")
    @Autowired
    private JetInstance jetInstance;

    @Autowired
    private TaskService taskService;

    private final Logger logger = LoggerFactory.getLogger(TaskController.class);


    @GetMapping("/list")
    public ResponseEntity<List<Task>> listTask() {
        List<Job> jobs = jetInstance.getJobs();
        return ResponseEntity.ok(taskService.transform(jobs));

    }


    @GetMapping("/task/{jobId}")
    public ResponseEntity<Task> getJob(@PathVariable("jobId") Long jobId) {
        Job job = jetInstance.getJob(jobId);
        Task task = taskService.transform(job);
        return ResponseEntity.ok(task);

    }

    @GetMapping("/cancel/{jobId}")
    public ResponseEntity<Task> cancel(@PathVariable("jobId")  Long jobId) {
        Job job = jetInstance.getJob(jobId);
        try{
            job.cancel();
        }catch (Exception e){
            logger.error("throw error when cancel the job",e);
            return ResponseEntity.status(500).build();
        }

        Task task = taskService.transform(job);
        return ResponseEntity.ok(task);

    }

    @GetMapping("/restart/{jobId}")
    public ResponseEntity<Task> restart(@PathVariable("jobId")  Long jobId) {
        Job job = jetInstance.getJob(jobId);
        try{
            job.restart();
        }catch (Exception e){
            logger.error("throw error when restart the job",e);
            return ResponseEntity.badRequest().build();
        }
        Task task = taskService.transform(job);

        return ResponseEntity.ok(task);

    }

    @GetMapping("/suspend/{jobId}")
    public ResponseEntity<Task> suspend(@PathVariable("jobId") Long jobId) {
        Job job = jetInstance.getJob(jobId);
        try{
            job.suspend();
        }catch (Exception e){
            logger.error("throw error when suspend the job",e);
            return ResponseEntity.status(500).build();
        }
        Task task = taskService.transform(job);
        return ResponseEntity.ok(task);

    }


    @GetMapping("/resume/{jobId}")
    public ResponseEntity<Task> resume(@PathVariable("jobId") Long jobId) {
        Job job = jetInstance.getJob(jobId);
        try{
            job.resume();
        }catch (Exception e){
            logger.error("throw error when resume the job",e);
            return ResponseEntity.status(500).build();
        }
        Task task = taskService.transform(job);
        return ResponseEntity.ok(task);

    }

    @PostMapping(value = "/submit")
    public ResponseEntity<String> submit(@RequestPart("file") MultipartFile file) {
        try{
           taskService.uploadTaskJar(file);

        }catch (Exception e){
            logger.error("throw error when submit the job",e);
            return ResponseEntity.status(500).build();
        }
        return ResponseEntity.ok("SUCCESS");

    }





}
