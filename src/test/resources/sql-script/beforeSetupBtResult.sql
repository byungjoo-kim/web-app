/* Result */
INSERT
  INTO result (RESULT_SEQ, ADMISSION_ID, ITEM_ID, RESULT_DATE, RESULT_TIME, DEVICE_ID, REG_DT)
VALUES (99999999, 'A123456789', 'I0001', '2021-12-06', '10:00:00', 'device001', '2021-12-01 13:23:01');
/* Result Detail */
INSERT INTO result_detail (RESULT_SEQ, RESULT_TYPE, RESULT) VALUES (99999999, '01', '36');

