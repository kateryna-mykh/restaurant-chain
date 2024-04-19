package parser.service.impl;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.File;
import parser.dto.StatisticsReportDto;
import parser.service.FileWriter;

public class XmlWriter implements FileWriter {
    private static XmlMapper xmlMapper = new XmlMapper();
    {
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public void jacksonAnnotation2Xml(String outputFile, StatisticsReportDto reportDto) {
        File reportFile = new File(outputFile);
        reportFile.getParentFile().mkdirs();
        try {
            xmlMapper.writeValue(reportFile, reportDto);
        } catch (Exception e) {
            throw new RuntimeException("Error writing to file " + outputFile, e);
        }
    }
}
