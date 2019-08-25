package com.common.service;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 基础服务
 * 提供基本的CRUD方法
 *
 * @author jjh
 * @date 2019/8/25
 **/
@Service
public class BaseService {

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * 保存对象到数据库
     * @param object 待保存对象
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(Object object) {
        Session session = sessionFactory.getCurrentSession();
        session.save(object);
    }

    /**
     * 更新对象到数据库
     * @param object 待更新对象
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(Object object) {
        Session session = sessionFactory.getCurrentSession();
        session.update(object);
    }

    /**
     * 从数据库中删除对象
     * @param object 待删除对象
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Object object) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(object);
    }

    /**
     * 通过ID查找对象
     * @param tClass
     * @param id    唯一编号
     * @param <T>
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public <T> T byId(Class<T> tClass,Object id) {
        Session session = sessionFactory.getCurrentSession();
        Criteria c = session.createCriteria(tClass);
        c.add(Restrictions.eq("id",id));
        return (T) c.uniqueResult();
    }

    /**
     * 获取所有对象
     * @param tClass
     * @param <T>
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public <T> List<T> listAll(Class<T> tClass) {
        Session session = sessionFactory.getCurrentSession();
        Criteria c = session.createCriteria(tClass);
        return c.list();
    }

    /**
     * 根据limit，排序，列名相等来查询
     * @param tClass
     * @param limit
     * @param orders
     * @param <T>
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public <T> List<T> listByEqColumnAndOrder(Class<T> tClass, int limit, List<Order> orders, Map<String,Object> params) {
        Session session = sessionFactory.getCurrentSession();
        Criteria c = session.createCriteria(tClass);
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                c.add(Restrictions.eq(entry.getKey(),entry.getValue()));
            }
        }

        if(null != orders && orders.size() > 0){
            for (int i = 0,length = orders.size(); i < length; i++){
                c.addOrder(orders.get(i));
            }
        }
        if(limit>0){
            c.setMaxResults(limit);
        }
        return c.list();
    }

}
