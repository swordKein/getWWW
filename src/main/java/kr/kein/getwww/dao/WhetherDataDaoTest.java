package kr.kein.getwww.dao;

import kr.kein.getwww.mapper.MybatisConnectionFactory;
import kr.kein.getwww.vo.Paging;
import kr.kein.getwww.vo.Whether;
import org.apache.ibatis.session.SqlSession;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class WhetherDataDaoTest {

    @Test
    public void selectAddrGroupTest() {
        // Mybatis 세션연결
        SqlSession sqlSession = MybatisConnectionFactory.getSqlSessionFactory()
                .openSession(true);

        // Mapper 연결
        WhetherDataDao whetherDataDao = sqlSession.getMapper(WhetherDataDao.class);

        // Select
        List<Whether> result = null;
        result = whetherDataDao.selectAddrGroup();
        for (Whether item : result) {
            System.out.println("item : " + item.toString());
        }
        System.out.println("item count:"+result.size());
    }

    @Test
    public void selectPagingTest() {
        // Mybatis 세션연결
        SqlSession sqlSession = MybatisConnectionFactory.getSqlSessionFactory()
                .openSession(true);

        // Mapper 연결
        WhetherDataDao whetherDataDao = sqlSession.getMapper(WhetherDataDao.class);

        // Select
        List<Whether> result = null;
        Paging page = new Paging();
        page.setPageNo(1);
        page.setPageSize(10);
        result = whetherDataDao.selectPaging(page);
        for (Whether item : result) {
            System.out.println("item : " + item.toString());
        }

    }
}