package com.yzy.common.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.yzy.common.config.BmsConfig;
import com.yzy.common.domain.FileDO;
import com.yzy.common.service.FileService;
import com.yzy.common.utils.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件上传
 *
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-09-19 16:02:20
 */
@Controller
@RequestMapping("/common/sysFile")
public class FileController extends BaseController {

    @Autowired
    private FileService sysFileService;

    @Autowired
    private BmsConfig bmsConfig;

    private static void readExcel(String fileName, String subfix, BmsConfig bmsConfig) {
        //读取Excel文件数据
        if ((".xls".equalsIgnoreCase(subfix)) || ((".xlsx".equalsIgnoreCase(subfix)))) {
            ExcelReader reader = ExcelUtil.getReader(cn.hutool.core.io.FileUtil.file(bmsConfig.getUploadPath() + fileName));
            reader.setIgnoreEmptyRow(true);
            List<List<Object>> lists = reader.read(1);
            System.out.println(lists);
        }
    }

    @GetMapping()
    @RequiresPermissions("common:sysFile:sysFile")
    String sysFile(Model model) {
        Map<String, Object> params = new HashMap<>(16);
        return "common/file/file";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("common:sysFile:sysFile")
    public PageUtils list(@RequestParam Map<String, Object> params) {
        // 查询列表数据
        Query query = new Query(params);
        List<FileDO> sysFileList = sysFileService.list(query);
        int total = sysFileService.count(query);
        PageUtils pageUtils = new PageUtils(sysFileList, total);
        return pageUtils;
    }

    @GetMapping("/add")
        // @RequiresPermissions("common:bComments")
    String add() {
        return "common/sysFile/add";
    }

    @GetMapping("/edit")
        // @RequiresPermissions("common:bComments")
    String edit(Long id, Model model) {
        FileDO sysFile = sysFileService.get(id);
        model.addAttribute("sysFile", sysFile);
        return "common/sysFile/edit";
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("common:info")
    public ResultDTO info(@PathVariable("id") Long id) {
        FileDO sysFile = sysFileService.get(id);
        return ResultDTO.ok().put("sysFile", sysFile);
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("common:save")
    public ResultDTO save(FileDO sysFile) {
        if (sysFileService.save(sysFile) > 0) {
            return ResultDTO.ok();
        }
        return ResultDTO.error();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("common:update")
    public ResultDTO update(@RequestBody FileDO sysFile) {
        sysFileService.update(sysFile);

        return ResultDTO.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    // @RequiresPermissions("common:remove")
    public ResultDTO remove(Long id, HttpServletRequest request) {
        String fileName = bmsConfig.getUploadPath() + sysFileService.get(id).getUrl().replace("/files/", "");
        if (sysFileService.remove(id) > 0) {
            boolean b = FileUtil.deleteFile(fileName);
            if (!b) {
                return ResultDTO.error("数据库记录删除成功，文件删除失败");
            }
            return ResultDTO.ok();
        } else {
            return ResultDTO.error();
        }
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("common:remove")
    public ResultDTO remove(@RequestParam("ids[]") Long[] ids) {
        sysFileService.batchRemove(ids);
        return ResultDTO.ok();
    }

    @ResponseBody
    @PostMapping("/upload")
    ResultDTO upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        String fileName = file.getOriginalFilename();
        //获取文件后缀名
        String subfix = fileName.substring(fileName.lastIndexOf("."));

        fileName = FileUtil.renameToUUID(fileName);
        FileDO sysFile = new FileDO(FileType.fileType(fileName), "/files/" + fileName, new Date());
        try {
            FileUtil.uploadFile(file.getBytes(), bmsConfig.getUploadPath(), fileName);
            readExcel(fileName, subfix, bmsConfig);
        } catch (Exception e) {
            return ResultDTO.error();
        }

        if (sysFileService.save(sysFile) > 0) {
            return ResultDTO.ok().put("fileName", sysFile.getUrl());
        }
        return ResultDTO.error();
    }


}
