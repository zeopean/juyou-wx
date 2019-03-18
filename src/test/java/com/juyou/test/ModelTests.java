package com.juyou.test;


import com.juyou.wx.entity.dao.IUserDao;
import com.juyou.wx.entity.dao.InvicardDao;
import com.juyou.wx.entity.dao.InvicardLogDao;
import com.juyou.wx.entity.dto.Invicard;
import com.juyou.wx.entity.dto.InvicardLog;
import com.juyou.wx.entity.dto.User;
import com.juyou.wx.entity.mapper.InvicardMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with idea
 * Description:
 *
 * @author zeopean
 * Date: 2018-09-16
 */
public class ModelTests extends AppApplicationTests {

    @Autowired
    private IUserDao userDao;

    @Autowired
    private InvicardDao invicardDao;

    @Autowired
    private InvicardLogDao invicardLogDao;

    @Autowired
    private InvicardMapper invicardMapper;

    @Test
    public void user()
    {
        User user = userDao.selectById(88888800L);
    }

    @Test
    public void invicard()
    {
        Invicard invicard = invicardDao.selectById(1);

    }

    @Test
    public void invicardLog()
    {
        InvicardLog invicardLog = invicardLogDao.selectById(1L);
    }

    @Test
    public void top()
    {
        int num = invicardDao.userTopNum(1,88888806);
    }

}
