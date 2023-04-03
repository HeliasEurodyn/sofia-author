package com.crm.sofia.services.report;

import com.crm.sofia.dto.list.ListDTO;
import com.crm.sofia.dto.report.ReportDTO;
import com.crm.sofia.dto.tag.TagDTO;
import com.crm.sofia.exception.DoesNotExistException;
import com.crm.sofia.mapper.report.ReportMapper;
import com.crm.sofia.model.report.Report;
import com.crm.sofia.repository.report.ReportRepository;
import com.crm.sofia.services.auth.JWTService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportDesignerService {
    @Autowired
    private  ReportRepository reportRepository;
    @Autowired
    private ReportMapper reportMapper;
    @Autowired
    private  JWTService jwtService;

    @Autowired
    private  DataSource dataSource;




    public List<ReportDTO> getObject() {
        List<ReportDTO> reportList = this.reportRepository.getObject();
        return reportList;
    }

    public ReportDTO getObject(String id) {
        Report optionalEntity = this.reportRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("Report Does Not Exist"));

        Report entity = optionalEntity;
        ReportDTO dto = this.reportMapper.map(entity);
        return dto;
    }

    public List<ReportDTO> getObjectByTag(String tag) {
        return this.reportRepository.getObjectByTag(tag);
    }

    public List<TagDTO> getTag() {
        List<TagDTO> tag = reportRepository.findTagDistinct();
        return tag;
    }

//    public String getReportType(Long id) {
//        return this.reportRepository.findType(id);
//    }

    public ReportDTO postObject(
            @RequestParam("file") MultipartFile multipartFile,
            @RequestParam("report") String reportBase64Str) throws IOException {

        // Get filename & extension from MultipartFile
        String filename = multipartFile.getOriginalFilename();
        String extension = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
        String reportUuid = UUID.randomUUID().toString();


        // If type != jrxml Throw exception
        if (!extension.equals("jrxml")) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Wrong file type");
        }

        // Get Bytes of File
        byte[] reportFileData = this.getFileBytes(multipartFile);

        // Deserialized ReportDTO to Object
        ReportDTO reportDTO = this.base64JsonStringToReportDTO(reportBase64Str);

        // Save Report
        ReportDTO createdReportDTO = this.postObject(filename, extension, reportUuid, reportFileData, reportDTO);

        // Clear files structure from filesystem
        this.deleteReportFolderFromFileSystemIfExists(reportUuid);

        return createdReportDTO;
    }


    public ReportDTO postObject(ReportDTO reportDTO) throws IOException {

        List<TagDTO> tags =
                reportDTO.getTags().stream().collect(Collectors.toMap(TagDTO::getId, p -> p, (p, q) -> p))
                        .values()
                        .stream().collect(Collectors.toList());

        reportDTO.setTags(tags);

        if (reportDTO.getId() == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File cannot be empty");
        } else {

            Optional<Report> optionalEntity = this.reportRepository.findById(reportDTO.getId());
            if (!optionalEntity.isPresent()) {
                throw new DoesNotExistException("Report Does Not Exist");
            }

            // Save
            ReportDTO createdReportDTO = this.postObject(
                    optionalEntity.get().getReportFilename(),
                    optionalEntity.get().getReportExtension(),
                    optionalEntity.get().getReportUuid(),
                    optionalEntity.get().getReportFileData(),
                    reportDTO);

            this.deleteReportFolderFromFileSystemIfExists(optionalEntity.get().getReportUuid());

            return createdReportDTO;
        }
    }

    private ReportDTO postObject(
            String filename,
            String extension,
            String reportUuid,
            byte[] reportFileData,
            ReportDTO reportDTO) throws IOException {

        Report report = this.reportMapper.map(reportDTO);
        report.setReportFilename(filename);
        report.setReportExtension(extension);
        report.setReportFileData(reportFileData);
        if ((report.getReportUuid() == null ? "" : report.getReportUuid()).equals("")) {
            report.setReportUuid(reportUuid);
        }

        if (report.getId() == null) report.setCreatedOn(Instant.now());
        if (report.getId() == null) report.setCreatedBy(jwtService.getUserId());
        report.setModifiedOn(Instant.now());
        report.setModifiedBy(jwtService.getUserId());

        List<Report> subreports = new ArrayList<>();
        report.getSubreports().forEach(subReport -> {
            Report subreport = this.reportRepository.findById(subReport.getId()).get();
            subreports.add(subreport);
        });
        report.setSubreports(subreports);

        Report createdReport = this.reportRepository.save(report);
        return this.reportMapper.map(createdReport);
    }

    public void deleteObject(String id) {
        Report optionalEntity = this.reportRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("Report Does Not Exist"));
        this.reportRepository.deleteById(optionalEntity.getId());
    }

