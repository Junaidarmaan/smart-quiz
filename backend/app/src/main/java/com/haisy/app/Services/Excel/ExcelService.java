package com.haisy.app.Services.Excel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.haisy.app.Services.WebSocket.UserProfile;

@Service
public class ExcelService {

    /**
     * Generates leaderboard Excel file as byte[]
     * Caller decides how to return or store it.
     */
    public byte[] generateLeaderboardExcel(String quizId, List<UserProfile> leaderboard) {

        if (leaderboard == null || leaderboard.isEmpty()) {
            System.out.println("empty leaderboard waste to genrate xl ");
        }

        try (
            Workbook workbook = new XSSFWorkbook();
            ByteArrayOutputStream out = new ByteArrayOutputStream()
        ) {

            Sheet sheet = workbook.createSheet("Leaderboard");

            // Header
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Rank");
            header.createCell(1).setCellValue("Username");
            header.createCell(2).setCellValue("Score");
            header.createCell(3).setCellValue("Questions Answered");

            // Data rows
            int rowIdx = 1;
            int rank = 1;

            for (UserProfile user : leaderboard) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(rank++);
                row.createCell(1).setCellValue(user.getUserName());
                row.createCell(2).setCellValue(user.getScore());
                row.createCell(3).setCellValue(user.getCurQuestion());
            }

            // Auto-size columns (cosmetic but professional)
            for (int i = 0; i < 4; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Failed to generate leaderboard Excel", e);
        }
    }
}
