package sv.com.jsoft.efactmh.servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author misanchez
 */
public class DynamicImageServlet extends HttpServlet {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("variables");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        cargarImagen(response, request.getParameter("file"));
    }

    private void cargarImagen(HttpServletResponse response, String logoImg) {
        try {
            byte[] bytes;
            String pathImg = System.getProperty("os.name").toUpperCase()
                    .contains("WINDOWS") ? RESOURCE_BUNDLE.getString("path.img-logo.win") : RESOURCE_BUNDLE.getString("path.img-logo.linuc");

            log("path img: " + pathImg);

            if (logoImg != null && logoImg.trim().isEmpty()) {
                logoImg = "logo-dte.jpg";
            }

            // Get image contents.
            try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(pathImg + File.separator + logoImg))) {
                // Get image contents.
                bytes = new byte[in.available()];
                in.read(bytes);
            }

            // Write image contents to response.
            response.getOutputStream().write(bytes);
        } catch (IOException e) {
            cargarImagen(response, "logo-dte.jpg");
        }
    }
}