//    @Transactional
//    public void print(HttpServletResponse response, long id, Map<String, Object> parameters) throws Throwable {
//
//        Optional<Report> optionalReport = reportRepository.findById(id);
//        if (!optionalReport.isPresent()) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Report does not exist");
//        }
//        Report report = optionalReport.get();
//        this.writeReportToFileSystemIfNotExists(report);
//
//        Map<String, Object> combinedParameters = combineParameters(id, parameters);
//
//        String folderPathStr = System.getProperty("java.io.tmpdir");
//        folderPathStr += report.getReportUuid() + "/";
//
//        String reportFilePathStr = folderPathStr + report.getReportFilename();
//
//        File reportFilePath = new File(reportFilePathStr);
//        JasperReport jasperReport = JasperCompileManager.compileReport(reportFilePath.getAbsolutePath());
//
//        for (Report subreport : report.getSubreports()) {
//            String subreportFilePathStr = folderPathStr + subreport.getReportFilename();
//            File subreportFilePath = new File(subreportFilePathStr);
//            JasperReport jasperSubReport = JasperCompileManager.compileReport(subreportFilePath.getAbsolutePath());
//            JRSaver.saveObject(jasperSubReport, folderPathStr + subreport.getReportFilename().replace(".jrxml", ".jasper"));
//            combinedParameters.put(subreport.getReportFilename().replace(".jrxml", ""),
//                    folderPathStr + subreport.getReportFilename().replace(".jrxml", ".jasper"));
//        }
//
//        Connection connection = dataSource.getConnection();
//        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, combinedParameters, connection);
//        JRXlsExporter a;
//        String reportType = this.reportRepository.findType(id);
//        switch (reportType) {
//            case "pdf":
//                response.addHeader("Content-disposition", "attachment; filename=file.pdf");
//                JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
//                break;
//            case "docx":
//                response.setContentType("application/docx");
//                response.setHeader("Content-disposition", "attachment; filename=file.docx");
//                ServletOutputStream docxServletOutputStream = response.getOutputStream();
//                JRDocxExporter jrDocxExporter = new JRDocxExporter();
//                jrDocxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
//                jrDocxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(docxServletOutputStream));
//                jrDocxExporter.exportReport();
//                docxServletOutputStream.flush();
//                break;
//            case "xls":
//                response.setContentType("application/vnd.ms-excel");
//                response.setHeader("Content-disposition", "attachment; filename=file.xls");
//                ServletOutputStream xlsServletOutputStream = response.getOutputStream();
//                JRXlsExporter exporter = new JRXlsExporter();
//                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
//                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(xlsServletOutputStream));
//                exporter.exportReport();
//                xlsServletOutputStream.flush();
//                break;
//            case "xlsx":
//                response.setContentType("application/vnd.ms-excel");
//                response.setHeader("Content-disposition", "attachment; filename=file.xls");
//                ServletOutputStream xlsxServletOutputStream = response.getOutputStream();
//                JRXlsxExporter jrXlsxExporter = new JRXlsxExporter();
//                jrXlsxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
//                jrXlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(xlsxServletOutputStream));
//                jrXlsxExporter.exportReport();
//                xlsxServletOutputStream.flush();
//                break;
//            case "csv":
//                response.setContentType("application/csv");
//                response.setHeader("Content-disposition", "attachment; filename=file.csv");
//                ServletOutputStream csvServletOutputStream = response.getOutputStream();
//                JRCsvExporter jrCsvExporter = new JRCsvExporter();
//                jrCsvExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
//                jrCsvExporter.setExporterOutput(new SimpleWriterExporterOutput(csvServletOutputStream));
//                jrCsvExporter.exportReport();
//                csvServletOutputStream.flush();
//                break;
//            case "text":
//                response.setContentType("application/csv");
//                response.setHeader("Content-disposition", "attachment; filename=file.csv");
//                ServletOutputStream textServletOutputStream = response.getOutputStream();
//                JRTextExporter jrTextExporter = new JRTextExporter();
//                jrTextExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
//                jrTextExporter.setExporterOutput(new SimpleWriterExporterOutput(textServletOutputStream));
//                jrTextExporter.exportReport();
//                textServletOutputStream.flush();
//                break;
//        }
//
//        connection.close();
//    }

    public void getFileReport(HttpServletResponse response, String id) throws IOException {
        Report optionalReport = reportRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException("Report Does Not Exist"));
        Report report = optionalReport;

        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename="+ report.getName());
        response.setStatus(HttpServletResponse.SC_OK);

        OutputStream outputStream = response.getOutputStream();
        outputStream.write(report.getReportFileData());
        outputStream.flush();
    }

