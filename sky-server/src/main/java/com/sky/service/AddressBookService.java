package com.sky.service;

import com.sky.entity.AddressBook;
import com.sky.result.Result;

import java.util.List;

public interface AddressBookService {
    /**
     * 添加新地址
     * @param addressBook
     */
    void save(AddressBook addressBook);

    /**
     * 查询地址簿
     * @return
     */
    List<AddressBook> list();

    /**
     * 获取默认地址
     * @param currentId
     * @return
     */
    List<AddressBook> getDefault(Long currentId);

    /**
     * 根据ID修改地址
     * @param addressBook
     */
    void update(AddressBook addressBook);

    /**
     * 根据ID删除地址
     * @param id
     */
    void delete(Long id);

    /**
     * 根据ID查询地址
     * @param id
     * @return
     */
    AddressBook getAddr(Long id);

    /**
     * 设置默认地址
     * @param addressBook
     */
    void setDefault(AddressBook addressBook);
}
