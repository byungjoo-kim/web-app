/* Patient */
INSERT
  INTO patient (PATIENT_ID, LOGIN_ID, PASSWORD, PATIENT_NM, SSN, BIRTH_DATE, SEX, CELL_PHONE
            ,ZIP_CODE, ADDRESS1, ADDRESS2, DEL_YN, REG_DT, UPD_DT)
VALUES ('P123456789', 'wtest', '123456789', 'w테스트', '1211301111111', '2012-11-30', 'M', '01012345678'
       ,'123456', '서울시 서초구', 'test', 'N', '2021-12-06 08:57:08', '2021-12-06 08:57:08');
/* Admission */
INSERT
  INTO admission (ADMISSION_ID, PATIENT_ID, ADMISSION_DATE, DSCHGE_SCHDLD_DATE, DSCHGE_DATE, QANTN_DIV
                 ,PERSON_CHARGE, CENTER_ID, ROOM, DEL_YN, REG_ID, REG_DT, UPD_ID, UPD_DT)
VALUES ('A123456789', 'P123456789', '2021-12-05', '2022-12-30', null, '1'
       ,'헬스커넥트', 'C001', '01', 'N', 'admin', '2021-12-06 08:57:08', 'admin', '2021-12-06 08:57:08');