//    private Map<String, Object> combineParameters(Long id, Map<String, Object> parameters) throws IOException {
//        List<ReportParameter> storedParameters = this.reportRepository.getReportParametersById(id);
//        storedParameters.forEach(storedParameter -> {
//            if (parameters.containsKey(storedParameter.getCode())) {
//                storedParameter.setValue(parameters.get(storedParameter.getCode()).toString());
//            }
//        });
//
//        Map<String, Object> combinedParameters = new HashMap<>();
//        combinedParameters.put("userId", this.jwtService.getUserId().toString());
//        storedParameters.forEach(storedParameter -> {
//            combinedParameters.put(storedParameter.getCode(), storedParameter.getValue());
//        });
//
//        return combinedParameters;
//    }

    private void deleteReportFolderFromFileSystemIfExists(String uuid) throws IOException {
        if ((uuid == null ? "" : uuid).equals("")) {
            return;
        }

        String folderPathStr = System.getProperty("java.io.tmpdir");
        folderPathStr += uuid + "/";
        File folderPath = new File(folderPathStr);
        if (folderPath.exists()) {
           // Files.delete(folderPath.toPath());
            FileUtils.deleteDirectory(folderPath);
        }
    }

//    private void writeReportToFileSystemIfNotExists(Report report) throws IOException {
//
//        // Create folder if does not exist
//        String folderPathStr = System.getProperty("java.io.tmpdir");
//        folderPathStr += report.getReportUuid() + "/";
//        File folderPath = new File(folderPathStr);
//        if (!folderPath.exists()) {
//            folderPath.mkdir();
//        }
//
//        // Create report jrxml file if does not exist
//        String reportFilePathStr = folderPathStr + report.getReportFilename();
//        Path reportFilePath = Paths.get(reportFilePathStr);
//        if (!Files.exists(reportFilePath)) {
//            byte[] fileData = report.getReportFileData();
//            Files.write(reportFilePath, fileData);
//        }
//
//        // Create subreports jrxml files if does not exist
//        for (Report subreport : report.getSubreports()) {
//            String subreportFilePathStr = folderPathStr + subreport.getReportFilename();
//            Path subreportFilePath = Paths.get(subreportFilePathStr);
//            if (!Files.exists(subreportFilePath)) {
//                byte[] fileData = subreport.getReportFileData();
//                Files.write(subreportFilePath, fileData);
//            }
//        }
//    }

    private ReportDTO base64JsonStringToReportDTO(String reportBase64Str) throws JsonProcessingException {
        byte[] reportBytes = Base64.getDecoder().decode(reportBase64Str);
        ObjectMapper mapper = new ObjectMapper();
        ReportDTO reportDTO = mapper.readValue(new String(reportBytes), ReportDTO.class);
        return reportDTO;
    }

    private byte[] getFileBytes(MultipartFile multipartFile) throws IOException {
        InputStream inputStream = multipartFile.getInputStream();
        byte[] byteArray = new byte[inputStream.available()];
        inputStream.read(byteArray);

        return byteArray;
    }

}
