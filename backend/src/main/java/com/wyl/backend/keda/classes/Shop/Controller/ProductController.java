package com.wyl.backend.keda.classes.Shop.Controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


import com.wyl.backend.keda.classes.Shop.ProductInfo;
import com.wyl.backend.keda.classes.Shop.SQL.ProductInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductInfoMapper productInfoMapper;

    @PostMapping("/add")
    public ProductInfo addProduct(@RequestBody ProductInfo productInfo) {
        if (productInfoMapper.selectCount(new QueryWrapper<ProductInfo>().eq("name", productInfo.getName())) != 0) {
            productInfoMapper.updateById(productInfo);
        } else {
            productInfoMapper.insert(productInfo);
        }
        return productInfoMapper.selectById(productInfo.getId());
    }


    @GetMapping("/{id}")
    public ProductInfo getProduct(@PathVariable int id) {
        return productInfoMapper.selectById(id);
    }

    @GetMapping("/list")
    public List<ProductInfo> listProducts(@RequestParam(required = false) String name,
                                          @RequestParam(required = false) Double price,
                                          @RequestParam(required = false) String tags) {
        QueryWrapper<ProductInfo> queryWrapper = new QueryWrapper<>();
        if (name != null) {
            queryWrapper.like("name", name);
        }
        if (price != null) {
            queryWrapper.eq("price", price);
        }
        if (tags != null) {
            queryWrapper.like("tags", tags);
        }
        return productInfoMapper.selectList(queryWrapper);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteProduct(@PathVariable int id) {
        productInfoMapper.deleteById(id);
    }

    @PostMapping("/upload")
    public List<String> handleFileUpload(@RequestParam("files") MultipartFile[] files) {
        String folder = System.getProperty("user.dir") + "/uploads/";
        File uploadDir = new File(folder);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        List<String> filePaths = new ArrayList<>();
        for (MultipartFile file : files) {
            String originalFilename = file.getOriginalFilename();
            String newFilename = UUID.randomUUID().toString() + "_" + originalFilename;

            try {
                file.transferTo(new File(folder + newFilename));
                filePaths.add("/images/" + newFilename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filePaths;
    }

}
