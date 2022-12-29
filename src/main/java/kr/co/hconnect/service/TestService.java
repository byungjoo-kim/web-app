package kr.co.hconnect.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opentok.Archive;
import com.opentok.OpenTok;
import com.opentok.exception.OpenTokException;
import kr.co.hconnect.vo.*;
import kr.co.hconnect.repository.AiInferenceDao;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.apache.commons.io.FileUtils;
import kr.co.hconnect.common.zipUtil;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.springframework.util.StringUtils;
import ws.schild.jave.*;


/**
 * 복약 서비스
 */
@Service
@Transactional(rollbackFor = Exception.class, readOnly = true)
public class TestService {


    private int apikey = 47595911;
    private String apiSecret = "2ddde1eb92a2528bd22be0c465174636daca363d";

    private final AiInferenceDao aiInferenceDao;

    public TestService(AiInferenceDao aiInferenceDao) {
        this.aiInferenceDao = aiInferenceDao;
    }

    public void fileDownload_Url() throws IOException, OpenTokException {

        String OUTPUT_FILE_PATH = "E:\\score";
        String FILE_URL = "E:\\upload\\gizmo.mp4";
        String archiveId = "아카이브 파일 경로";

        Archive archive = null;
        try {
            System.out.println("파일 다운로드 시작");
            InputStream in = new URL("file:///" +FILE_URL).openStream();
            Path imagePath = Paths.get(OUTPUT_FILE_PATH);
            Files.copy(in, imagePath);
            System.out.println("파일 다운로드 종료");

        } catch(IOException e){
            System.out.println(e.getMessage());
        }

    }

