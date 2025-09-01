package iuh.fit.se.upload;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.*;

import java.io.*;
import java.nio.file.*;

@MultipartConfig
public class MultiUploadServlet extends HttpServlet {

    private Path uploadDir;

    @Override
    public void init() {
        uploadDir = Paths.get("D:\\WWW\\LeNguyenSinh_WWW_Tuan02\\Tuan02_Bai02\\src\\main\\webapp\\uploads");
        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create upload folder", e);
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.println("<h2>Kết quả upload:</h2>");

        for (int i = 1; i <= 5; i++) {
            Part part = req.getPart("file" + i);
            if (part != null && part.getSize() > 0) {
                String filename = Paths.get(part.getSubmittedFileName())
                        .getFileName().toString();
                Path target = uploadDir.resolve(filename);

                try (InputStream in = part.getInputStream()) {
                    Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
                }

                out.println("File #" + i + ": " + filename + " [Saved successfully]<br>");
            } else {
                out.println("File #" + i + ": do not select file<br>");
            }
        }
    }
}
