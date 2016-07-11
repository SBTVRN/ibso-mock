package ru.sbt.drtmn.lab.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import ru.sbt.drtmn.lab.model.Configuration;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created: 05.07.2016.
 *
 * @author <a href="mailto:svrazuvaev@yandex.ru">Razuvaev Sergey</a>
 *         Short Class Description.
 */
public class FileUtil {
    private static Logger logger = Logger.getLogger(FileUtil.class);

    public static String getResourceAsString(String resource) throws IOException {
        InputStream stream = FileUtil.class.getClassLoader().getResourceAsStream(resource);
        String str = IOUtils.toString(stream, "UTF-8");
        return str;
    }

    public static Properties getProperties(String name) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(name));
        return properties;
    }

    public static Configuration importConfigurationFromFile(File configurationFile) {
        Configuration configuration = null;
        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(Configuration.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            configuration = (Configuration)jaxbUnmarshaller.unmarshal(configurationFile);
        } catch (JAXBException e) {
            logger.error("Error when unmarshalling xml template", e);
        }
        return configuration;
    }

    public static File exportConfigurations(List<Configuration> configurations) {
        String tmpDir = System.getProperty("java.io.tmpdir");
        if ( tmpDir == null ){
            logger.error("No temporary dir found. Variable java.io.tmpdir must be set.");
            return null;
        }
        File sessionDir = new File(tmpDir + System.getProperty("file.separator") + UUID.randomUUID());
        if ( !(sessionDir.mkdir()) ) {
            logger.error("Error when creating temporary dir ");
            return null;
        }
        List<File> templateFiles = new ArrayList();
        for (Configuration configuration : configurations) {
            BufferedOutputStream out = null;
            try {
                File template = new File(sessionDir + System.getProperty("file.separator") + configuration.getName() + ".xml" );
                out = new BufferedOutputStream(new FileOutputStream(template));
                JAXBContext jaxbContext = JAXBContext.newInstance(Configuration.class);
                Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
                jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                jaxbMarshaller.marshal(configuration, out);
                templateFiles.add(template);
            } catch (JAXBException jaxbe) {
                logger.error("Error when marshalling configuration to xml file.\n" + configuration, jaxbe);
                return null;
            } catch (IOException ioe) {
                logger.error("Error when writing template file on hdd.\n" + configuration, ioe);
                return null;
            } finally {
                try {
                    out.close();
                } catch (IOException ioe) {
                    logger.warn("Error when closing ByteArrayOutputStream or BufferedWriter.\n" + configuration, ioe);
                }
            }
        }
        File zipFile = new File(sessionDir + System.getProperty("file.separator") + "IbsoMockMessageConfigurations.zip");
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        BufferedOutputStream bos = null;
        BufferedInputStream in = null;
        try {
            fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(fos);
            bos = new BufferedOutputStream(zos);
            for(File templateFile : templateFiles) {
                in = new BufferedInputStream(new FileInputStream(templateFile));
                zos.putNextEntry(new ZipEntry(templateFile.getName()));
                int c;
                while ( (c = in.read()) != -1 ) {
                    bos.write(c);
                }
                in.close();
                bos.flush();
                zos.closeEntry();
            }
        } catch (IOException e) {
            // Return null because zip file is corrupted
            zipFile = null;
            logger.error("Error when creating Zip file ", e);
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                logger.warn("Error when closing Buffered Reader ", e);
            }
            try {
                bos.close();
            } catch (Exception e) {
                logger.warn("Error when closing Buffered ZipOutputStream file ", e);
            }
            for (File templateFile : templateFiles) {
                templateFile.delete();
            }
        }
        return zipFile;
    }

    public static List<File> processUploadedFile(File temporaryFile, String name) throws Exception {
        // Save temporaryFile
        String tmpDir = System.getProperty("java.io.tmpdir");
        if ( tmpDir == null ){
            logger.error("No temporary dir found. Variable java.io.tmpdir must be set.");
            throw new IllegalArgumentException("No temporary dir found. Variable java.io.tmpdir must be set.");
        }
        File sessionDir = new File(tmpDir + System.getProperty("file.separator") + UUID.randomUUID());
        if ( !(sessionDir.mkdir()) ) {
            logger.error("Error when creating temporary dir");
            throw new IOException("Error when creating temporary dir");
        }
        List<File> processedFiles = new ArrayList();
        File uploadedFile = null;
        try {
            uploadedFile = new File(sessionDir.getAbsolutePath() + System.getProperty("file.separator") + name);
            FileUtils.copyFile(temporaryFile, uploadedFile);
            // Check File Extension and process file
            String extension = uploadedFile.getName().substring(uploadedFile.getName().lastIndexOf('.')+1);
            if (extension.equalsIgnoreCase("xml")) {
                processedFiles.add(uploadedFile);
            } else if (extension.equalsIgnoreCase("zip")) {
                BufferedInputStream in = null;
                ZipInputStream zis = null;
                try {
                    zis = new ZipInputStream(new FileInputStream(uploadedFile));
                    in = new BufferedInputStream(zis);
                    ZipEntry entry;
                    while( ( entry = zis.getNextEntry() ) != null ){
                        File template = new File(sessionDir, entry.getName());
                        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(template));
                        int x = 0;
                        while ( ( x = zis.read() ) != -1 ) {
                            out.write(x);
                        }
                        try {
                            out.close();
                        } catch (IOException ioe) {
                            logger.warn("Unzip error. Error when closing Buffered output stream", ioe);
                        }
                        processedFiles.add(template);
                    }
                } catch (Exception e){
                    logger.error("Unzip error. Error when unzipping uploaded FIle " + uploadedFile, e);
                } finally {
                    try {
                        in.close();
                    } catch(IOException ioe) {
                        logger.warn("Unzip error. Error when closing Buffered Input Stream" + ioe);
                    }
                }
            } else {
                logger.error("Incorrect file format");
                throw new Exception("Incorrect file format");
            }
        } catch (Exception e) {
            logger.error("Error when working with uploaded file " + uploadedFile, e);
            throw e;
        }
        return processedFiles;
    }

}
