package parser.service;

import parser.dto.StatisticsReportDto;

public interface FileWriter {
    void jacksonAnnotation2Xml(String outputFile, StatisticsReportDto reportDto);
}
