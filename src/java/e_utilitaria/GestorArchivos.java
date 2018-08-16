/**
 * Permite manipular archivos y carpetas
 */
package e_utilitaria;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.primefaces.model.UploadedFile;
import org.apache.commons.io.FileUtils;

public class GestorArchivos {

    /**
     * Construye un File a partir de un UploadedFile
     * @param archivoSubido
     * @return
     * @throws ExceptionFatal 
     */
    public static File construirArchivo(UploadedFile archivoSubido) throws ExceptionFatal {
        try {
            File archivo = new File(archivoSubido.getFileName());
            InputStream in = archivoSubido.getInputstream();
            OutputStream out = new FileOutputStream(archivo);
            int reader = 0;
            byte[] bytes = new byte[(int) archivoSubido.getSize()];
            while ((reader = in.read(bytes)) != -1) out.write(bytes, 0, reader);
            in.close();
            out.flush();
            out.close();
            return archivo;
        } catch (Exception e) {
            throw new ExceptionFatal("Error al crear el archivo " + archivoSubido.getFileName());
        }
    }
    
    /**
     * Crea un archivo en disco en la ruta de File con la información de UploadedFile
     * @param archivo
     * @param archivoSubido
     * @throws ExceptionFatal 
     */
    public static void crearArchivo(File archivo, UploadedFile archivoSubido) throws ExceptionFatal {
        try {
            InputStream in = archivoSubido.getInputstream();
            OutputStream out = new FileOutputStream(archivo);
            int reader = 0;
            byte[] bytes = new byte[(int) archivoSubido.getSize()];
            while ((reader = in.read(bytes)) != -1)
                out.write(bytes, 0, reader);
            in.close();
            out.flush();
            out.close();
        } catch (Exception e) {
            throw new ExceptionFatal("Error al crear el archivo " + archivoSubido.getFileName());
        }
    }

    /**
     * Crea una carpeta en disco
     * @param url
     * @throws ExceptionFatal 
     */
    public static void crearCarpeta(String url) throws ExceptionFatal {
        try{
            (new File(url)).mkdirs();
        } catch (Exception e) {
            throw new ExceptionFatal("Error al crear la carpeta " + url);
        }
    }

    /**
     * Devuelve el último elemento encontrado en un "split" sobre un punto (.) y
     * por tanto es la extensión del archivo. Si no tiene extensión devuelve
     * vacío
     * @param archivoSubido
     * @return
     * @throws ExceptionFatal 
     */
    public static String extension(UploadedFile archivoSubido) throws ExceptionFatal {
        try{
            String nombreArchivo = archivoSubido.getFileName();
            String[] separacion = Helper.dividir(nombreArchivo, ".");
            if (separacion[separacion.length - 1].equals(nombreArchivo)) return "";
            return separacion[separacion.length - 1];
        }catch(Exception e){
            throw new ExceptionFatal("Error al determinar la extensión del archivo " + archivoSubido.getFileName());
        }
    }
    
    /**
     * A partir de un BufferedReader, se construye una lista String donde cada posición
     * es una fila del archivo leído.
     * @param br
     * @return
     * @throws ExceptionFatal 
     */
    public static List<String> lineasArchivo(BufferedReader br) throws ExceptionFatal {
        try{
            String linea;
            ArrayList<String> lineasArchivo = new ArrayList<>();
            while ((linea = br.readLine()) != null) lineasArchivo.add(linea);
            return lineasArchivo;
        }catch(Exception e){
            throw new ExceptionFatal("Error al generar la lista con las líneas del archivo. " + e.getMessage());
        }
    }

    /**
     * Devuelve el nombre del archivo sin la extensión.
     * @param nombreOriginalArchivo
     * @param extension
     * @return
     * @throws ExceptionFatal 
     */
    public static String nombreArchivo(String nombreOriginalArchivo, String extension) throws ExceptionFatal {
        try{
            if (extension.equals("")) return extension;
            return nombreOriginalArchivo.substring(0, nombreOriginalArchivo.length() - extension.length() - 1);
        }catch(Exception e){
            throw new ExceptionFatal("Error al separar el nombre del archivo de su extensión. " + e.getMessage());
        }
    }

    /**
     * Permite reconocer el tipo de documento de acuerdo a su extensión
     * @param extension
     * @return
     * @throws ExceptionFatal 
     */
    public static String tipoArchivo(String extension) throws ExceptionFatal {
        try{
            extension = extension.toLowerCase();
            switch (extension) {
                case "docx":
                case "doc":
                    return "word";
                case "pdf":
                    return "pdf";
                case "xlsx":
                case "xls":
                    return "excel";
                case "ppt":
                case "pptx":
                    return "powerpoint";
                case "bmp":
                case "jpg":
                case "jpeg":
                case "png":
                case "gif":
                case "tiff":
                    return "imagen";
                case "mp3":
                case "wav":
                case "wma":
                case "aac":
                    return "sonido";
                case "mp4":
                case "mkv":
                case "avi":
                case "mpeg":
                case "wmv":
                case "flv":
                    return "video";
                case "java":
                case "php":
                case "cpp":
                case "c":
                case "sql":
                case "html":
                case "css":
                case "js":
                case "class":
                case "py":
                case "rb":
                    return "codigo";
                case "rar":
                case "zip":
                case "7z":
                    return "comprimido";
                default:
                    return "otro";
            }
        }catch(Exception e){
            throw new ExceptionFatal("Error al reconocer el tipo de documento " + extension + ". " + e.getMessage());
        }
    }

    /**
     * Permite eliminar un archivo basado en su rutaa completa.
     * @param ruta
     * @throws ExceptionFatal 
     */
    public static void eliminarArchivo(String ruta) throws ExceptionFatal {
        try{
            new File(ruta).delete();
        }catch(Exception e){
            throw new ExceptionFatal("Error al eliminar el archivo " + ruta);
        }
    }

    /**
     * Permite eliminar una carpeta basado en su rutaa completa.
     * @param ruta
     * @throws ExceptionFatal 
     */
    public static void eliminarCarpeta(String ruta) throws ExceptionFatal {
        try{
            FileUtils.deleteDirectory(new File(ruta));
        }catch(Exception e){
            throw new ExceptionFatal("Error al eliminar la carpeta " + ruta);
        }
    }

    /**
     * Identifica si el archivo existe o no, dada la ruta.
     * @param ruta
     * @return
     * @throws ExceptionFatal 
     */
    public static boolean existe(String ruta) throws ExceptionFatal {
        try{
            return new File(ruta).exists();
        }catch(Exception e){
            throw new ExceptionFatal("Error al identificar si la ruta " + ruta + " existe");
        }
    }    
}