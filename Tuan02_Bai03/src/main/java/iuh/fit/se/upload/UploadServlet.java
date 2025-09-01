package iuh.fit.se.upload;

import jakarta.servlet.*;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.*;

import java.io.*;
import java.nio.file.*;
import java.sql.*;

@MultipartConfig
public class UploadServlet extends HttpServlet {

    private String uploadPathToSource;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        uploadPathToSource = "D:\\WWW\\LeNguyenSinh_WWW_Tuan02\\Tuan02_Bai03\\src\\main\\webapp\\uploads";
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        Part filePart = req.getPart("photo");

        String fileUploadName = null;
        byte[] fileBytes = null;

        if (filePart != null && filePart.getSize() > 0) {
            fileUploadName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

            try (InputStream inputStream = filePart.getInputStream()) {
                fileBytes = inputStream.readAllBytes();
            }
        }

        String message;

        try (Connection conn = DBConnection.getConnection()) {

            String sqlInsert = "INSERT INTO contacts (first_name, last_name, photo) VALUES (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sqlInsert);
            statement.setString(1, firstName);
            statement.setString(2, lastName);

            if (fileBytes != null) {
                statement.setBytes(3, fileBytes); // dùng byte[] thay vì InputStream
            } else {
                statement.setNull(3, Types.BLOB);
            }

            int row = statement.executeUpdate();

            if (row > 0 && fileUploadName != null && fileBytes != null) {
                Path uploadDir = Paths.get(uploadPathToSource);
                Files.createDirectories(uploadDir); // đảm bảo thư mục tồn tại

                Path filePath = uploadDir.resolve(fileUploadName);
                Files.write(filePath, fileBytes);   // ghi file

                message = "File uploaded & saved to DB, copied to uploads/" + fileUploadName;
            } else {
                message = "Upload failed!";
            }

        } catch (SQLException e) {
            message = "ERROR: " + e.getMessage();
            e.printStackTrace();
        }

        req.setAttribute("message", message);
        getServletContext().getRequestDispatcher("/message").forward(req, resp);
    }
}
