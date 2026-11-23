package co.edu.unipiloto.plataforma.excepcion;

import co.edu.unipiloto.plataforma.dto.ErrorRespuesta;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Convierte las excepciones en respuestas JSON con el mismo formato:
 * { timestamp, status, error, mensaje, ruta, detalles }
 */
@RestControllerAdvice
public class ManejadorErrores {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorRespuesta> noEncontrado(RecursoNoEncontradoException ex, HttpServletRequest req) {
        return construir(HttpStatus.NOT_FOUND, ex.getMessage(), req, null);
    }

    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ErrorRespuesta> reglaNegocio(ReglaNegocioException ex, HttpServletRequest req) {
        return construir(HttpStatus.BAD_REQUEST, ex.getMessage(), req, null);
    }

    @ExceptionHandler(ConflictoException.class)
    public ResponseEntity<ErrorRespuesta> conflicto(ConflictoException ex, HttpServletRequest req) {
        return construir(HttpStatus.CONFLICT, ex.getMessage(), req, null);
    }

    @ExceptionHandler(ProhibidoException.class)
    public ResponseEntity<ErrorRespuesta> prohibido(ProhibidoException ex, HttpServletRequest req) {
        return construir(HttpStatus.FORBIDDEN, ex.getMessage(), req, null);
    }

    @ExceptionHandler(NoAutorizadoException.class)
    public ResponseEntity<ErrorRespuesta> noAutorizado(NoAutorizadoException ex, HttpServletRequest req) {
        return construir(HttpStatus.UNAUTHORIZED, ex.getMessage(), req, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorRespuesta> validacion(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<String> detalles = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            detalles.add(error.getField() + ": " + error.getDefaultMessage());
        }
        return construir(HttpStatus.BAD_REQUEST, "Hay datos invalidos en la peticion", req, detalles);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorRespuesta> jsonInvalido(HttpMessageNotReadableException ex, HttpServletRequest req) {
        return construir(HttpStatus.BAD_REQUEST,
                "El cuerpo de la peticion no es un JSON valido o tiene un valor incorrecto "
                        + "(fechas con formato 2026-12-31T23:59:00, enumerados escritos en MAYUSCULAS)", req, null);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorRespuesta> tipoIncorrecto(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        return construir(HttpStatus.BAD_REQUEST,
                "El parametro '" + ex.getName() + "' tiene un valor incorrecto", req, null);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorRespuesta> parametroFaltante(MissingServletRequestParameterException ex, HttpServletRequest req) {
        return construir(HttpStatus.BAD_REQUEST,
                "Falta el parametro obligatorio '" + ex.getParameterName() + "'", req, null);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorRespuesta> metodoNoPermitido(HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {
        return construir(HttpStatus.METHOD_NOT_ALLOWED, "Metodo HTTP no permitido para esta ruta: " + ex.getMethod(), req, null);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ErrorRespuesta> parteFaltante(MissingServletRequestPartException ex, HttpServletRequest req) {
        return construir(HttpStatus.BAD_REQUEST,
                "Falta el campo '" + ex.getRequestPartName() + "' (en Postman: Body > form-data, tipo File)", req, null);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorRespuesta> archivoGrande(MaxUploadSizeExceededException ex, HttpServletRequest req) {
        return construir(HttpStatus.PAYLOAD_TOO_LARGE, "El archivo supera el tamano maximo permitido (10 MB)", req, null);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ErrorRespuesta> noMultipart(MultipartException ex, HttpServletRequest req) {
        return construir(HttpStatus.BAD_REQUEST,
                "La peticion debe enviarse como multipart/form-data con el archivo (en Postman: Body > form-data, tipo File)", req, null);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorRespuesta> tipoNoSoportado(HttpMediaTypeNotSupportedException ex, HttpServletRequest req) {
        return construir(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                "Falta el encabezado Content-Type: application/json (en Postman: Body > raw > JSON)", req, null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorRespuesta> integridad(DataIntegrityViolationException ex, HttpServletRequest req) {
        return construir(HttpStatus.CONFLICT,
                "La operacion viola una restriccion de la base de datos (registro duplicado o con datos asociados)", req, null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRespuesta> inesperado(Exception ex, HttpServletRequest req) {
        return construir(HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado en el servidor: " + ex.getMessage(), req, null);
    }

    private ResponseEntity<ErrorRespuesta> construir(HttpStatus estado, String mensaje, HttpServletRequest req,
                                                     List<String> detalles) {
        ErrorRespuesta cuerpo = new ErrorRespuesta();
        cuerpo.setTimestamp(LocalDateTime.now());
        cuerpo.setStatus(estado.value());
        cuerpo.setError(estado.getReasonPhrase());
        cuerpo.setMensaje(mensaje);
        cuerpo.setRuta(req.getRequestURI());
        cuerpo.setDetalles(detalles);
        return ResponseEntity.status(estado).body(cuerpo);
    }
}
