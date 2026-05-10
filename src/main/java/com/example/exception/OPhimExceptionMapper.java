package com.example.exception;

import com.example.dto.ErrorResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

@Provider
public class OPhimExceptionMapper implements ExceptionMapper<OPhimException> {
    private static final Logger LOG = Logger.getLogger(OPhimExceptionMapper.class);

    @Override
    public Response toResponse(OPhimException exception) {
        if (exception.status() >= 500) {
            LOG.errorv(exception, "Returning OPhim error response: status={0}, message={1}",
                    exception.status(),
                    exception.getMessage());
        } else {
            LOG.warnv("Returning OPhim error response: status={0}, message={1}",
                    exception.status(),
                    exception.getMessage());
        }

        return Response.status(exception.status())
                .entity(ErrorResponse.error(exception.getMessage()))
                .build();
    }
}
