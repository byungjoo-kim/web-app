package kr.co.hconnect.repository;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;
import kr.co.hconnect.domain.Result;
import kr.co.hconnect.domain.ResultDetail;
import kr.co.hconnect.domain.SaveSleepTimeResult;
import org.springframework.stereotype.Repository;

/**
 * 측정결과 Dao
 */
@Repository
public class ResultDao extends EgovAbstractMapper {

    /**
     * 측정결과 저장
     *
     * @param result 측정결과
     * @return affectedRow
     */
    public int insertResult(Result result) {
        return insert("kr.co.hconnect.sqlmapper.insertResult", result);
    }

    /**
     * 측정결과 상세 저장
     *
     * @param resultDetail 측정결과 상세
     * @return affectedRow
     */
    public int insertResultDetail(ResultDetail resultDetail) {
        return insert("kr.co.hconnect.sqlmapper.insertResultDetail", resultDetail);
    }

    /**
     * 수면 측정결과 저장
     *
     * @param resultInfo 수면 측정결과 저장 정보
     * @return affectedRow
     */
    public int insertResultSleepTime(SaveSleepTimeResult resultInfo) {
        return insert("kr.co.hconnect.sqlmapper.insertResultSleepTime", resultInfo);
    }

}