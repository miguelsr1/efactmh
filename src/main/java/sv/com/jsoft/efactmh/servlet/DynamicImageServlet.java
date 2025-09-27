package sv.com.jsoft.efactmh.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author misanchez
 */
//@WebServlet("/DynamicImageServlet")
public class DynamicImageServlet extends HttpServlet {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("variables");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        cargarImagen(response, request.getParameter("file"));
    }

    private void cargarImagen(HttpServletResponse response, String logoImg) {
        try {
            byte[] bytes;
            String pathImg = System.getProperty("os.name").toLowerCase().contains("win")
                    ? RESOURCE_BUNDLE.getString("path.img-logo.win")
                    : RESOURCE_BUNDLE.getString("path.img-logo.linux");

            log("path img: " + pathImg);

            if (logoImg == null) {
                logoImg = "logo-dte.jpg";
            } else if (logoImg.trim().isEmpty()) {
                logoImg = "logo-dte.jpg";
            } else {
                logoImg = "logos".concat(File.separator).concat(logoImg).concat(".jpg");
            }
            
            File archivo = new File(pathImg.concat(File.separator).concat(logoImg));

            if (archivo.exists()) {
                response.setContentType(getServletContext().getMimeType(archivo.getName()));
                response.setContentLength((int) archivo.length());

                try (FileInputStream in = new FileInputStream(archivo); OutputStream out = response.getOutputStream()) {
                    byte[] buffer = new byte[4096];
                    int bytesLeidos;
                    while ((bytesLeidos = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesLeidos);
                    }
                }
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }

            // Get image contents.
            /*try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(pathImg.concat(File.separator).concat(logoImg)))) {
                // Get image contents.
                bytes = new byte[in.available()];
                in.read(bytes);
            }

            // Write image contents to response.
            response.getOutputStream().write(bytes);*/
        } catch (IOException e) {
            cargarImagen(response, null);
        }
    }
}
