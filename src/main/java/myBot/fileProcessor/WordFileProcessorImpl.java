package myBot.fileProcessor;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class WordFileProcessorImpl {

    public FileInputStream createWordFile(List<String> taskList) throws IOException {
        XWPFDocument doc = new XWPFDocument(Objects.requireNonNull(getClass().getClassLoader().
                getResourceAsStream("Template.docx")));
        setTaskListToXWPFDocument(doc, taskList);
        return createTempFile(doc);
    }

    private void setTaskListToXWPFDocument(XWPFDocument doc, List<String> taskList) {
        XWPFParagraph paragraph = doc.getLastParagraph();
        XWPFRun run = paragraph.createRun();
        setRunParameters(run, taskList.get(0));

        for (String s : taskList) {
            paragraph = doc.createParagraph();
            run = paragraph.createRun();
            setRunParameters(run, s);
        }
    }

    private void setRunParameters(XWPFRun run, String task) {
        run.setFontSize(25);
        run.setFontFamily("Calibri");
        run.setText(task);
    }

    private FileInputStream createTempFile(XWPFDocument doc) throws IOException {
        File result = File.createTempFile("Print_Me", ".docx");
        try (FileOutputStream out = new FileOutputStream(result)) {
            doc.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new FileInputStream(result);
    }
}
