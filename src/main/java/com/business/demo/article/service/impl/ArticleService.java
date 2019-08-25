package com.business.demo.article.service.impl;

import com.business.demo.article.dao.ArticleMapper;
import com.business.demo.article.model.Article;
import com.business.demo.article.service.IArticleService;
import com.common.exception.BusinessException;
import com.common.service.BaseService;
import com.common.util.PojoUtils;
import com.common.web.form.PageRequestForm;
import com.common.web.form.SimpleForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 文章 服务层实现
 */
@Service
public class ArticleService implements IArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private BaseService baseService;

    /**
     * 获取列表
     * @param form
     * @return
     */
    @Override
    public Page<Article> find(PageRequestForm form) {
        return articleMapper.find(form);
    }

    /**
     * 保存
     * @param article
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(Article article) {
        if (StringUtils.isEmpty(article.getTitle())) {
            throw new BusinessException("标题不能为空");
        }
        baseService.save(article);
    }

    /**
     * 更新
     * @param article
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Article article) {
        Article entity = baseService.byId(Article.class,article.getId());
        if (entity == null) {
            throw new BusinessException("未找到待更新的对象");
        }
        PojoUtils.copyPropertiesIgnoreNull(article,entity);
        baseService.update(entity);
    }

    /**
     * 删除
     * @param form
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(SimpleForm form) {
        if (StringUtils.isEmpty(form.getId())) {
            throw new BusinessException("id不能为空");
        }
        Article entity = baseService.byId(Article.class,form.getId());
        if (entity == null) {
            throw new BusinessException("未找到待删除的对象");
        }
        baseService.delete(entity);
    }
}
