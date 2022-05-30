package kr.co.hconnect.controller;


import egovframework.rte.fdl.cmmn.exception.FdlException;
import kr.co.hconnect.common.ApiResponseCode;
import kr.co.hconnect.common.VoValidationGroups;
import kr.co.hconnect.exception.InvalidRequestArgumentException;
import kr.co.hconnect.jwt.TokenDetailInfo;
import kr.co.hconnect.service.TreatmentCenterService;
import kr.co.hconnect.vo.ResponseVO;
import kr.co.hconnect.vo.SaveCompletedVO;
import kr.co.hconnect.vo.TreatmentCenterSaveVO;
import kr.co.hconnect.vo.TreatmentCenterVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

/**
 * 생활치료센터
 */
@RestController
@RequestMapping("/api/treatmentCenter")
public class TreatmentCenterController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TreatmentCenterController.class);

    private final MessageSource messageSource;

    /**
     * 생활치료센터 서비스
     */
    private final TreatmentCenterService treatmentCenterService;

    /**
     * 생성자
     * @param messageSource MessageSource
     * @param treatmentCenterService 생활치료센터 서비스
     */
    @Autowired
    public TreatmentCenterController(MessageSource messageSource, TreatmentCenterService treatmentCenterService) {
        this.messageSource = messageSource;
        this.treatmentCenterService = treatmentCenterService;
    }
    
    // C: 입력, insert + 메서드, add +
    // R: 조회, select + 메서드, get +
    // U: 수정, update + 메서드, modify +
    // D: 삭제, delete + 메서드, remove +
    // C+U: 저장, save + 메서드

    @RequestMapping(value = "/info", method = RequestMethod.POST)
    public ResponseVO<TreatmentCenterVO> selectTreatmentCenter(@RequestBody TreatmentCenterVO vo) {
        ResponseVO<TreatmentCenterVO> responseVO = new ResponseVO<>();

        if (StringUtils.isEmpty(vo.getCenterId())) {
            responseVO.setCode(ApiResponseCode.CODE_INVALID_REQUEST_PARAMETER.getCode());
            responseVO.setMessage(messageSource.getMessage("validation.null.result"
                    , null, Locale.getDefault()));
        } else {
            responseVO.setCode(ApiResponseCode.SUCCESS.getCode());
            responseVO.setResult(treatmentCenterService.selectTreatmentCenter(vo));
        }

        return responseVO;
    }

    /**
     * 생활치료센터 리스트 검색
     * @return 생활치료센터 목록
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseVO<List<TreatmentCenterVO>> selectTreatmentCenterList(@RequestBody TreatmentCenterVO vo) {
        ResponseVO<List<TreatmentCenterVO>> responseVO = new ResponseVO<>();

        try {
            responseVO.setCode(ApiResponseCode.SUCCESS.getCode());
            responseVO.setMessage("조회성공");
            responseVO.setResult(treatmentCenterService.selectTreatmentCenterList(vo));
        } catch (RuntimeException e) {
            responseVO.setCode(ApiResponseCode.CODE_INVALID_REQUEST_PARAMETER.getCode());
            responseVO.setMessage(e.getMessage());
        }

        return responseVO;
    }

    /**
     * 생활치료센터 입력
     * @param vo 생활치료센터 정보
     * @return 생활치료센터 목록
     */
    @RequestMapping(value = "/save", method = RequestMethod.PUT)
    public ResponseVO<SaveCompletedVO<TreatmentCenterVO, TreatmentCenterVO>> insertTreatmentCenter(
              @Validated(VoValidationGroups.add.class) @RequestBody TreatmentCenterSaveVO vo
            , @RequestAttribute TokenDetailInfo tokenDetailInfo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestArgumentException(bindingResult);
        }

        ResponseVO<SaveCompletedVO<TreatmentCenterVO, TreatmentCenterVO>> responseVO = new ResponseVO<>();

        // 저장정보 구성
        TreatmentCenterVO treatmentCenterVO = new TreatmentCenterVO();
        BeanUtils.copyProperties(vo, treatmentCenterVO);
        treatmentCenterVO.setRegId(tokenDetailInfo.getId());

        try {
            String centerId = treatmentCenterService.insertTreatmentCenter(treatmentCenterVO);

            SaveCompletedVO<TreatmentCenterVO, TreatmentCenterVO> saveCompletedVO = new SaveCompletedVO<>();
            // 저장정보 조회
            treatmentCenterVO = new TreatmentCenterVO();
            treatmentCenterVO.setCenterId(centerId);
            saveCompletedVO.setData(treatmentCenterService.selectTreatmentCenter(treatmentCenterVO));
            // 리스트 조회
            saveCompletedVO.setList(treatmentCenterService.selectTreatmentCenterList(vo.getSearchInfo()));

            responseVO.setCode(ApiResponseCode.SUCCESS.getCode());
            responseVO.setResult(saveCompletedVO);
        } catch (FdlException e) {
            responseVO.setCode(ApiResponseCode.CODE_INVALID_REQUEST_PARAMETER.getCode());
            responseVO.setMessage(e.getMessage());
        }

        return responseVO;
    }

//    /**
//     * 생활치료센터 수정
//     * @param vo 생활치료센터 정보
//     * @return 생활치료센터 목록
//     */
//    @RequestMapping(value = "/save", method = RequestMethod.PATCH)
//    public List<TreatmentCenterVO> updateTreatmentCenter(@Validated(VoValidationGroups.modify.class) @RequestBody TreatmentCenterSaveVO vo
//            , @RequestAttribute TokenDetailInfo tokenDetailInfo, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            throw new InvalidRequestArgumentException(bindingResult);
//        }
//
//        vo.setUpdId(tokenDetailInfo.getId());
//
//        //# 수정
//        treatmentCenterService.updateTreatmentCenter(vo);
//
//        //# 목록조회
//        return treatmentCenterService.selectTreatmentCenterList(vo);
//    }
//
//    /**
//     * 생활치료센터 삭제
//     * @param vo 생활치료센터 정보
//     * @return 생활치료센터 목록
//     */
//    @RequestMapping(value = "/save", method = RequestMethod.DELETE)
//    public List<TreatmentCenterVO> deleteTreatmentCenter(@Validated(VoValidationGroups.delete.class) @RequestBody TreatmentCenterSaveVO vo
//            , @RequestAttribute TokenDetailInfo tokenDetailInfo, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            throw new InvalidRequestArgumentException(bindingResult);
//        }
//
//        //# 삭제
//        treatmentCenterService.deleteTreatmentCenter(vo);
//        //# 목록조회
//        return treatmentCenterService.selectTreatmentCenterList(null);
//    }

}
