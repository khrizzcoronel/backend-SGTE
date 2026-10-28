package com.app.backend.services.externos;

/**
 * Plantillas HTML de correo para eventos de solicitudes/trámites.
 */
public final class PlantillaCorreoTramite {

    private PlantillaCorreoTramite() {}

    private static String base(String contenido) {
        return """
        <!DOCTYPE html>
        <html lang="es">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <style>
                body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f7f6; margin: 0; padding: 0; color: #333; }
                .email-container { max-width: 600px; margin: 40px auto; background: #fff; border-radius: 12px; box-shadow: 0 4px 15px rgba(0,0,0,0.05); overflow: hidden; border: 1px solid #e0e0e0; }
                .header { background: #fff; padding: 25px 20px; text-align: center; border-bottom: 3px solid #087f38; }
                .header h1 { color: #087f38; margin: 0; font-size: 22px; }
                .content { padding: 30px; }
                .content h2 { color: #333; font-size: 18px; margin-top: 0; }
                .content p { line-height: 1.6; font-size: 15px; margin-bottom: 15px; }
                .info-box { background: #edf2fb; border: 1px solid #d7e3f4; border-radius: 10px; padding: 20px; margin: 20px 0; }
                .info-box p { margin: 6px 0; font-size: 14px; }
                .info-box .label { color: #6c757d; font-size: 12px; text-transform: uppercase; letter-spacing: 0.05em; }
                .info-box .value { font-weight: bold; color: #212529; }
                .badge { display: inline-block; padding: 4px 14px; border-radius: 20px; font-size: 13px; font-weight: 600; color: #fff; }
                .badge-success { background: #059669; }
                .badge-danger { background: #dc2626; }
                .badge-warning { background: #d97706; }
                .badge-info { background: #2563eb; }
                .btn-container { text-align: center; margin: 25px 0 10px 0; }
                .btn { background: #087f38; color: #fff !important; text-decoration: none; padding: 12px 30px; border-radius: 8px; font-weight: bold; font-size: 15px; display: inline-block; }
                .footer { background: #212529; color: #fff; text-align: center; padding: 15px; font-size: 12px; }
                .divider { border: none; border-top: 1px solid #e9ecef; margin: 20px 0; }
            </style>
        </head>
        <body>
            <div class="email-container">
                <div class="header">
                    <h1>UTEQ | SGTE</h1>
                </div>
                <div class="content">
                    %s
                </div>
                <div class="footer">
                    Sistema de Gestión de Trámites Estudiantiles<br>
                    Universidad Técnica Estatal de Quevedo
                </div>
            </div>
        </body>
        </html>
        """.formatted(contenido);
    }

    public static String solicitudCreada(String nombreUsuario, String codigoSolicitud,
                                          String nombrePlantilla, String prioridad) {
        String contenido = """
            <h2>Solicitud Registrada</h2>
            <p>Estimado(a) <strong>%s</strong>,</p>
            <p>Su solicitud ha sido registrada exitosamente en el sistema y se encuentra en proceso de revisión.</p>
            <div class="info-box">
                <p><span class="label">Código</span><br><span class="value">%s</span></p>
                <p><span class="label">Tipo de trámite</span><br><span class="value">%s</span></p>
                <p><span class="label">Prioridad</span><br><span class="value">%s</span></p>
                <p><span class="label">Estado</span><br><span class="badge badge-warning">Pendiente</span></p>
            </div>
            <p>Recibirá notificaciones a medida que su solicitud avance en el flujo de aprobación.</p>
            <div class="btn-container">
                <a href="http://localhost:4200" class="btn">Ver mi solicitud</a>
            </div>
            <p style="margin-top: 25px;">Atentamente,<br><strong>Equipo SGTE UTEQ</strong></p>
        """.formatted(nombreUsuario, codigoSolicitud, nombrePlantilla, prioridad);
        return base(contenido);
    }

    public static String pasoAprobado(String nombreUsuario, String codigoSolicitud,
                                       String nombrePlantilla, String etapaAprobada,
                                       String aprobadoPor, String siguienteEtapa) {
        String siguienteInfo = siguienteEtapa != null
                ? "<p>La solicitud ha avanzado a la siguiente etapa: <strong>" + siguienteEtapa + "</strong>.</p>"
                : "<p><span class=\"badge badge-success\">La solicitud ha sido finalizada exitosamente.</span></p>";

        String contenido = """
            <h2>Etapa Aprobada</h2>
            <p>Estimado(a) <strong>%s</strong>,</p>
            <p>Le informamos que una etapa de su solicitud ha sido aprobada.</p>
            <div class="info-box">
                <p><span class="label">Código</span><br><span class="value">%s</span></p>
                <p><span class="label">Trámite</span><br><span class="value">%s</span></p>
                <p><span class="label">Etapa aprobada</span><br><span class="value">%s</span></p>
                <p><span class="label">Aprobado por</span><br><span class="value">%s</span></p>
            </div>
            %s
            <div class="btn-container">
                <a href="http://localhost:4200" class="btn">Ver seguimiento</a>
            </div>
            <p style="margin-top: 25px;">Atentamente,<br><strong>Equipo SGTE UTEQ</strong></p>
        """.formatted(nombreUsuario, codigoSolicitud, nombrePlantilla, etapaAprobada, aprobadoPor, siguienteInfo);
        return base(contenido);
    }

