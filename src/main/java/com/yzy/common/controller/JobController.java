package com.yzy.common.controller;

import com.yzy.common.config.Constant;
import com.yzy.common.domain.TaskDO;
import com.yzy.common.service.JobService;
import com.yzy.common.utils.PageUtils;
import com.yzy.common.utils.Query;
import com.yzy.common.utils.ResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-09-26 20:53:48
 */
@Controller
@RequestMapping("/common/job")
public class JobController extends BaseController {
    @Autowired
    private JobService taskScheduleJobService;

    @GetMapping()
    String taskScheduleJob() {
        return "common/job/job";
    }

    @ResponseBody
    @GetMapping("/list")
    public PageUtils list(@RequestParam Map<String, Object> params) {
        // 查询列表数据
        Query query = new Query(params);
        List<TaskDO> taskScheduleJobList = taskScheduleJobService.list(query);
        int total = taskScheduleJobService.count(query);
        PageUtils pageUtils = new PageUtils(taskScheduleJobList, total);
        return pageUtils;
    }

    @GetMapping("/add")
    String add() {
        return "common/job/add";
    }

    @GetMapping("/edit/{id}")
    String edit(@PathVariable("id") Long id, Model model) {
        TaskDO job = taskScheduleJobService.get(id);
        model.addAttribute("job", job);
        return "common/job/edit";
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public ResultDTO info(@PathVariable("id") Long id) {
        TaskDO taskScheduleJob = taskScheduleJobService.get(id);
        return ResultDTO.ok().put("taskScheduleJob", taskScheduleJob);
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/save")
    public ResultDTO save(TaskDO taskScheduleJob) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return ResultDTO.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        if (taskScheduleJobService.save(taskScheduleJob) > 0) {
            return ResultDTO.ok();
        }
        return ResultDTO.error();
    }

    /**
     * 修改
     */
    @ResponseBody
    @PostMapping("/update")
    public ResultDTO update(TaskDO taskScheduleJob) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return ResultDTO.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        taskScheduleJobService.update(taskScheduleJob);
        return ResultDTO.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    public ResultDTO remove(Long id) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return ResultDTO.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        if (taskScheduleJobService.remove(id) > 0) {
            return ResultDTO.ok();
        }
        return ResultDTO.error();
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    public ResultDTO remove(@RequestParam("ids[]") Long[] ids) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return ResultDTO.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        taskScheduleJobService.batchRemove(ids);

        return ResultDTO.ok();
    }

    @PostMapping(value = "/changeJobStatus")
    @ResponseBody
    public ResultDTO changeJobStatus(Long id, String cmd) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return ResultDTO.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        String label = "停止";
        if ("start".equals(cmd)) {
            label = "启动";
        } else {
            label = "停止";
        }
        try {
            taskScheduleJobService.changeStatus(id, cmd);
            return ResultDTO.ok("任务" + label + "成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultDTO.ok("任务" + label + "失败");
    }

}
