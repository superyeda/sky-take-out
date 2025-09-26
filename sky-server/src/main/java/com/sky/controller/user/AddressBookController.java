package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api("地址簿相关接口")
@RequestMapping("/user/addressBook")
@Slf4j
public class AddressBookController {

    @Autowired
    AddressBookService addressBookService;

    @PostMapping
    @ApiOperation("新增地址")
    private Result save(@RequestBody AddressBook addressBook){
        log.info("新增地址");
        addressBookService.save(addressBook);
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("获取地址簿")
    private Result<List<AddressBook>> list(){
        log.info("查询地址簿");
        List<AddressBook> addrList = addressBookService.list();
        return Result.success(addrList);
    }

    @GetMapping("/default")
    @ApiOperation("查询默认地址")
    private Result<AddressBook> getDefault() {
        log.info("查询默认地址");
        List<AddressBook> aDefault = addressBookService.getDefault(BaseContext.getCurrentId());
        if(aDefault != null && aDefault.size()>0){
            return Result.success(aDefault.get(0));
        }
        return Result.error("未查询到默认地址");
    }

    @PutMapping
    @ApiOperation("根据ID修改地址")
    private Result updateAddr(@RequestBody AddressBook addressBook){
        addressBookService.update(addressBook);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("根据ID删除地址")
    private Result deleteAddr(@RequestParam Long id){
        addressBookService.delete(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询地址")
    private Result<AddressBook> getAddr(@PathVariable Long id){
        AddressBook addressBook = addressBookService.getAddr(id);
        return Result.success(addressBook);
    }

    @PutMapping("/default")
    @ApiOperation("设置默认地址")
    private Result setDefault(@RequestBody AddressBook addressBook){
        addressBookService.setDefault(addressBook);
        return Result.success();
    }
}