    public static String solicitudRechazada(String nombreUsuario, String codigoSolicitud,
                                             String nombrePlantilla, String etapa,
                                             String rechazadoPor, String comentarios) {
        String comentarioHtml = (comentarios != null && !comentarios.isBlank())
                ? "<hr class=\"divider\"><p><strong>Observaciones:</strong></p><p style=\"font-style: italic; color: #6c757d;\">« " + comentarios + " »</p>"
                : "";

        String contenido = """
            <h2>Solicitud Rechazada</h2>
            <p>Estimado(a) <strong>%s</strong>,</p>
            <p>Lamentamos informarle que su solicitud ha sido rechazada en la etapa de revisión.</p>
            <div class="info-box">
                <p><span class="label">Código</span><br><span class="value">%s</span></p>
                <p><span class="label">Trámite</span><br><span class="value">%s</span></p>
                <p><span class="label">Etapa</span><br><span class="value">%s</span></p>
                <p><span class="label">Rechazado por</span><br><span class="value">%s</span></p>
                <p><span class="label">Estado</span><br><span class="badge badge-danger">Rechazado</span></p>
            </div>
            %s
            <p>Si tiene consultas, puede acercarse a coordinación para mayor información.</p>
            <div class="btn-container">
                <a href="http://localhost:4200" class="btn">Ver detalles</a>
            </div>
            <p style="margin-top: 25px;">Atentamente,<br><strong>Equipo SGTE UTEQ</strong></p>
        """.formatted(nombreUsuario, codigoSolicitud, nombrePlantilla, etapa, rechazadoPor, comentarioHtml);
        return base(contenido);
    }

    public static String solicitudFinalizada(String nombreUsuario, String codigoSolicitud,
                                              String nombrePlantilla) {
        String contenido = """
            <h2>Solicitud Finalizada</h2>
            <p>Estimado(a) <strong>%s</strong>,</p>
            <p>Nos complace informarle que su solicitud ha completado todo el flujo de aprobación exitosamente.</p>
            <div class="info-box">
                <p><span class="label">Código</span><br><span class="value">%s</span></p>
                <p><span class="label">Trámite</span><br><span class="value">%s</span></p>
                <p><span class="label">Estado</span><br><span class="badge badge-success">Finalizado</span></p>
            </div>
            <p>Puede acceder al sistema para revisar la resolución y los detalles finales de su trámite.</p>
            <div class="btn-container">
                <a href="http://localhost:4200" class="btn">Ver resolución</a>
            </div>
            <p style="margin-top: 25px;">Atentamente,<br><strong>Equipo SGTE UTEQ</strong></p>
        """.formatted(nombreUsuario, codigoSolicitud, nombrePlantilla);
        return base(contenido);
    }

    public static String notificacionGestor(String nombreGestor, String codigoSolicitud,
                                              String nombrePlantilla, String estudiante,
                                              String etapa, String accionRequerida) {
        String contenido = """
            <h2>Acción Requerida</h2>
            <p>Estimado(a) <strong>%s</strong>,</p>
            <p>Se requiere su intervención en la siguiente solicitud:</p>
            <div class="info-box">
                <p><span class="label">Código</span><br><span class="value">%s</span></p>
                <p><span class="label">Trámite</span><br><span class="value">%s</span></p>
                <p><span class="label">Estudiante</span><br><span class="value">%s</span></p>
                <p><span class="label">Etapa actual</span><br><span class="value">%s</span></p>
                <p><span class="label">Acción</span><br><span class="badge badge-info">%s</span></p>
            </div>
            <p>Por favor, ingrese al sistema para revisar y gestionar esta solicitud.</p>
            <div class="btn-container">
                <a href="http://localhost:4200" class="btn">Gestionar solicitud</a>
            </div>
            <p style="margin-top: 25px;">Atentamente,<br><strong>Equipo SGTE UTEQ</strong></p>
        """.formatted(nombreGestor, codigoSolicitud, nombrePlantilla, estudiante, etapa, accionRequerida);
        return base(contenido);
    }
}
