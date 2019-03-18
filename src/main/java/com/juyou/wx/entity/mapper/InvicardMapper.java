package com.juyou.wx.entity.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.juyou.wx.entity.dto.Invicard;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * Created with idea
 * Description:
 *
 * @author zeopean
 * Date: 2018-09-19
 */
@Component
public interface InvicardMapper extends BaseMapper<Invicard> {

    @Select("select id, share_user_id from (SELECT (@rowno:=@rowno+1) as id, M.share_user_id from " +
            "(SELECT `share_user_id`, `code`, count(1) as num from `plat_invicard` where code=#{code} GROUP BY `share_user_id` ORDER BY num desc) as M ," +
            "(SELECT @rowno:=0) AS N ) as C where share_user_id=#{shareUserId}")
    Invicard userTopNum(@Param("code") int code, @Param("shareUserId") long shareUserId);

}
