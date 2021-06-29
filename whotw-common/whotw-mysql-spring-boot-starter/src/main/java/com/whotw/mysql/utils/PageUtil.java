package com.whotw.mysql.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.*;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author EdisonXu
 * @date 2019-09-30
 */
public class PageUtil {

    public static <T> Page<T> toSpringPage(IPage<T> iPage){
        if(iPage==null)
            return null;
        Sort sort = Sort.unsorted();
        List<OrderItem> orderItems = iPage.orders();
        if(CollectionUtils.isNotEmpty(orderItems)){
            List<Sort.Order> orders = orderItems
                    .stream()
                    .map(oi->new Sort.Order(oi.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, oi.getColumn()))
                    .collect(Collectors.toList());
            sort = Sort.by(orders);
        }
        Pageable pageRequest = PageRequest.of((int)(iPage.getCurrent()-1), (int)iPage.getSize(), sort);
        PageImpl<T> page = new PageImpl<T>(iPage.getRecords(), pageRequest, iPage.getTotal());
        return page;
    }

    public static <T> com.baomidou.mybatisplus.extension.plugins.pagination.Page toMybatisPlusPage(Page<T> pageImpl){
        if(pageImpl==null)
            return null;
        Pageable pageable = pageImpl.getPageable();
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> page = toMyBatisPlusPage(pageable);
        page.setRecords(pageImpl.getContent());
        return page;
    }

    public static <T> com.baomidou.mybatisplus.extension.plugins.pagination.Page toMyBatisPlusPage(Pageable pageable){
        return toMyBatisPlusPage(pageable, null);
    }

    public static <T> com.baomidou.mybatisplus.extension.plugins.pagination.Page toMyBatisPlusPage(Pageable pageable, com.baomidou.mybatisplus.extension.plugins.pagination.Page page){
        if(page==null)
            page = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<T>(
                    pageable.getPageNumber()+1,
                    pageable.getPageSize());
        else{
            page.setCurrent(pageable.getPageNumber()+1);
            page.setSize(pageable.getPageSize());
        }

        Sort sort = pageable.getSort();
        if(sort!=null && sort.isSorted()){
            Iterator<Sort.Order> iterator = sort.iterator();
            while(iterator.hasNext()){
                Sort.Order order = iterator.next();
                OrderItem orderItem = Sort.Direction.ASC == order.getDirection() ? OrderItem.asc(order.getProperty()) : OrderItem.desc(order.getProperty());
                page.addOrder(orderItem);
            }
        }
        return page;
    }
}