    public String fileDownload2(testVO vo) throws IOException, OpenTokException , MalformedURLException {

        String rtn = "";

        String OUTPUT_FILE_PATH = ""; // "출력 파일 경로";
        String FILE_URL ="";         // "리소스 경로";
        //String archiveId = "ab279a4b-f158-417b-95e2-4f2e4f1a3428";
        String outputDir  = "E:\\python\\video\\";
        Archive archive = null;

        InputStream is = null;
        FileOutputStream os = null;

        String archiveId = vo.getArchiveId();
        String admissionId = vo.getAdmissionId();

        outputDir += "\\" + admissionId;

        //디렉토리 생성하기
        File dir = new File(outputDir);
        if (!dir.exists()){
            try{
                dir.mkdir();
            } catch (Exception e){
                System.out.println(e.getMessage());
                return "";
            }
        }
        //디렉토리 생성하기
        try{
            OpenTok openTok = new OpenTok(apikey, apiSecret);
            archive =openTok.getArchive(archiveId);

            FILE_URL = archive.getUrl();

            rtn = FILE_URL;

            //해당 url
            URL url = new URL(FILE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();

            System.out.println("responseCode " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String fileName = "";
                String disposition = conn.getHeaderField("Content-Disposition");
                String contentType = conn.getContentType();

                // 일반적으로 Content-Disposition 헤더에 있지만
                // 없을 경우 url 에서 추출해 내면 된다.
                if (disposition != null) {
                    String target = "filename=";
                    int index = disposition.indexOf(target);
                    if (index != -1) {
                        fileName = disposition.substring(index + target.length() + 1);
                    }
                } else {
                    //fileName = FILE_URL.substring(FILE_URL.lastIndexOf("/") + 1);
                    fileName = admissionId + ".mp4";

                }

                System.out.println("Content-Type = " + contentType);
                System.out.println("Content-Disposition = " + disposition);
                System.out.println("fileName = " + fileName);

                is = conn.getInputStream();
                os = new FileOutputStream(new File(outputDir, fileName));

                final int BUFFER_SIZE = 4096;
                int bytesRead;
                byte[] buffer = new byte[BUFFER_SIZE];


                System.out.println("is.read(buffer) = " + is.read(buffer));

                while ((bytesRead = is.read(buffer)) != -1) {
                    System.out.println("is read = " );
                    os.write(buffer, 0, bytesRead);
                }
                os.close();
                is.close();
                System.out.println("File downloaded");

                //파일 다운로드 등록
                ArchiveDownVO dn = new ArchiveDownVO();
                dn.setAdmissionId(admissionId);
                dn.setDnFolder(outputDir);
                dn.setDnFileName(fileName);
                dn.setArchiveId(archiveId);


            } else {
                System.out.println("No file to download. Server replied HTTP code: " + responseCode);
            }
            conn.disconnect();

        } catch (OpenTokException e) {
            System.out.println(e.getMessage());
        } catch (Exception e){
            System.out.println("An error occurred while trying to download a file.");
            e.printStackTrace();
            try {
                if (is != null){
                    is.close();
                }
                if (os != null){
                    os.close();
                }
            } catch (IOException e1){
                e1.printStackTrace();
            }
        }

        return rtn;

    }

    public void fileDownload3(String url, String fileName) throws IOException, OpenTokException , MalformedURLException {

        File f= new File((fileName));
        FileUtils.copyURLToFile(new URL(url), f);


    }
    public int fileDownload_file() throws IOException, OpenTokException {

        int rtn = 0;

        String OUTPUT_FILE_PATH = "E:\\score";
        String FILE_URL = "E:\\upload\\gizmo.mp4";
        String archiveId = "아카이브 파일 경로";

        String filePath = "E:\\upload\\gizmo.mp4"; // 대상 파일
        FileInputStream inputStream = null; // 파일 스트림
        FileOutputStream outputStream = null;
        BufferedInputStream bufferedInputStream = null; // 버퍼 스트림
        BufferedOutputStream bufferedOutputStream = null;

        String TargetfilePath = "E:\\score\\gizmo.mp4"; // 대상 파일

        try {
            inputStream = new FileInputStream(filePath);// 파일 입력 스트림 생성
            bufferedInputStream = new BufferedInputStream(inputStream);// 파일 출력 스트림 생성

            outputStream = new FileOutputStream(TargetfilePath);
            bufferedOutputStream = new BufferedOutputStream(outputStream);

            // 파일 내용을 담을 버퍼(?) 선언
            byte[] readBuffer = new byte[1024];
            while (bufferedInputStream.read(readBuffer, 0, readBuffer.length) != -1)
            {
                //버퍼 크기만큼 읽을 때마다 출력 스트림에 써준다.
                bufferedOutputStream.write(readBuffer);
            }

        }
        catch (Exception e) {
            rtn = 1;
            System.out.println(e.getMessage());
        }
        return rtn;
    }

    public int csvCreate(testVO vo) {
        int resultCount = 0;

        List list= null;
        String filePath = vo.getFilePath();

        //데이터를 받아오고 파일로 쓰기
        try {

            File file = new File(filePath);

            if( file.exists() ) {
                file.delete();
            }

            //csv 파일의 기존 값에 이어쓰려면 위처럼 tru를 지정하고 기존갑을 덮어 쓰려면 true를 삭제한다
            BufferedWriter fw = new BufferedWriter(new FileWriter(filePath));

            //쿼리 를 한다.
            //
            List<ScoreVO> dataList = aiInferenceDao.scoreList();

            if (dataList.size() > 0 ) {
                //타이틀 넣기
                String tData = "";
                tData = "환자 id";   //환자 id
                tData += "," + "나이";    // 나이
                tData += "," + "심박수";     //심박수
                tData += "," + "산소포화도";   // 산소포화도
                tData += "," + "혈압";       //혈압
                tData += "," + "체온";     //체온
                tData += "," + "고혈압여부";    //고혈압 여부

                fw.write(tData);
                fw.newLine();

                for (ScoreVO dt : dataList) {
                    String aData = "";
                    aData = dt.getAdmissionId();   //환자 id
                    aData += "," + dt.getAge();    // 나이
                    aData += "," + dt.getPr();     //심박수
                    aData += "," + dt.getSpo2();   // 산소포화도
                    aData += "," + dt.getSbp();    //수축기 혈압
                    aData += "," + dt.getBt();     //체온
                    aData += "," + dt.getHyp();    //고혈압 여부

                    fw.write(aData);
                    fw.newLine();
                }
            }
            fw.flush();
            //객체 닫기
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            return resultCount;
        }

    }

    public String fileDownload4(testVO vo) throws IOException, OpenTokException , MalformedURLException {

        String rtn = "";

        String OUTPUT_FILE_PATH = ""; // "출력 파일 경로";
        String FILE_URL ="";         // "리소스 경로";
        //String archiveId = "ab279a4b-f158-417b-95e2-4f2e4f1a3428";
        String outputDir  = "E:/python/video/";
        Archive archive = null;
        String vFileName ="";
        String getJsonPath ="";
        String getVideoPath ="";

        InputStream is = null;
        FileOutputStream os = null;

        String archiveId = vo.getArchiveId();
        String admissionId = vo.getAdmissionId();

        outputDir += admissionId;

        //디렉토리 생성하기
        File dir = new File(outputDir);
        if (!dir.exists()){
            try{
                dir.mkdir();
            } catch (Exception e){
                System.out.println(e.getMessage());
                return "";
            }
        }
        //디렉토리 생성하기
        try{

            OpenTok openTok = new OpenTok(apikey, apiSecret);
            archive =openTok.getArchive(archiveId);

            FILE_URL = archive.getUrl();

            rtn = FILE_URL;

            //해당 url
            URL url = new URL(FILE_URL);
            String outputFile = outputDir + "/" + admissionId +".zip";

            File f = new File(outputFile);
            //파일 다운 로드
            FileUtils.copyURLToFile(url, f );

            String zipFile = admissionId + ".zip";

            zipUtil zipf = new zipUtil();
            outputDir +="/"  ;
            System.out.println("outputDir >> " + outputDir);
            System.out.println("zipFile >> "+ zipFile);

            if(zipf.unZip(outputDir, zipFile, outputDir)){

                /* json  파일 찾아서 읽기 */
                getJsonPath = outputDir +  admissionId + "/";
                getVideoPath = getJsonPath;

                System.out.println("getJsonPath  " + getJsonPath);
                File[] jsonFiles = getFileNames(getJsonPath, "json");

                System.out.println("jsonFiles.length  " + jsonFiles.length);
                String jsonfileName="";
                for(int i=0; i<jsonFiles.length; i++ ){
                    System.out.println("= jsonfile   " + i );
                    System.out.println("===================================================="  );

                    jsonfileName = jsonFiles[i].getName();
                    System.out.println("jsonfileName  " + jsonfileName);
                }

                getJsonPath += jsonfileName;

                //json 파일에서 환자의 파일이름을 읽는다.
                // jackson objectmapper 객체 생성
                JSONParser parser = new JSONParser();

                try(Reader reader = new FileReader(getJsonPath)){

                    JSONObject jsonObject = (JSONObject) parser.parse(reader);
                    System.out.println(jsonObject);

                    String name = (String) jsonObject.get("name");
                    System.out.println(name);

                    JSONArray files = (JSONArray) jsonObject.get("files");
                    System.out.println(files);

                    for ( int i=0; i<files.size(); i++){

                        System.out.println("=files  " + i +" ===========================================");
                        JSONObject dataObject = (JSONObject) files.get(i);
                        System.out.println("files: connectionData==>"+dataObject.get("connectionData"));

                        String connData = (String) dataObject.get("connectionData");

                        //파일명 찾기
                        if (connData.contains(admissionId)){
                            System.out.println("files: connectionData==>");
                            vFileName = (String) dataObject.get("filename");
                            System.out.println(vFileName);
                        }

                    }

                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

            }
            //mp3 파일 추출
            if (!StringUtils.isEmpty(vFileName)) {
                //mpe3파일 추출
                System.setProperty("java.io.tmpdir", "E:\\python\\");

                String sourcePath = getVideoPath + vFileName;
                String targetPath = getVideoPath + admissionId +".mp3";

                System.out.println("video sourcePath" + sourcePath);
                System.out.println("video targetPath" + targetPath);

                Map<String, Object> cvAudio = new HashMap<String, Object>();
                cvAudio.put("sourcePath", sourcePath);
                cvAudio.put("targetPath", targetPath);

                System.out.println("video convert start");

                String r = convertMp3(cvAudio);

                System.out.println("video convert end");

            }
        } catch (OpenTokException e) {
            System.out.println(e.getMessage());
        }

        return rtn;

    }


    private File[] getFileNames(String targetDirName, String fileExt) {
        File dir = new File(targetDirName);

        File[] files = null;
        if (dir.isDirectory()) {
            final String ext = fileExt.toLowerCase();
            files = dir.listFiles(new FileFilter() {
                public boolean accept(File file) {
                    if (file.isDirectory()) {
                        return false;
                    }
                    return file.getName().toLowerCase().endsWith("." + ext);
                }
            });
        }

        return files;
    }

    private String convertMp3(Map<String, Object> cvAudio) {

        String rtn ="";
        //mpe3파일 추출
        System.setProperty("java.io.tmpdir", "E:\\python\\");

        String sourcePath =(String)cvAudio.get("sourcePath");
        String targetPath = (String)cvAudio.get("targetPath");


        System.out.println("video sourcePath" + sourcePath);
        System.out.println("video targetPath" + targetPath);

        File source = new File(sourcePath);
        File target = new File(targetPath);

        try {
            //Audio Attributes
            System.out.println("Audio Attributes");
            AudioAttributes audio = new AudioAttributes();
            audio.setCodec("libmp3lame");
            audio.setBitRate(128000);
            audio.setChannels(2);
            audio.setSamplingRate(44100);

            //Encoding attributes
            System.out.println("Encoding attributes");
            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setFormat("mp3");
            attrs.setAudioAttributes(audio);

            //Encode
            //Encoder encoder = new Encoder(new MyFFMPEGExecutableLocator())
            System.out.println("Encoding ");
            Encoder encoder = new Encoder(new DefaultFFMPEGLocator());
            encoder.encode(new MultimediaObject(source), target, attrs);

        } catch (Exception ex) {
            rtn = ex.getMessage();
            ex.printStackTrace();
        }
        return rtn;
    }

    private String convertMp4(Map<String, Object> cvVideo) {

        String rtn ="";
        //mpe3파일 추출
        System.setProperty("java.io.tmpdir", "E:\\python\\");

        String sourcePath =(String)cvVideo.get("sourcePath");
        String targetPath = (String)cvVideo.get("targetPath");


        System.out.println("video sourcePath" + sourcePath);
        System.out.println("video targetPath" + targetPath);

        File source = new File(sourcePath);
        File target = new File(targetPath);

        try {
            //Audio Attributes
            System.out.println("Audio Attributes");
            AudioAttributes audio = new AudioAttributes();
            audio.setCodec("libmp3lame");
            audio.setBitRate(128000);
            audio.setChannels(2);
            audio.setSamplingRate(44100);

            //Encoding attributes
            System.out.println("Encoding attributes");
            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setFormat("mp3");
            attrs.setAudioAttributes(audio);

            //Encode
            //Encoder encoder = new Encoder(new MyFFMPEGExecutableLocator())
            System.out.println("Encoding ");
            Encoder encoder = new Encoder(new DefaultFFMPEGLocator());
            encoder.encode(new MultimediaObject(source), target, attrs);

        } catch (Exception ex) {
            rtn = ex.getMessage();
            ex.printStackTrace();
        }
        return rtn;
    }

}