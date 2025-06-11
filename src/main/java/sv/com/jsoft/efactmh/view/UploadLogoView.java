package sv.com.jsoft.efactmh.view;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import sv.com.jsoft.efactmh.services.SessionService;

/**
 *
 * @author msanchez
 */
@Named
@RequestScoped
@Slf4j
public class UploadLogoView {

    private final static ResourceBundle VARIABLES = ResourceBundle.getBundle("variables");
    private String PATH_IMG_LOGO;

    @Inject
    SessionService sessionService;

    @PostConstruct
    public void init() {
        PATH_IMG_LOGO = VARIABLES.getString("path.img-logo");
    }

    public void handleFileUpload(FileUploadEvent event) {
        UploadedFile file = event.getFile();

        if (file != null && file.getContent() != null && file.getContent().length > 0 && file.getFileName() != null) {
            File imgLogo = new File(PATH_IMG_LOGO + File.separator + sessionService.getParametroDto().getUserJwt().concat(".jpg"));

            try (InputStream input = file.getInputStream()) {
                Files.copy(input, imgLogo.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                log.error("ERROR CREANDO IMAGEN DE LOGO PARA EMISOR: " + sessionService.getParametroDto().getUserJwt());
            }
        }
    }
}
