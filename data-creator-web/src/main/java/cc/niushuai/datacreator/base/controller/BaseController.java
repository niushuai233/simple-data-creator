/*
 * Copyright (C) 2019-2024 niushuai233 niushuai.cc
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cc.niushuai.datacreator.base.controller;

import cc.niushuai.datacreator.base.R;
import cc.niushuai.datacreator.base.service.BaseService;
import cc.niushuai.datacreator.common.valid.CreateValid;
import cc.niushuai.datacreator.common.valid.UpdateValid;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonView;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.query.QueryWrapperAdapter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

/**
 * 基础controller
 *
 * @author niushuai233
 * @date 2024/09/03 17:29
 * @since 0.0.1
 */
@Validated
@SuppressWarnings("unchecked")
public class BaseController<Service extends BaseService, Entity> {

    public static final String DEFAULT_PAGE_NO = "1";
    public static final String DEFAULT_PAGE_SIZE = "10";

    protected Service service;

    public Service getService() {
        return service;
    }

    @Lazy
    @Autowired
    public void setService(Service service) {
        this.service = service;
    }


    /**
     * 分页查询
     *
     * @param pageNumber 页码
     * @param pageSize   每页数据量
     * @param entity     查询条件
     * @return
     * @author niushuai233
     * @date 2024/09/10 16:17
     * @since 0.0.1
     */
    @GetMapping("/page")
    public R page(@RequestParam(defaultValue = DEFAULT_PAGE_NO) Integer pageNumber,
                  @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                  Entity entity) {

        QueryWrapper queryWrapper = QueryWrapperAdapter.create(entity);

        Page<Entity> page = service.page(new Page<Entity>(pageNumber, pageSize), queryWrapper);

        return R.success(page);
    }

    /**
     * 不分页查询
     *
     * @param entity
     * @return
     * @author niushuai233
     * @date 2024/09/10 16:19
     * @since 0.0.1
     */
    @GetMapping("/list")
    public R list(Entity entity) {

        QueryWrapper queryWrapper = QueryWrapperAdapter.create(entity);

        List<Entity> list = service.list(queryWrapper);

        return R.success(list);
    }

    /**
     * 根据id查询
     *
     * @param id 用户id
     * @return
     * @author niushuai233
     * @date 2024/09/10 16:19
     * @since 0.0.1
     */
    @GetMapping("/queryById")
    public R queryById(@NotBlank(message = "用户ID不能为空") String id) {
        return R.success(service.getById(id));
    }

    /**
     * 新增数据
     *
     * @param entity 待保存数据
     * @return
     * @author niushuai233
     * @date 2024/09/10 16:34
     * @since 0.0.1
     */
    @PostMapping("/save")
    public R save(@Valid @JsonView(CreateValid.class) @RequestBody Entity entity) {
        service.save(entity);
        return R.success();
    }

    /**
     * 更新数据
     *
     * @param entity 得更新数据
     * @return
     * @author niushuai233
     * @date 2024/09/10 16:34
     * @since 0.0.1
     */
    @PostMapping("/update")
    public R update(@Validated(UpdateValid.class) @JsonView(UpdateValid.class) @RequestBody Entity entity) {
        service.updateById(entity);
        return R.success(entity);
    }

    /**
     * 批量删除 支持单id或多id(逗号分隔)
     *
     * @param ids
     * @return
     * @author niushuai233
     * @date 2024/09/10 16:37
     * @since 0.0.1
     */
    @PostMapping("/deleteByIds")
    public R deleteByIds(String ids) {
        if (StrUtil.isEmpty(ids)) {
            return R.success();
        }
        return R.success(service.removeByIds(Arrays.asList(ids.split(","))));
    }

}
