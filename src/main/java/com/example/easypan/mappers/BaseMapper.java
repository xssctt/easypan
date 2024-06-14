package com.example.easypan.mappers;



import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface BaseMapper<T,P> {

    /**
     *
     * @param t
     * @return
     * insert:(插入)
     */
    Integer insert(@Param("bean") T t);

    /**
     *
     * @param t
     * @return
     * insertOrUpdate:(插入/更新)
     */
    Integer insertOrUpdate(@Param("bean") T t);


    /**
     *
     * @param list
     * @return
     * insertBatch:(批量插入)
     */
    Integer insertBatch(@Param("list") List<T> list);


    /**
     *
     * @param list
     * @return
     * insertOrUpdateBatch:(批量插入/更新)
     */
    Integer insertOrUpdateBatch(@Param("list") List<T> list);


    /**
     *
     * @param p
     * @return
     * selectList:(根据参数查询集合)
     */
    List<T> selectList(@Param("query") P p);


    /**
     *
     * @param p
     * @return
     * selectCount:(根据参数查询数量)
     */
    Integer selectCount(@Param("query") P p);


}
