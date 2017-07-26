package kr.kein.getwww.dao;

import kr.kein.getwww.vo.Paging;
import kr.kein.getwww.vo.Whether;

import java.util.List;

public interface WhetherDataDao {
    public List<Whether> selectPaging(Paging page);
    public List<Whether> selectAddrGroup();
}
